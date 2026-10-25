import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { meusPedidos, mudarStatus } from '@/api/pedidos'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import type { PedidoResponse } from '@/api/types'

const statusLabel: Record<string, string> = {
  AGUARDANDO_PAGAMENTO: 'Aguardando pagamento',
  PAGO: 'Pago', EM_SEPARACAO: 'Em separação',
  ENVIADO: 'Enviado', ENTREGUE: 'Entregue', CANCELADO: 'Cancelado',
}

export default function OrdersPage() {
  const qc = useQueryClient()
  const { data, isLoading } = useQuery({ queryKey: ['pedidos'], queryFn: () => meusPedidos(0, 20) })
  const cancelarMut = useMutation({
    mutationFn: (id: number) => mudarStatus(id, 'CANCELADO'),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['pedidos'] }),
  })
  const pagarMut = useMutation({
    mutationFn: (id: number) => mudarStatus(id, 'PAGO'),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['pedidos'] }),
  })

  if (isLoading) return <p>Carregando...</p>
  if (!data || data.content.length === 0) return <p className="text-muted-foreground">Você ainda não fez nenhum pedido.</p>

  const podeCancelar = (p: PedidoResponse) => ['AGUARDANDO_PAGAMENTO', 'PAGO'].includes(p.status)
  const podePagar = (p: PedidoResponse) => p.status === 'AGUARDANDO_PAGAMENTO'

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">Meus pedidos</h1>
      <div className="space-y-4">
        {data.content.map((p) => (
          <Card key={p.id}>
            <CardHeader className="flex flex-row justify-between items-center">
              <CardTitle className="text-lg">Pedido #{p.id}</CardTitle>
              <Badge>{statusLabel[p.status]}</Badge>
            </CardHeader>
            <CardContent>
              <p className="text-sm text-muted-foreground mb-2">{p.enderecoEntrega}</p>
              <ul className="text-sm space-y-1 mb-3">
                {p.itens.map((i) => <li key={i.produtoId}>{i.quantidade}× {i.nomeProduto} — R$ {i.subtotal.toFixed(2)}</li>)}
              </ul>
              <p className="text-sm">Subtotal: R$ {p.subtotal.toFixed(2)} · Frete: R$ {p.frete.toFixed(2)} · <strong>Total: R$ {p.total.toFixed(2)}</strong></p>
              <div className="flex gap-2 mt-4">
                {podePagar(p) && <Button size="sm" onClick={() => pagarMut.mutate(p.id)}>Pagar (mock)</Button>}
                {podeCancelar(p) && <Button size="sm" variant="outline" onClick={() => cancelarMut.mutate(p.id)}>Cancelar</Button>}
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  )
}