CREATE TABLE estoque (
    id UUID PRIMARY KEY,
    produto_id UUID NOT NULL,
    quantidade_disponivel INTEGER NOT NULL CHECK (quantidade_disponivel >= 0),
    custo_medio NUMERIC(15,2) NOT NULL CHECK (custo_medio >= 0),
    custo_total NUMERIC(15,2) NOT NULL CHECK (custo_total >= 0),
    valor_venda NUMERIC(15,2) NOT NULL CHECK (valor_venda >= 0),
    CONSTRAINT fk_estoque_produto FOREIGN KEY (produto_id) REFERENCES produto(id) ON DELETE CASCADE
); 