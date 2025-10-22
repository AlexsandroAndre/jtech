import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useAuthStore } from '../src/stores/auth.store'
import { authApi } from '../src/api/auth'
import { usersApi } from '../src/api/users'

vi.mock('../src/api/auth')
vi.mock('../src/api/users')

describe('Auth Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
    localStorage.clear()
  })

  it('should initialize with null user and token', () => {
    const store = useAuthStore()
    expect(store.user).toBeNull()
    expect(store.token).toBeNull()
    expect(store.isAuthenticated).toBe(false)
  })

  it('should login successfully', async () => {
    const store = useAuthStore()
    const mockToken = 'mock.jwt.token'
    const mockUser = {
      id: '123',
      name: 'Test User',
      email: 'test@example.com'
    }

    vi.mocked(authApi.login).mockResolvedValue(mockToken)
    vi.mocked(usersApi.getUserById).mockResolvedValue(mockUser)

    const result = await store.login({
      email: 'test@example.com',
      password: 'password123'
    })

    expect(result).toBe(true)
    expect(store.token).toBe(mockToken)
    expect(store.user).toEqual(mockUser)
    expect(store.isAuthenticated).toBe(true)
  })

  it('should handle login error', async () => {
    const store = useAuthStore()

    vi.mocked(authApi.login).mockRejectedValue(new Error('Invalid credentials'))

    const result = await store.login({
      email: 'test@example.com',
      password: 'wrong'
    })

    expect(result).toBe(false)
    expect(store.error).toBeTruthy()
    expect(store.isAuthenticated).toBe(false)
  })

  it('should register successfully', async () => {
    const store = useAuthStore()
    const mockUser = {
      id: '123',
      name: 'New User',
      email: 'new@example.com',
      password: 'password123'
    }

    vi.mocked(authApi.register).mockResolvedValue(mockUser)

    const result = await store.register({
      name: 'New User',
      email: 'new@example.com',
      password: 'password123'
    })

    expect(result).toBe(true)
    expect(store.error).toBeNull()
  })

  it('should logout correctly', () => {
    const store = useAuthStore()
    store.token = 'mock.token'
    store.user = { id: '123', name: 'Test', email: 'test@example.com' }

    store.logout()

    expect(store.token).toBeNull()
    expect(store.user).toBeNull()
    expect(store.isAuthenticated).toBe(false)
  })
})
