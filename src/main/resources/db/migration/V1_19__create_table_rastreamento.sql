CREATE TABLE rastreamento (
    id UUID PRIMARY KEY,
    pedido_id UUID NOT NULL UNIQUE,
    codigo VARCHAR(60) NOT NULL,
    transportadora VARCHAR(100) NOT NULL,
    status_atual VARCHAR(30) NOT NULL,
    previsao_entrega DATE,
    CONSTRAINT fk_rastreamento_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE
);

CREATE INDEX idx_rastreamento_pedido ON rastreamento(pedido_id);
