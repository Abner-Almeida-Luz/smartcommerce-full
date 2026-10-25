export interface ProdutoResponse {
  id: number; nome: string; preco: number; estoque: number; ativo: boolean; criadoEm: string
}
export interface ProdutoRequest { nome: string; preco: number; estoque: number }

export interface Page<T> {
  content: T[]
  page: { size: number; number: number; totalElements: number; totalPages: number }
}

export interface TokenResponse { token: string; tipo: string; expiraEmMinutos: number }
export interface LoginRequest { email: string; senha: string }
export interface ClienteRequest { nome: string; email: string; senha: string }
export interface ClienteResponse {
  id: number; nome: string; email: string; role: 'CUSTOMER' | 'ADMIN'; criadoEm: string
}

export interface CarrinhoItem {
  produtoId: number; nomeProduto: string; quantidade: number; precoUnitario: number; subtotal: number
}
export interface CarrinhoResponse { id: number; status: 'ABERTO' | 'FECHADO'; itens: CarrinhoItem[]; total: number }
export interface AdicionarItemRequest { produtoId: number; quantidade: number }

export type StatusPedido = 'AGUARDANDO_PAGAMENTO' | 'PAGO' | 'EM_SEPARACAO' | 'ENVIADO' | 'ENTREGUE' | 'CANCELADO'
export interface PedidoItem {
  produtoId: number; nomeProduto: string; quantidade: number; precoUnitario: number; subtotal: number
}
export interface PedidoResponse {
  id: number; status: StatusPedido; enderecoEntrega: string; itens: PedidoItem[]
  subtotal: number; frete: number; total: number; criadoEm: string
}
export interface CheckoutRequest { enderecoEntrega: string }
export interface MudarStatusRequest { novoStatus: StatusPedido }

export interface ProblemDetail {
  title: string; status: number; detail: string
  errors?: Array<{ field: string; message: string }>
}