import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { useNavigate, Link } from 'react-router'
import { useMutation } from '@tanstack/react-query'
import { login } from '@/api/auth'
import { useAuthStore, parseJwt } from './authStore'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'

const schema = z.object({
  email: z.string().email('Email inválido'),
  senha: z.string().min(8, 'Mínimo 8 caracteres'),
})
type FormData = z.infer<typeof schema>

export default function LoginPage() {
  const navigate = useNavigate()
  const setAuth = useAuthStore((s) => s.setAuth)

  const form = useForm<FormData>({
    resolver: zodResolver(schema),
    defaultValues: { email: '', senha: '' },
  })

  const mutation = useMutation({
    mutationFn: login,
    onSuccess: (data) => {
      const payload = parseJwt(data.token)
      setAuth(data.token, {
        id: 0, nome: payload.sub, email: payload.sub,
        role: payload.role as 'CUSTOMER' | 'ADMIN', criadoEm: '',
      })
      navigate('/')
    },
  })

  return (
    <div className="min-h-screen flex items-center justify-center p-4">
      <Card className="w-full max-w-sm">
        <CardHeader><CardTitle>Entrar</CardTitle></CardHeader>
        <CardContent>
          <form onSubmit={form.handleSubmit((d) => mutation.mutate(d))} className="space-y-4">
            <Input placeholder="email@example.com" {...form.register('email')} />
            {form.formState.errors.email && <p className="text-sm text-destructive">{form.formState.errors.email.message}</p>}
            <Input type="password" placeholder="senha" {...form.register('senha')} />
            {form.formState.errors.senha && <p className="text-sm text-destructive">{form.formState.errors.senha.message}</p>}
            {mutation.isError && <p className="text-sm text-destructive">Credenciais inválidas</p>}
            <Button type="submit" className="w-full" disabled={mutation.isPending}>
              {mutation.isPending ? 'Entrando...' : 'Entrar'}
            </Button>
          </form>
          <p className="text-sm text-muted-foreground mt-4 text-center">
            Sem conta? <Link to="/registrar" className="text-primary underline">Registrar</Link>
          </p>
        </CardContent>
      </Card>
    </div>
  )
}