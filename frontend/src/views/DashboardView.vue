<template>
  <v-app>
    <v-app-bar color="secondary" elevation="2">
      <v-app-bar-title class="font-weight-bold">
        <v-icon class="mr-2">mdi-checkbox-marked-circle-outline</v-icon>
        Task Manager
      </v-app-bar-title>

      <v-spacer />

      <v-chip class="mr-4" color="primary" variant="flat">
        <v-icon start>mdi-account</v-icon>
        {{ authStore.user?.name }}
      </v-chip>

      <v-btn icon @click="handleLogout">
        <v-icon>mdi-logout</v-icon>
      </v-btn>
    </v-app-bar>

    <v-main>
      <v-container fluid class="pa-6">
        <v-row>
          <v-col cols="12" md="3">
            <ListSelector
              :lists="tasksStore.lists"
              :selected-list="selectedList"
              @select="selectList"
              @create="showCreateListDialog = true"
            />
          </v-col>

          <v-col cols="12" md="9">
            <v-card elevation="2">
              <v-card-title class="d-flex align-center justify-space-between">
                <div class="d-flex align-center">
                  <v-icon class="mr-2" color="secondary">mdi-format-list-bulleted</v-icon>
                  <span class="text-h5">{{ selectedList || 'Todas as Tarefas' }}</span>
                  <v-chip class="ml-3" color="primary" size="small">
                    {{ filteredTasks.length }}
                  </v-chip>
                </div>

                <v-btn
                  color="secondary"
                  prepend-icon="mdi-plus"
                  @click="openCreateDialog"
                >
                  Nova Tarefa
                </v-btn>
              </v-card-title>

              <v-divider />

              <v-card-text>
                <v-alert
                  v-if="tasksStore.error"
                  type="error"
                  variant="tonal"
                  closable
                  class="mb-4"
                  @click:close="tasksStore.error = null"
                >
                  {{ tasksStore.error }}
                </v-alert>

                <v-progress-linear
                  v-if="tasksStore.loading"
                  indeterminate
                  color="secondary"
                  class="mb-4"
                />

                <div v-if="filteredTasks.length === 0 && !tasksStore.loading" class="text-center py-8">
                  <v-icon size="64" color="grey-lighten-1">mdi-clipboard-text-outline</v-icon>
                  <p class="text-h6 text-grey mt-4">Nenhuma tarefa encontrada</p>
                  <p class="text-grey">Crie sua primeira tarefa para começar</p>
                </div>

                <v-row v-else>
                  <v-col
                    v-for="task in filteredTasks"
                    :key="task.id"
                    cols="12"
                  >
                    <TaskCard
                      :task="task"
                      @edit="openEditDialog"
                      @delete="openDeleteDialog"
                      @toggle-complete="toggleComplete"
                    />
                  </v-col>
                </v-row>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>

    <TaskDialog
      v-model="showTaskDialog"
      :task="selectedTask"
      :lists="tasksStore.lists"
      :default-list="selectedList"
      @save="handleSaveTask"
    />

    <v-dialog v-model="showCreateListDialog" max-width="500">
      <v-card>
        <v-card-title>Nova Lista</v-card-title>
        <v-card-text>
          <v-text-field
            v-model="newListName"
            label="Nome da Lista"
            variant="outlined"
            autofocus
            @keyup.enter="createList"
          />
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn @click="showCreateListDialog = false">Cancelar</v-btn>
          <v-btn color="secondary" @click="createList">Criar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <ConfirmDialog
      v-model="showDeleteDialog"
      title="Excluir Tarefa"
      :message="`Tem certeza que deseja excluir a tarefa '${taskToDelete?.title}'?`"
      @confirm="confirmDelete"
    />
  </v-app>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.store'
import { useTasksStore } from '@/stores/tasks.store'
import TaskCard from '@/components/TaskCard.vue'
import TaskDialog from '@/components/TaskDialog.vue'
import ListSelector from '@/components/ListSelector.vue'
import ConfirmDialog from '@/components/ConfirmDialog.vue'
import type { Task } from '@/types'

const router = useRouter()
const authStore = useAuthStore()
const tasksStore = useTasksStore()

const selectedList = ref<string | null>(null)
const showTaskDialog = ref(false)
const showCreateListDialog = ref(false)
const showDeleteDialog = ref(false)
const selectedTask = ref<Task | null>(null)
const taskToDelete = ref<Task | null>(null)
const newListName = ref('')

const filteredTasks = computed(() => {
  if (!selectedList.value) {
    return tasksStore.tasks
  }
  return tasksStore.getTasksByList(selectedList.value)
})

onMounted(async () => {
  await tasksStore.fetchTasks()
})

function selectList(listName: string | null) {
  selectedList.value = listName
}

function openCreateDialog() {
  selectedTask.value = null
  showTaskDialog.value = true
}

function openEditDialog(task: Task) {
  selectedTask.value = task
  showTaskDialog.value = true
}

function openDeleteDialog(task: Task) {
  taskToDelete.value = task
  showDeleteDialog.value = true
}

async function handleSaveTask(taskData: any) {
  let success = false

  if (selectedTask.value) {
    success = await tasksStore.updateTask({
      id: selectedTask.value.id,
      ...taskData
    })
  } else {
    success = await tasksStore.createTask(taskData)
  }

  if (success) {
    showTaskDialog.value = false
  }
}

async function confirmDelete() {
  if (taskToDelete.value) {
    await tasksStore.deleteTask(taskToDelete.value.id)
    taskToDelete.value = null
  }
}

async function toggleComplete(task: Task) {
  await tasksStore.toggleTaskComplete(task)
}

function createList() {
  if (newListName.value.trim()) {
    selectedList.value = newListName.value.trim()
    newListName.value = ''
    showCreateListDialog.value = false
    openCreateDialog()
  }
}

function handleLogout() {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
</style>
