<template>
  <v-card elevation="2">
    <v-card-title class="d-flex align-center justify-space-between bg-primary">
      <span class="font-weight-bold">Listas</span>
      <v-btn
        icon
        size="small"
        variant="text"
        @click="$emit('create')"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </v-card-title>

    <v-divider />

    <v-list>
      <v-list-item
        :active="selectedList === null"
        active-color="secondary"
        @click="$emit('select', null)"
      >
        <template #prepend>
          <v-icon>mdi-view-dashboard</v-icon>
        </template>
        <v-list-item-title>Todas as Tarefas</v-list-item-title>
      </v-list-item>

      <v-divider />

      <v-list-item
        v-for="list in lists"
        :key="list"
        :active="selectedList === list"
        active-color="secondary"
        @click="$emit('select', list)"
      >
        <template #prepend>
          <v-icon>mdi-format-list-bulleted</v-icon>
        </template>
        <v-list-item-title>{{ list }}</v-list-item-title>
      </v-list-item>

      <v-list-item v-if="lists.length === 0" disabled>
        <v-list-item-title class="text-grey text-center">
          Nenhuma lista criada
        </v-list-item-title>
      </v-list-item>
    </v-list>
  </v-card>
</template>

<script setup lang="ts">
defineProps<{
  lists: string[]
  selectedList: string | null
}>()

defineEmits<{
  select: [list: string | null]
  create: []
}>()
</script>

<style scoped>
</style>
