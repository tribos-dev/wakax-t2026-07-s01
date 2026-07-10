CREATE TABLE produto (
    id UUID PRIMARY KEY,
    descricao VARCHAR(150) NOT NULL,
    status VARCHAR(20) NOT NULL,
    peso_liquido NUMERIC(15,3) NOT NULL,
    peso_bruto NUMERIC(15,3) NOT NULL,
    descricao_complementar VARCHAR(500),
    grupo VARCHAR(50),
    unidade VARCHAR(10),
    estoque_minimo INTEGER,
    estoque_maximo INTEGER
); 