import { Link, useNavigate } from 'react-router'
import { useAuthStore } from '@/features/auth/authStore'
import { Button } from '@/components/ui/button'
import { useQuery } from '@tanstack/react-query'
import { obterCarrinho } from '@/api/carrinho'

export function Header() {
  const { user, token, logout } = useAuthStore()
  const navigate = useNavigate()
  const { data: carrinho } = useQuery({
    queryKey: ['carrinho'], queryFn: obterCarrinho, enabled: !!token,
  })
  const totalItens = carrinho?.itens.reduce((s, i) => s + i.quantidade, 0) ?? 0

  return (
    <header className="border-b bg-background">
      <div className="container mx-auto px-4 h-14 flex items-center justify-between">
        <div className="flex items-center gap-6">
          <Link to="/" className="font-bold text-lg">SmartCommerce</Link>
          {user?.role === 'ADMIN' && <Link to="/admin" className="text-sm text-muted-foreground hover:text-foreground">Admin</Link>}
        </div>
        <nav className="flex items-center gap-4">
          {token ? (
            <>
              <Link to="/carrinho" className="text-sm">
                Carrinho {totalItens > 0 && <span className="bg-primary text-primary-foreground rounded-full px-2 py-0.5 text-xs">{totalItens}</span>}
              </Link>
              <Link to="/meus-pedidos" className="text-sm">Pedidos</Link>
              <span className="text-sm text-muted-foreground">{user?.email}</span>
              <Button variant="ghost" size="sm" onClick={() => { logout(); navigate('/login') }}>Sair</Button>
            </>
          ) : (
            <Button size="sm" onClick={() => navigate('/login')}>Entrar</Button>
          )}
        </nav>
      </div>
    </header>
  )
}