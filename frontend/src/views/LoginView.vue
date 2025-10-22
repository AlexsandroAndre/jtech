<template>
  <v-container fluid class="fill-height login-container">
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="5" lg="4">
        <v-card elevation="8" class="pa-4">
          <v-card-title class="text-h4 text-center mb-4 font-weight-bold">
            Task Manager
          </v-card-title>

          <v-card-subtitle class="text-center mb-6">
            {{ isLoginMode ? 'Faça login para continuar' : 'Crie sua conta' }}
          </v-card-subtitle>

          <v-card-text>
            <v-form ref="formRef" v-model="valid" @submit.prevent="handleSubmit">
              <v-text-field
                v-if="!isLoginMode"
                v-model="formData.name"
                label="Nome"
                :rules="nameRules"
                prepend-inner-icon="mdi-account"
                variant="outlined"
                class="mb-2"
              />

              <v-text-field
                v-model="formData.email"
                label="Email"
                :rules="emailRules"
                prepend-inner-icon="mdi-email"
                variant="outlined"
                class="mb-2"
              />

              <v-text-field
                v-model="formData.password"
                label="Senha"
                :rules="passwordRules"
                :type="showPassword ? 'text' : 'password'"
                prepend-inner-icon="mdi-lock"
                :append-inner-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                @click:append-inner="showPassword = !showPassword"
                variant="outlined"
                class="mb-4"
              />

              <v-alert
                v-if="authStore.error"
                type="error"
                variant="tonal"
                class="mb-4"
                closable
                @click:close="authStore.error = null"
              >
                {{ authStore.error }}
              </v-alert>

              <v-btn
                type="submit"
                color="secondary"
                size="large"
                block
                :loading="authStore.loading"
                :disabled="!valid"
                class="mb-4"
              >
                {{ isLoginMode ? 'Entrar' : 'Registrar' }}
              </v-btn>

              <v-divider class="mb-4" />

              <div class="text-center">
                <v-btn
                  variant="text"
                  color="secondary"
                  @click="toggleMode"
                >
                  {{ isLoginMode ? 'Criar nova conta' : 'Já tenho uma conta' }}
                </v-btn>
              </div>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.store'

const router = useRouter()
const authStore = useAuthStore()

const formRef = ref()
const valid = ref(false)
const showPassword = ref(false)
const isLoginMode = ref(true)

const formData = reactive({
  name: '',
  email: '',
  password: ''
})

const nameRules = [
  (v: string) => !!v || 'Nome é obrigatório',
  (v: string) => v.length >= 3 || 'Nome deve ter no mínimo 3 caracteres'
]

const emailRules = [
  (v: string) => !!v || 'Email é obrigatório',
  (v: string) => /.+@.+\..+/.test(v) || 'Email deve ser válido'
]

const passwordRules = [
  (v: string) => !!v || 'Senha é obrigatória',
  (v: string) => v.length >= 6 || 'Senha deve ter no mínimo 6 caracteres'
]

function toggleMode() {
  isLoginMode.value = !isLoginMode.value
  authStore.error = null
  formRef.value?.resetValidation()
}

async function handleSubmit() {
  const { valid: isValid } = await formRef.value.validate()
  if (!isValid) return

  let success = false

  if (isLoginMode.value) {
    success = await authStore.login({
      email: formData.email,
      password: formData.password
    })
  } else {
    success = await authStore.register({
      name: formData.name,
      email: formData.email,
      password: formData.password
    })

    if (success) {
      isLoginMode.value = true
      formData.password = ''
      formRef.value?.resetValidation()
    }
  }

  if (success && isLoginMode.value) {
    router.push('/')
  }
}
</script>

<style scoped>
.login-container {
  background: linear-gradient(135deg, #3483FA 0%, #2968C8 100%);
  min-height: 100vh;
}
</style>
