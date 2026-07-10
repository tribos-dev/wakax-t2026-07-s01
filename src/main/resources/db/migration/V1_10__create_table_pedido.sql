CREATE TABLE pedido (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    data_pedido TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    valor_total NUMERIC(15,2) NOT NULL CHECK (valor_total >= 0),
    forma_pagamento VARCHAR(20) NOT NULL,
    endereco_entrega_id UUID NOT NULL,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE,
    CONSTRAINT fk_pedido_endereco FOREIGN KEY (endereco_entrega_id) REFERENCES endereco(id) ON DELETE CASCADE
); 