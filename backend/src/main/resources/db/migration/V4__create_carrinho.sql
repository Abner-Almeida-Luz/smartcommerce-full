CREATE TABLE carrinho (
                          id BIGSERIAL PRIMARY KEY,
                          cliente_id BIGINT NOT NULL UNIQUE REFERENCES cliente(id),
                          status VARCHAR(20) NOT NULL DEFAULT 'ABERTO',
                          criado_em TIMESTAMP NOT NULL DEFAULT now(),
                          atualizado_em TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE carrinho_item (
                               id BIGSERIAL PRIMARY KEY,
                               carrinho_id BIGINT NOT NULL REFERENCES carrinho(id) ON DELETE CASCADE,
                               produto_id BIGINT NOT NULL REFERENCES produto(id),
                               quantidade INTEGER NOT NULL CHECK (quantidade > 0),
                               preco_unitario NUMERIC(10,2) NOT NULL CHECK (preco_unitario > 0),
                               UNIQUE (carrinho_id, produto_id)
);