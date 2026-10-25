import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { useNavigate, Link } from 'react-router'
import { useMutation } from '@tanstack/react-query'
import { registrar } from '@/api/clientes'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'

const schema = z.object({
  nome: z.string().min(2, 'Mínimo 2 caracteres'),
  email: z.string().email('Email inválido'),
  senha: z.string().min(8, 'Mínimo 8 caracteres').max(72),
})
type FormData = z.infer<typeof schema>

export default function RegisterPage() {
  const navigate = useNavigate()
  const form = useForm<FormData>({ resolver: zodResolver(schema), defaultValues: { nome: '', email: '', senha: '' } })
  const mutation = useMutation({
    mutationFn: registrar,
    onSuccess: () => navigate('/login'),
  })

  return (
    <div className="min-h-screen flex items-center justify-center p-4">
      <Card className="w-full max-w-sm">
        <CardHeader><CardTitle>Criar conta</CardTitle></CardHeader>
        <CardContent>
          <form onSubmit={form.handleSubmit((d) => mutation.mutate(d))} className="space-y-4">
            <Input placeholder="nome" {...form.register('nome')} />
            {form.formState.errors.nome && <p className="text-sm text-destructive">{form.formState.errors.nome.message}</p>}
            <Input placeholder="email@example.com" {...form.register('email')} />
            {form.formState.errors.email && <p className="text-sm text-destructive">{form.formState.errors.email.message}</p>}
            <Input type="password" placeholder="senha" {...form.register('senha')} />
            {form.formState.errors.senha && <p className="text-sm text-destructive">{form.formState.errors.senha.message}</p>}
            {mutation.isError && <p className="text-sm text-destructive">Erro ao registrar. Email já cadastrado?</p>}
            <Button type="submit" className="w-full" disabled={mutation.isPending}>
              {mutation.isPending ? 'Criando...' : 'Registrar'}
            </Button>
          </form>
          <p className="text-sm text-muted-foreground mt-4 text-center">
            Já tem conta? <Link to="/login" className="text-primary underline">Entrar</Link>
          </p>
        </CardContent>
      </Card>
    </div>
  )
}