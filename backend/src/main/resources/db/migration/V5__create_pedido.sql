CREATE TABLE pedido (
                        id BIGSERIAL PRIMARY KEY,
                        cliente_id BIGINT NOT NULL REFERENCES cliente(id),
                        status VARCHAR(30) NOT NULL DEFAULT 'AGUARDANDO_PAGAMENTO',
                        subtotal NUMERIC(10,2) NOT NULL CHECK (subtotal >= 0),
                        frete NUMERIC(10,2) NOT NULL CHECK (frete >= 0),
                        total NUMERIC(10,2) NOT NULL CHECK (total >= 0),
                        endereco_entrega VARCHAR(255) NOT NULL,
                        criado_em TIMESTAMP NOT NULL DEFAULT now(),
                        atualizado_em TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE pedido_item (
                             id BIGSERIAL PRIMARY KEY,
                             pedido_id BIGINT NOT NULL REFERENCES pedido(id) ON DELETE CASCADE,
                             produto_id BIGINT NOT NULL REFERENCES produto(id),
                             nome_produto VARCHAR(100) NOT NULL,
                             quantidade INTEGER NOT NULL CHECK (quantidade > 0),
                             preco_unitario NUMERIC(10,2) NOT NULL CHECK (preco_unitario > 0)
);

CREATE TABLE estoque_movimento (
                                   id BIGSERIAL PRIMARY KEY,
                                   produto_id BIGINT NOT NULL REFERENCES produto(id),
                                   tipo VARCHAR(20) NOT NULL,
                                   quantidade INTEGER NOT NULL,
                                   motivo VARCHAR(255),
                                   criado_em TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_pedido_cliente ON pedido(cliente_id);
CREATE INDEX idx_pedido_status ON pedido(status);