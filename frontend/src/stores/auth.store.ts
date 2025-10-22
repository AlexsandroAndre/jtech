// src/stores/auth.store.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'
import { usersApi } from '@/api/users'
import type { LoginDto, RegisterDto, User } from '@/types'
import { jwtDecode } from 'jwt-decode'

interface JwtPayload {
  sub: string
  [key: string]: any
}

function looksLikeUuid(value: string): boolean {
  return typeof value === 'string' && value.length === 36 && value.includes('-')
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(null)
  const user = ref<User | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  const isAuthenticated = computed(() => !!token.value && !!user.value)

  async function resolveUserFromToken(currentToken: string) {
    const decoded = jwtDecode<JwtPayload>(currentToken)
    const subject = decoded.sub // pode ser id (uuid) ou e-mail

    if (looksLikeUuid(subject)) {
      const userData = await usersApi.getUserById(subject)
      user.value = userData
      return
    }

    // sub é e-mail -> busca usuário por e-mail para obter UUID
    const userData = await usersApi.getUserByEmail(subject)
    user.value = userData
  }

  async function login(credentials: LoginDto) {
    try {
      loading.value = true
      error.value = null

      const receivedToken = await authApi.login(credentials)
      token.value = receivedToken
      localStorage.setItem('auth_token', receivedToken)

      await resolveUserFromToken(receivedToken)
      return true
    } catch (err: any) {
      error.value = err?.response?.data?.message || 'Erro ao fazer login'
      return false
    } finally {
      loading.value = false
    }
  }

  async function register(userData: RegisterDto) {
    try {
      loading.value = true
      error.value = null
      await authApi.register(userData)
      return true
    } catch (err: any) {
      error.value = err?.response?.data?.message || 'Erro ao registrar usuário'
      return false
    } finally {
      loading.value = false
    }
  }

  function logout() {
    token.value = null
    user.value = null
    localStorage.removeItem('auth_token')
  }

  async function checkAuth() {
    const storedToken = localStorage.getItem('auth_token')
    if (storedToken) {
      try {
        token.value = storedToken
        await resolveUserFromToken(storedToken)
      } catch {
        logout()
      }
    }
  }

  return {
    token,
    user,
    loading,
    error,
    isAuthenticated,
    login,
    register,
    logout,
    checkAuth
  }
}, {
  persist: {
    paths: ['token', 'user']
  }
})