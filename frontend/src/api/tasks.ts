import apiClient from './axios.config'
import type { Task, CreateTaskDto, UpdateTaskDto } from '@/types'

export const tasksApi = {
  async getTasksByUserId(userId: string): Promise<Task[]> {
    const response = await apiClient.get<Task[]>(`/api/tasks/user/${userId}`)
    return response.data
  },

  async getTaskById(id: string): Promise<Task> {
    const response = await apiClient.get<Task>(`/api/tasks/${id}`)
    return response.data
  },

  async createTask(task: CreateTaskDto): Promise<Task> {
    const response = await apiClient.post<Task>('/api/tasks', task)
    return response.data
  },

  async updateTask(task: UpdateTaskDto): Promise<Task> {
    const response = await apiClient.put<Task>('/api/tasks', task)
    return response.data
  },

  async deleteTask(id: string): Promise<void> {
    await apiClient.delete(`/api/tasks/${id}`)
  }
}
