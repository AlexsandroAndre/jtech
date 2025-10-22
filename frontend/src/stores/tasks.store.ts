import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { tasksApi } from '@/api/tasks'
import type { Task, CreateTaskDto, UpdateTaskDto } from '@/types'
import { useAuthStore } from './auth.store'

export const useTasksStore = defineStore('tasks', () => {
  const tasks = ref<Task[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  const lists = computed(() => {
    const uniqueLists = new Set(tasks.value.map(task => task.listName))
    return Array.from(uniqueLists).sort()
  })

  function looksLikeUuid(value: string): boolean {
  return typeof value === 'string' && value.length === 36 && value.includes('-')
}

  function getTasksByList(listName: string) {
    return tasks.value.filter(task => task.listName === listName)
  }

  function isDuplicate(title: string, listName: string, excludeId?: string): boolean {
    return tasks.value.some(task => 
      task.title.toLowerCase() === title.toLowerCase() && 
      task.listName === listName &&
      task.id !== excludeId
    )
  }

  async function fetchTasks() {
  const authStore = useAuthStore()
  if (!authStore.user?.id) return

  try {
    loading.value = true
    error.value = null

    // Só buscar se o id parecer UUID. Se for e-mail, aguarde até obter o UUID.
    if (looksLikeUuid(authStore.user.id)) {
      tasks.value = await tasksApi.getTasksByUserId(authStore.user.id)
    } else {
      // Evita chamar com e-mail (que causa 400/404/500)
      tasks.value = []
      error.value = 'Não foi possível carregar as tarefas: usuário sem UUID. Faça login com um token que contenha o userId ou configure a resolução de email→id.'
    }
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Erro ao carregar tarefas'
  } finally {
    loading.value = false
  }
}

  async function createTask(taskData: Omit<CreateTaskDto, 'userId'>) {
    const authStore = useAuthStore()
    if (!authStore.user?.id) return false

    if (isDuplicate(taskData.title, taskData.listName)) {
      error.value = 'Já existe uma tarefa com este título nesta lista'
      return false
    }

    try {
      loading.value = true
      error.value = null

      const newTask = await tasksApi.createTask({
        ...taskData,
        userId: authStore.user.id
      })

      tasks.value.push(newTask)
      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Erro ao criar tarefa'
      return false
    } finally {
      loading.value = false
    }
  }

  async function updateTask(taskData: Omit<UpdateTaskDto, 'userId'>) {
    const authStore = useAuthStore()
    if (!authStore.user?.id) return false

    if (isDuplicate(taskData.title, taskData.listName, taskData.id)) {
      error.value = 'Já existe uma tarefa com este título nesta lista'
      return false
    }

    try {
      loading.value = true
      error.value = null

      const updatedTask = await tasksApi.updateTask({
        ...taskData,
        userId: authStore.user.id
      })

      const index = tasks.value.findIndex(t => t.id === updatedTask.id)
      if (index !== -1) {
        tasks.value[index] = updatedTask
      }

      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Erro ao atualizar tarefa'
      return false
    } finally {
      loading.value = false
    }
  }

  async function deleteTask(id: string) {
    try {
      loading.value = true
      error.value = null

      await tasksApi.deleteTask(id)
      tasks.value = tasks.value.filter(t => t.id !== id)

      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Erro ao deletar tarefa'
      return false
    } finally {
      loading.value = false
    }
  }

  async function toggleTaskComplete(task: Task) {
    return await updateTask({
      id: task.id,
      title: task.title,
      description: task.description,
      listName: task.listName,
      completed: !task.completed
    })
  }

  return {
    tasks,
    lists,
    loading,
    error,
    getTasksByList,
    isDuplicate,
    fetchTasks,
    createTask,
    updateTask,
    deleteTask,
    toggleTaskComplete
  }
})
