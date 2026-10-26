import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { listarProdutos, criarProduto, desativarProduto } from '@/api/produtos'
import { mudarStatus } from '@/api/pedidos'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import type { StatusPedido } from '@/api/types'
import type { Page, PedidoResponse } from '@/api/types'
import { apiClient } from '@/api/client'


export default function AdminPage() {
  const qc = useQueryClient()
  const [nome, setNome] = useState('')
  const [preco, setPreco] = useState('')
  const [estoque, setEstoque] = useState('')

  const { data: produtos } = useQuery({ queryKey: ['produtos', 'admin'], queryFn: () => listarProdutos(0, 50) })

  const criar = useMutation({
    mutationFn: () => criarProduto({ nome, preco: Number(preco), estoque: Number(estoque) }),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['produtos'] })
      setNome(''); setPreco(''); setEstoque('')
    },
  })
  const desativar = useMutation({
    mutationFn: desativarProduto,
    onSuccess: () => qc.invalidateQueries({ queryKey: ['produtos'] }),
  })
  const mudarStatusMut = useMutation({
    mutationFn: ({ id, status }: { id: number; status: StatusPedido }) => mudarStatus(id, status),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['admin', 'pedidos'] }),
  })

const { data: pedidos } = useQuery<Page<PedidoResponse>>({
  queryKey: ['admin', 'pedidos'],
  queryFn: async () => {
    const { data } = await apiClient.get('/api/v1/pedidos/admin', { params: { page: 0, size: 50 } })
    return data
  },
})


    
  return (
    <div className="space-y-8">
      <h1 className="text-3xl font-bold">Admin</h1>

      <Card>
        <CardHeader><CardTitle>Novo produto</CardTitle></CardHeader>
        <CardContent>
          <div className="flex gap-2 flex-wrap">
            <Input placeholder="nome" value={nome} onChange={(e) => setNome(e.target.value)} className="flex-1 min-w-48" />
            <Input placeholder="preço" type="number" step="0.01" value={preco} onChange={(e) => setPreco(e.target.value)} className="w-32" />
            <Input placeholder="estoque" type="number" value={estoque} onChange={(e) => setEstoque(e.target.value)} className="w-32" />
            <Button onClick={() => criar.mutate()} disabled={!nome || !preco || !estoque || criar.isPending}>Criar</Button>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader><CardTitle>Produtos</CardTitle></CardHeader>
        <CardContent>
          <ul className="divide-y">
            {produtos?.content.map((p) => (
              <li key={p.id} className="py-2 flex justify-between items-center">
                <span>{p.nome} — R$ {p.preco.toFixed(2)} (estoque {p.estoque})</span>
                <Button size="sm" variant="outline" onClick={() => desativar.mutate(p.id)}>Desativar</Button>
              </li>
            ))}
          </ul>
        </CardContent>
      </Card>

      <Card>
        <CardHeader><CardTitle>Pedidos recentes</CardTitle></CardHeader>
        <CardContent>
          <ul className="divide-y">
            {pedidos?.content.map((p) => (
              <li key={p.id} className="py-3 flex justify-between items-center gap-2">
                <span>#{p.id} — {p.enderecoEntrega} — R$ {p.total.toFixed(2)}</span>
                <Badge>{p.status}</Badge>
                <div className="flex gap-1">
                  <Button size="sm" variant="outline" onClick={() => mudarStatusMut.mutate({ id: p.id, status: 'EM_SEPARACAO' })}>Separar</Button>
                  <Button size="sm" variant="outline" onClick={() => mudarStatusMut.mutate({ id: p.id, status: 'ENVIADO' })}>Enviar</Button>
                  <Button size="sm" variant="outline" onClick={() => mudarStatusMut.mutate({ id: p.id, status: 'ENTREGUE' })}>Entregar</Button>
                </div>
              </li>
            ))}
          </ul>
        </CardContent>
      </Card>
    </div>
  )
}