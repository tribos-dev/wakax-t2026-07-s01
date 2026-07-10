CREATE TABLE credencial (
    id_credencial UUID PRIMARY KEY,
    id_usuario UUID NOT NULL,
    usuario VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(60) NOT NULL,
    validado BOOLEAN,
    CONSTRAINT fk_credencial_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);