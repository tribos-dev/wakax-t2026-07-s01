CREATE TABLE preco (
    id UUID PRIMARY KEY,
    tipo VARCHAR(20) NOT NULL,
    valor NUMERIC(15,2) NOT NULL CHECK (valor >= 0),
    produto_id UUID,
    CONSTRAINT fk_preco_produto FOREIGN KEY (produto_id) REFERENCES produto(id) ON DELETE CASCADE
); 