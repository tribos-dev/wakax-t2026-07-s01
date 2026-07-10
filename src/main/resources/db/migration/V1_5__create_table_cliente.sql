CREATE TABLE cliente (
    id UUID PRIMARY KEY,
    pessoa_id UUID NOT NULL,
    CONSTRAINT fk_cliente_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE
); 