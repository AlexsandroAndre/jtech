<template>
  <v-dialog :model-value="modelValue" max-width="600" persistent>
    <v-card>
      <v-card-title class="d-flex align-center bg-secondary">
        <v-icon class="mr-2">{{ task ? 'mdi-pencil' : 'mdi-plus' }}</v-icon>
        {{ task ? 'Editar Tarefa' : 'Nova Tarefa' }}
      </v-card-title>

      <v-card-text class="pt-6">
        <v-form ref="formRef" v-model="valid">
          <v-text-field
            v-model="formData.title"
            label="Título"
            :rules="titleRules"
            variant="outlined"
            prepend-inner-icon="mdi-format-title"
            class="mb-3"
          />

          <v-textarea
            v-model="formData.description"
            label="Descrição"
            :rules="descriptionRules"
            variant="outlined"
            prepend-inner-icon="mdi-text"
            rows="3"
            class="mb-3"
          />

          <v-combobox
            v-model="formData.listName"
            :items="lists"
            label="Lista"
            :rules="listRules"
            variant="outlined"
            prepend-inner-icon="mdi-format-list-bulleted"
            class="mb-3"
          />

          <v-checkbox
            v-model="formData.completed"
            label="Tarefa concluída"
            color="secondary"
            hide-details
          />
        </v-form>
      </v-card-text>

      <v-card-actions>
        <v-spacer />
        <v-btn @click="close">Cancelar</v-btn>
        <v-btn
          color="secondary"
          :disabled="!valid"
          @click="save"
        >
          {{ task ? 'Salvar' : 'Criar' }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, watch, reactive } from 'vue'
import type { Task } from '@/types'

const props = defineProps<{
  modelValue: boolean
  task: Task | null
  lists: string[]
  defaultList: string | null
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  save: [data: any]
}>()

const formRef = ref()
const valid = ref(false)

const formData = reactive({
  title: '',
  description: '',
  listName: '',
  completed: false
})

const titleRules = [
  (v: string) => !!v || 'Título é obrigatório',
  (v: string) => v.length >= 3 || 'Título deve ter no mínimo 3 caracteres'
]

const descriptionRules = [
  (v: string) => !!v || 'Descrição é obrigatória',
  (v: string) => v.length >= 5 || 'Descrição deve ter no mínimo 5 caracteres'
]

const listRules = [
  (v: string) => !!v || 'Lista é obrigatória'
]

watch(() => props.modelValue, (newVal) => {
  if (newVal) {
    if (props.task) {
      formData.title = props.task.title
      formData.description = props.task.description
      formData.listName = props.task.listName
      formData.completed = props.task.completed
    } else {
      formData.title = ''
      formData.description = ''
      formData.listName = props.defaultList || ''
      formData.completed = false
    }
    formRef.value?.resetValidation()
  }
})

function close() {
  emit('update:modelValue', false)
}

async function save() {
  const { valid: isValid } = await formRef.value.validate()
  if (!isValid) return

  emit('save', {
    title: formData.title,
    description: formData.description,
    listName: formData.listName,
    completed: formData.completed
  })
}
</script>

<style scoped>
</style>
