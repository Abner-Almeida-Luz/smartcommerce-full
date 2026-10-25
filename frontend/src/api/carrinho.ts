import { apiClient } from './client'
import type { AdicionarItemRequest, CarrinhoResponse } from './types'

export async function obterCarrinho(): Promise<CarrinhoResponse> {
  const { data } = await apiClient.get('/api/v1/carrinho')
  return data
}
export async function adicionarItem(body: AdicionarItemRequest): Promise<CarrinhoResponse> {
  const { data } = await apiClient.post('/api/v1/carrinho/itens', body)
  return data
}
export async function removerItem(produtoId: number): Promise<CarrinhoResponse> {
  const { data } = await apiClient.delete(`/api/v1/carrinho/itens/${produtoId}`)
  return data
}
export async function limparCarrinho(): Promise<CarrinhoResponse> {
  const { data } = await apiClient.delete('/api/v1/carrinho')
  return data
}