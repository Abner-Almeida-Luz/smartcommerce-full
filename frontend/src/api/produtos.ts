import { apiClient } from './client'
import type { ProdutoResponse, ProdutoRequest, Page } from './types'

export async function listarProdutos(page = 0, size = 20): Promise<Page<ProdutoResponse>> {
  const { data } = await apiClient.get('/api/v1/produtos', { params: { page, size } })
  return data
}
export async function buscarProduto(id: number): Promise<ProdutoResponse> {
  const { data } = await apiClient.get(`/api/v1/produtos/${id}`)
  return data
}
export async function criarProduto(body: ProdutoRequest): Promise<ProdutoResponse> {
  const { data } = await apiClient.post('/api/v1/produtos', body)
  return data
}
export async function atualizarProduto(id: number, body: ProdutoRequest): Promise<ProdutoResponse> {
  const { data } = await apiClient.put(`/api/v1/produtos/${id}`, body)
  return data
}
export async function desativarProduto(id: number): Promise<void> {
  await apiClient.delete(`/api/v1/produtos/${id}`)
}