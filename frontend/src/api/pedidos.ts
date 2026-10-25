import { apiClient } from './client'
import type { Page, PedidoResponse, CheckoutRequest, StatusPedido } from './types'

export async function criarPedido(body: CheckoutRequest): Promise<PedidoResponse> {
  const { data } = await apiClient.post('/api/v1/pedidos', body)
  return data
}
export async function buscarPedido(id: number): Promise<PedidoResponse> {
  const { data } = await apiClient.get(`/api/v1/pedidos/${id}`)
  return data
}
export async function meusPedidos(page = 0, size = 10): Promise<Page<PedidoResponse>> {
  const { data } = await apiClient.get('/api/v1/pedidos', { params: { page, size } })
  return data
}
export async function mudarStatus(id: number, novoStatus: StatusPedido): Promise<PedidoResponse> {
  const { data } = await apiClient.patch(`/api/v1/pedidos/${id}/status`, { novoStatus })
  return data
}