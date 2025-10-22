import apiClient from './axios.config'
import type { User } from '@/types'

export const usersApi = {
  async getUserById(id: string): Promise<User> {
    const response = await apiClient.get<User>(`/api/users/${id}`)
    return response.data
  },

  async getUserByEmail(email: string): Promise<User> {
    const response = await apiClient.get<User>(
      `/api/users/email/${encodeURIComponent(email)}`
    )
    return response.data
  },
}