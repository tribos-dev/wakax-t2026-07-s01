CREATE TABLE item_carrinho (
    id UUID PRIMARY KEY,
    carrinho_id UUID NOT NULL,
    produto_id UUID NOT NULL,
    quantidade INTEGER NOT NULL CHECK (quantidade >= 1),
    CONSTRAINT fk_item_carrinho_carrinho FOREIGN KEY (carrinho_id) REFERENCES carrinho(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_carrinho_produto FOREIGN KEY (produto_id) REFERENCES produto(id) ON DELETE CASCADE
); 