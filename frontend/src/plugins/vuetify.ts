import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import '@mdi/font/css/materialdesignicons.css'

const mercadoLivreTheme = {
  dark: false,
  colors: {
    primary: '#FFE600',
    secondary: '#3483FA',
    'secondary-darken-1': '#2968C8',
    accent: '#FFE600',
    error: '#FF5252',
    info: '#3483FA',
    success: '#4CAF50',
    warning: '#FFC107',
    background: '#FFFFFF',
    surface: '#FFFFFF',
    'on-primary': '#000000',
    'on-secondary': '#FFFFFF',
  }
}

export default createVuetify({
  components,
  directives,
  theme: {
    defaultTheme: 'mercadoLivreTheme',
    themes: {
      mercadoLivreTheme
    }
  },
  icons: {
    defaultSet: 'mdi'
  }
})
