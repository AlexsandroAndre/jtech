import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useTasksStore } from '../src/stores/tasks.store'
import { useAuthStore } from '../src/stores/auth.store'
import { tasksApi } from '../src/api/tasks'

vi.mock('../src/api/tasks')

describe('Tasks Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()

    const authStore = useAuthStore()
    authStore.user = {
      id: '123',
      name: 'Test User',
      email: 'test@example.com'
    }
  })

  it('should initialize with empty tasks', () => {
    const store = useTasksStore()
    expect(store.tasks).toEqual([])
    expect(store.lists).toEqual([])
  })

  it('should fetch tasks successfully', async () => {
    const store = useTasksStore()
    const mockTasks = [
      {
        id: '1',
        title: 'Task 1',
        description: 'Description 1',
        completed: false,
        listName: 'Work',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
    ]

    vi.mocked(tasksApi.getTasksByUserId).mockResolvedValue(mockTasks)

    await store.fetchTasks()

    expect(store.tasks).toEqual(mockTasks)
    expect(store.lists).toContain('Work')
  })

  it('should detect duplicate tasks', () => {
    const store = useTasksStore()
    store.tasks = [
      {
        id: '1',
        title: 'Task 1',
        description: 'Description',
        completed: false,
        listName: 'Work',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
    ]

    expect(store.isDuplicate('Task 1', 'Work')).toBe(true)
    expect(store.isDuplicate('Task 2', 'Work')).toBe(false)
    expect(store.isDuplicate('Task 1', 'Personal')).toBe(false)
  })

  it('should create task successfully', async () => {
    const store = useTasksStore()
    const newTask = {
      id: '2',
      title: 'New Task',
      description: 'New Description',
      completed: false,
      listName: 'Work',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    }

    vi.mocked(tasksApi.createTask).mockResolvedValue(newTask)

    const result = await store.createTask({
      title: 'New Task',
      description: 'New Description',
      listName: 'Work',
      completed: false
    })

    expect(result).toBe(true)
    expect(store.tasks).toContainEqual(newTask)
  })

  it('should prevent duplicate task creation', async () => {
    const store = useTasksStore()
    store.tasks = [
      {
        id: '1',
        title: 'Existing Task',
        description: 'Description',
        completed: false,
        listName: 'Work',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
    ]

    const result = await store.createTask({
      title: 'Existing Task',
      description: 'New Description',
      listName: 'Work',
      completed: false
    })

    expect(result).toBe(false)
    expect(store.error).toContain('duplicata')
  })

  it('should delete task successfully', async () => {
    const store = useTasksStore()
    store.tasks = [
      {
        id: '1',
        title: 'Task to Delete',
        description: 'Description',
        completed: false,
        listName: 'Work',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
    ]

    vi.mocked(tasksApi.deleteTask).mockResolvedValue()

    await store.deleteTask('1')

    expect(store.tasks).toHaveLength(0)
  })

  it('should get tasks by list', () => {
    const store = useTasksStore()
    store.tasks = [
      {
        id: '1',
        title: 'Work Task',
        description: 'Description',
        completed: false,
        listName: 'Work',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      },
      {
        id: '2',
        title: 'Personal Task',
        description: 'Description',
        completed: false,
        listName: 'Personal',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
    ]

    const workTasks = store.getTasksByList('Work')
    expect(workTasks).toHaveLength(1)
    expect(workTasks[0].title).toBe('Work Task')
  })

  it('should extract unique lists', () => {
    const store = useTasksStore()
    store.tasks = [
      {
        id: '1',
        title: 'Task 1',
        description: 'Description',
        completed: false,
        listName: 'Work',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      },
      {
        id: '2',
        title: 'Task 2',
        description: 'Description',
        completed: false,
        listName: 'Work',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      },
      {
        id: '3',
        title: 'Task 3',
        description: 'Description',
        completed: false,
        listName: 'Personal',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
    ]

    expect(store.lists).toEqual(['Personal', 'Work'])
  })
})
