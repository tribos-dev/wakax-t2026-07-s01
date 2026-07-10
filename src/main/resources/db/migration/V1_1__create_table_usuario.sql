CREATE TABLE usuario (
    id_usuario UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL
); 