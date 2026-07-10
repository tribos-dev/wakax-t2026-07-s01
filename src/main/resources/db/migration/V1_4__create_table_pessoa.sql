CREATE TABLE pessoa (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf_cnpj VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE pessoa_emails (
    pessoa_id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    CONSTRAINT fk_pessoa_emails_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE
);

CREATE TABLE pessoa_telefones (
    pessoa_id UUID NOT NULL,
    telefone VARCHAR(30) NOT NULL,
    CONSTRAINT fk_pessoa_telefones_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE
);

CREATE TABLE endereco (
    id UUID PRIMARY KEY,
    logradouro VARCHAR(255),
    numero VARCHAR(20),
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(50),
    cep VARCHAR(20),
    principal BOOLEAN,
    pessoa_id UUID,
    CONSTRAINT fk_endereco_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE
); 