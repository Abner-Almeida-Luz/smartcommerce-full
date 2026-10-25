import { apiClient } from './client'
import type { LoginRequest, TokenResponse } from './types'
export async function login(body: LoginRequest): Promise<TokenResponse> {
  const { data } = await apiClient.post('/api/v1/auth/login', body)
  return data
}