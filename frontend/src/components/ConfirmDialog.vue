<template>
  <v-dialog :model-value="modelValue" max-width="400" persistent>
    <v-card>
      <v-card-title class="bg-error text-white">
        <v-icon class="mr-2">mdi-alert</v-icon>
        {{ title }}
      </v-card-title>

      <v-card-text class="pt-6">
        {{ message }}
      </v-card-text>

      <v-card-actions>
        <v-spacer />
        <v-btn @click="close">Cancelar</v-btn>
        <v-btn color="error" @click="confirm">Confirmar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
defineProps<{
  modelValue: boolean
  title: string
  message: string
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  confirm: []
}>()

function close() {
  emit('update:modelValue', false)
}

function confirm() {
  emit('confirm')
  close()
}
</script>

<style scoped>
</style>
