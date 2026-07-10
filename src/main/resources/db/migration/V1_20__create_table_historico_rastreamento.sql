CREATE TABLE historico_rastreamento (
    id UUID PRIMARY KEY,
    rastreamento_id UUID NOT NULL,
    data_evento TIMESTAMP NOT NULL,
    status VARCHAR(30) NOT NULL,
    local VARCHAR(120) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    CONSTRAINT fk_historico_rastreamento FOREIGN KEY (rastreamento_id) REFERENCES rastreamento(id) ON DELETE CASCADE
);

CREATE INDEX idx_historico_rastreamento ON historico_rastreamento(rastreamento_id);
CREATE INDEX idx_historico_data ON historico_rastreamento(data_evento);
