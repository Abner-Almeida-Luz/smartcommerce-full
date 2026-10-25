import { useQuery } from '@tanstack/react-query'
import { useParams } from 'react-router'
import { buscarProduto } from '@/api/produtos'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'

export default function ProductDetailPage() {
  const { id } = useParams()
  const { data, isLoading } = useQuery({
    queryKey: ['produto', id],
    queryFn: () => buscarProduto(Number(id)),
    enabled: !!id,
  })
  if (isLoading) return <p>Carregando...</p>
  if (!data) return <p>Produto não encontrado.</p>
  return (
    <Card className="max-w-xl">
      <CardHeader><CardTitle>{data.nome}</CardTitle></CardHeader>
      <CardContent className="space-y-2">
        <p className="text-3xl font-bold">R$ {data.preco.toFixed(2)}</p>
        <p className="text-sm text-muted-foreground">Estoque: {data.estoque}</p>
        <p className="text-xs text-muted-foreground">Cadastrado em {new Date(data.criadoEm).toLocaleString()}</p>
      </CardContent>
    </Card>
  )
}