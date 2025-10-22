<template>
  <v-card
    :class="['task-card', { 'task-completed': task.completed }]"
    elevation="1"
    hover
  >
    <v-card-text class="d-flex align-start">
      <v-checkbox
        :model-value="task.completed"
        color="secondary"
        hide-details
        class="mt-0 pt-0 mr-3"
        @update:model-value="$emit('toggle-complete', task)"
      />

      <div class="flex-grow-1">
        <div class="d-flex align-center mb-1">
          <h3 :class="['task-title', { 'text-decoration-line-through': task.completed }]">
            {{ task.title }}
          </h3>
          <v-chip size="x-small" color="primary" variant="outlined" class="ml-2">
            {{ task.listName }}
          </v-chip>
        </div>

        <p :class="['task-description', { 'text-decoration-line-through': task.completed }]">
          {{ task.description }}
        </p>

        <div class="d-flex align-center text-caption text-grey mt-2">
          <v-icon size="small" class="mr-1">mdi-clock-outline</v-icon>
          {{ formatDate(task.createdAt) }}
        </div>
      </div>

      <div class="ml-3">
        <v-btn
          icon
          size="small"
          variant="text"
          color="secondary"
          @click="$emit('edit', task)"
        >
          <v-icon>mdi-pencil</v-icon>
        </v-btn>

        <v-btn
          icon
          size="small"
          variant="text"
          color="error"
          @click="$emit('delete', task)"
        >
          <v-icon>mdi-delete</v-icon>
        </v-btn>
      </div>
    </v-card-text>
  </v-card>
</template>

<script setup lang="ts">
import type { Task } from '@/types'

defineProps<{
  task: Task
}>()

defineEmits<{
  edit: [task: Task]
  delete: [task: Task]
  'toggle-complete': [task: Task]
}>()

function formatDate(dateString: string): string {
  const date = new Date(dateString)
  return date.toLocaleDateString('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}
</script>

<style scoped>
.task-card {
  transition: all 0.3s ease;
  border-left: 4px solid #3483FA;
}

.task-card:hover {
  border-left-color: #FFE600;
}

.task-completed {
  opacity: 0.7;
  background-color: #f5f5f5;
}

.task-title {
  font-size: 1.1rem;
  font-weight: 500;
  color: #333;
}

.task-description {
  color: #666;
  margin: 0;
}
</style>
