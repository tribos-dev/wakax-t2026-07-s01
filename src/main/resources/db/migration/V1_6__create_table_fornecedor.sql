CREATE TABLE fornecedor (
    id UUID PRIMARY KEY,
    pessoa_id UUID NOT NULL,
    CONSTRAINT fk_fornecedor_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE
); 