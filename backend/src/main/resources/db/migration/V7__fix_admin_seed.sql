-- Habilita a extensão de criptografia (já inclusa no Postgres 16)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Gera um hash BCrypt (fator 10) compatível com o Spring Security
UPDATE cliente
SET senha_hash = crypt('admin1234', gen_salt('bf', 10))
WHERE email = 'admin@example.com';