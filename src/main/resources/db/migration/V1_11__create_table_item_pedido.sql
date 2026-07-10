CREATE TABLE item_pedido (
    id UUID PRIMARY KEY,
    pedido_id UUID NOT NULL,
    produto_id UUID NOT NULL,
    quantidade INTEGER NOT NULL CHECK (quantidade >= 1),
    valor_unitario NUMERIC(15,2) NOT NULL CHECK (valor_unitario >= 0),
    CONSTRAINT fk_item_pedido_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_pedido_produto FOREIGN KEY (produto_id) REFERENCES produto(id) ON DELETE CASCADE
); 