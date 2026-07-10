CREATE TABLE carrinho (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    CONSTRAINT fk_carrinho_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE
); 