import { Outlet } from 'react-router'
import { Header } from './Header'

export default function RootLayout() {
  return (
    <div className="min-h-screen bg-background text-foreground">
      <Header />
      <main className="container mx-auto px-4 py-8"><Outlet /></main>
    </div>
  )
}