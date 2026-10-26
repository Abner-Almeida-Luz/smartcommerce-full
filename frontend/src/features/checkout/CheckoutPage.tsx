import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { useNavigate } from 'react-router'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { criarPedido } from '@/api/pedidos'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'

const schema = z.object({ enderecoEntrega: z.string().min(5, 'Endereço muito curto') })
type FormData = z.infer<typeof schema>

export default function CheckoutPage() {
  const navigate = useNavigate()
  const qc = useQueryClient()
  const form = useForm<FormData>({ resolver: zodResolver(schema), defaultValues: { enderecoEntrega: '' } })
  const mutation = useMutation({
    mutationFn: criarPedido,
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['carrinho'] })
      qc.invalidateQueries({ queryKey: ['pedidos'] })
      navigate(`/meus-pedidos`)
    },
  })

  return (
    <div className="max-w-xl">
      <h1 className="text-3xl font-bold mb-6">Finalizar compra</h1>
      <Card>
        <CardHeader><CardTitle>Endereço de entrega</CardTitle></CardHeader>
        <CardContent>
          <form onSubmit={form.handleSubmit((d) => mutation.mutate(d))} className="space-y-4">
            <Input placeholder="Rua, número, bairro, cidade" {...form.register('enderecoEntrega')} />
            {form.formState.errors.enderecoEntrega && <p className="text-sm text-destructive">{form.formState.errors.enderecoEntrega.message}</p>}
            {mutation.isError && <p className="text-sm text-destructive">Erro ao criar pedido. Verifique o carrinho.</p>}
            <Button type="submit" className="w-full" disabled={mutation.isPending}>
              {mutation.isPending ? 'Processando...' : 'Confirmar pedido'}
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}