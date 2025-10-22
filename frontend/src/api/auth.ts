import apiClient from './axios.config'
import type { LoginDto, RegisterDto, User } from '@/types'

export const authApi = {
  async login(credentials: LoginDto): Promise<string> {
    const response = await apiClient.post<string>(
      '/auth/login',
      null,
      {
        params: {
          email: credentials.email,
          password: credentials.password,
        },
        headers: {
          Accept: '*/*',
        },
      }
    )
    return response.data
  },

  async register(userData: RegisterDto): Promise<User> {
    const response = await apiClient.post<User>('/api/users', userData)
    return response.data
  }
}