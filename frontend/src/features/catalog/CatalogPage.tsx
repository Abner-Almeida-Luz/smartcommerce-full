import { useQuery } from '@tanstack/react-query'
import { useSearchParams } from 'react-router'
import { listarProdutos } from '@/api/produtos'
import { ProductCard } from './ProductCard'
import { Button } from '@/components/ui/button'

export default function CatalogPage() {
  const [params, setParams] = useSearchParams()
  const page = Number(params.get('page') ?? 0)
  const { data, isLoading, isError } = useQuery({
    queryKey: ['produtos', page],
    queryFn: () => listarProdutos(page, 12),
  })

  if (isLoading) return <p>Carregando...</p>
  if (isError) return <p className="text-destructive">Erro ao carregar produtos.</p>
  if (!data || data.content.length === 0) return <p className="text-muted-foreground">Nenhum produto cadastrado.</p>

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">Produtos</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
        {data.content.map((p) => <ProductCard key={p.id} produto={p} />)}
      </div>
      <div className="flex items-center justify-center gap-2 mt-6">
        <Button variant="outline" disabled={page === 0} onClick={() => setParams({ page: String(page - 1) })}>Anterior</Button>
        <span className="text-sm text-muted-foreground">Página {page + 1} de {data.page.totalPages}</span>
        <Button variant="outline" disabled={page + 1 >= data.page.totalPages} onClick={() => setParams({ page: String(page + 1) })}>Próxima</Button>
      </div>
    </div>
  )
}