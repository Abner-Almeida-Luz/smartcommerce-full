import { Link } from 'react-router'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { adicionarItem } from '@/api/carrinho'
import { useAuthStore } from '@/features/auth/authStore'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@/components/ui/card'
import type { ProdutoResponse } from '@/api/types'

export function ProductCard({ produto }: { produto: ProdutoResponse }) {
  const token = useAuthStore((s) => s.token)
  const qc = useQueryClient()
  const mutation = useMutation({
    mutationFn: () => adicionarItem({ produtoId: produto.id, quantidade: 1 }),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['carrinho'] }),
  })

  return (
    <Card>
      <CardHeader><CardTitle className="text-lg">{produto.nome}</CardTitle></CardHeader>
      <CardContent>
        <p className="text-2xl font-bold">R$ {produto.preco.toFixed(2)}</p>
        <p className="text-sm text-muted-foreground">Estoque: {produto.estoque}</p>
      </CardContent>
      <CardFooter className="gap-2">
        <Button asChild variant="outline" size="sm"><Link to={`/produtos/${produto.id}`}>Detalhes</Link></Button>
        <Button size="sm" disabled={!token || produto.estoque === 0 || mutation.isPending} onClick={() => mutation.mutate()}>
          {!token ? 'Entre para comprar' : produto.estoque === 0 ? 'Sem estoque' : 'Adicionar'}
        </Button>
      </CardFooter>
    </Card>
  )
}