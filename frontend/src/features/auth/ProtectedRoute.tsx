import { Navigate, Outlet } from 'react-router'
import { useAuthStore } from './authStore'

export function ProtectedRoute({ role }: { role?: 'CUSTOMER' | 'ADMIN' }) {
  const { token, user } = useAuthStore()
  if (!token) return <Navigate to="/login" replace />
  if (role && user?.role !== role) return <Navigate to="/" replace />
  return <Outlet />
}