import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '/auth': 'http://localhost:8080',
      '/news': 'http://localhost:8080',
      '/api': 'http://localhost:8080',
      '/excel': 'http://localhost:8080',
      '/health': 'http://localhost:8080',
      '/favorites': 'http://localhost:8080'
    }
  }
})
