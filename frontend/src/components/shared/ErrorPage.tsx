import { isRouteErrorResponse, useRouteError } from 'react-router'

export default function ErrorPage() {
  const err = useRouteError()
  const msg = isRouteErrorResponse(err) ? `${err.status} ${err.statusText}` : 'Algo deu errado'
  return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="text-center">
        <h1 className="text-3xl font-bold mb-2">Erro</h1>
        <p className="text-muted-foreground">{msg}</p>
      </div>
    </div>
  )
}