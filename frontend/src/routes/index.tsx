import { createBrowserRouter, Navigate } from 'react-router'
import RootLayout from '@/components/shared/RootLayout'
import ErrorPage from '@/components/shared/ErrorPage'
import { ProtectedRoute } from '@/features/auth/ProtectedRoute'

export const router = createBrowserRouter([
  {
    path: '/',
    element: <RootLayout />,
    errorElement: <ErrorPage />,
    children: [
      { index: true, lazy: () => import('@/features/catalog/CatalogPage').then(m => ({ Component: m.default })) },
      { path: 'produtos/:id', lazy: () => import('@/features/catalog/ProductDetailPage').then(m => ({ Component: m.default })) },
      { path: 'login', lazy: () => import('@/features/auth/LoginPage').then(m => ({ Component: m.default })) },
      { path: 'registrar', lazy: () => import('@/features/auth/RegisterPage').then(m => ({ Component: m.default })) },
      {
        element: <ProtectedRoute />,
        children: [
          { path: 'carrinho', lazy: () => import('@/features/cart/CartPage').then(m => ({ Component: m.default })) },
          { path: 'checkout', lazy: () => import('@/features/checkout/CheckoutPage').then(m => ({ Component: m.default })) },
          { path: 'meus-pedidos', lazy: () => import('@/features/orders/OrdersPage').then(m => ({ Component: m.default })) },
        ],
      },
      {
        element: <ProtectedRoute role="ADMIN" />,
        children: [
          { path: 'admin', lazy: () => import('@/features/admin/AdminPage').then(m => ({ Component: m.default })) },
        ],
      },
      { path: '*', element: <Navigate to="/" replace /> },
    ],
  },
])