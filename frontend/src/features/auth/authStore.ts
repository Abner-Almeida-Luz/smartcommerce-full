import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type { ClienteResponse } from '@/api/types'

interface AuthState {
  token: string | null
  user: ClienteResponse | null
  setAuth: (token: string, user: ClienteResponse) => void
  logout: () => void
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      token: null,
      user: null,
      setAuth: (token, user) => set({ token, user }),
      logout: () => set({ token: null, user: null }),
    }),
    { name: 'smartcommerce-auth' }
  )
)

export const getAuthToken = () => useAuthStore.getState().token

export function parseJwt(token: string): { sub: string; role: string; exp: number } {
  const payload = token.split('.')[1]
  return JSON.parse(atob(payload.replace(/-/g, '+').replace(/_/g, '/')))
}