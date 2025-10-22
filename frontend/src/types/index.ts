export interface User {
  id: string
  name: string
  email: string
  password?: string
}

export interface Task {
  id: string
  title: string
  description: string
  completed: boolean
  listName: string
  createdAt: string
  updatedAt: string
}

export interface CreateTaskDto {
  id?: string
  title: string
  description: string
  listName: string
  userId: string
  completed: boolean
}

export interface UpdateTaskDto {
  id: string
  title: string
  description: string
  listName: string
  userId: string
  completed: boolean
}

export interface LoginDto {
  email: string
  password: string
}

export interface RegisterDto {
  id?: string
  name: string
  email: string
  password: string
}
