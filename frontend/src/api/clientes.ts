import { apiClient } from './client'
import type { ClienteRequest, ClienteResponse } from './types'
export async function registrar(body: ClienteRequest): Promise<ClienteResponse> {
  const { data } = await apiClient.post('/api/v1/clientes', body)
  return data
}