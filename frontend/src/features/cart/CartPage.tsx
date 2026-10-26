import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { Link, useNavigate } from 'react-router'
import { obterCarrinho, removerItem, limparCarrinho } from '@/api/carrinho'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { buttonVariants } from '@/components/ui/button'

export default function CartPage() {
  const navigate = useNavigate()
  const qc = useQueryClient()
  const { data, isLoading } = useQuery({ queryKey: ['carrinho'], queryFn: obterCarrinho })
  const removerMut = useMutation({
    mutationFn: removerItem,
    onSuccess: () => qc.invalidateQueries({ queryKey: ['carrinho'] }),
  })
  const limparMut = useMutation({
    mutationFn: limparCarrinho,
    onSuccess: () => qc.invalidateQueries({ queryKey: ['carrinho'] }),
  })

  if (isLoading) return <p>Carregando...</p>
  if (!data || data.itens.length === 0) return (
    <div className="text-center py-12">
      <p className="text-muted-foreground mb-4">Carrinho vazio</p>
     <Link to="/" className={buttonVariants()}>Ver produtos</Link>
    </div>
  )

  return (
    <div className="max-w-2xl">
      <h1 className="text-3xl font-bold mb-6">Carrinho</h1>
      <div className="space-y-4">
        {data.itens.map((item) => (
          <Card key={item.produtoId}>
            <CardContent className="flex items-center justify-between py-4">
              <div>
                <p className="font-medium">{item.nomeProduto}</p>
                <p className="text-sm text-muted-foreground">
                  {item.quantidade} × R$ {item.precoUnitario.toFixed(2)} = R$ {item.subtotal.toFixed(2)}
                </p>
              </div>
              <Button variant="outline" size="sm" onClick={() => removerMut.mutate(item.produtoId)}>Remover</Button>
            </CardContent>
          </Card>
        ))}
      </div>
      <Card className="mt-6">
        <CardHeader><CardTitle>Total: R$ {data.total.toFixed(2)}</CardTitle></CardHeader>
        <CardContent className="flex gap-2">
          <Button variant="outline" onClick={() => limparMut.mutate()}>Limpar</Button>
          <Button className="flex-1" onClick={() => navigate('/checkout')}>Finalizar compra</Button>
        </CardContent>
      </Card>
    </div>
  )
}