import axios from 'axios'
import { useAuthStore } from '@/features/auth/authStore'

const BASE_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

export const apiClient = axios.create({ baseURL: BASE_URL })

apiClient.interceptors.request.use((config) => {
  const token = useAuthStore.getState().token
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

apiClient.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      useAuthStore.getState().logout()
      if (!window.location.pathname.startsWith('/login')) window.location.href = '/login'
    }
    return Promise.reject(err)
  }
)