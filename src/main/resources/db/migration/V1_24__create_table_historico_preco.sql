CREATE TABLE historico_preco (
    id UUID PRIMARY KEY,
    preco_id UUID NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    valor_de NUMERIC(15,2) NOT NULL,
    valor_para NUMERIC(15,2) NOT NULL,
    data_evento TIMESTAMP NOT NULL,
    motivo VARCHAR(255) NOT NULL,
    usuario VARCHAR(255) NOT NULL,
    CONSTRAINT fk_historico_preco FOREIGN KEY (preco_id) REFERENCES preco(id)
);

CREATE INDEX idx_historico_preco ON historico_preco(preco_id);
CREATE INDEX idx_historico_preco_data ON historico_preco(data_evento);