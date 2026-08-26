ALTER TABLE pagamento
    ADD COLUMN numero_tentativas INTEGER NOT NULL DEFAULT 1;

ALTER TABLE pagamento
    ADD CONSTRAINT chk_pagamento_numero_tentativas
        CHECK (numero_tentativas BETWEEN 1 AND 3);

CREATE TABLE tentativa_pagamento (
    id UUID PRIMARY KEY,
    pagamento_id UUID NOT NULL,
    numero_tentativa INTEGER NOT NULL CHECK (numero_tentativa BETWEEN 2 AND 3),
    data_tentativa TIMESTAMP NOT NULL,
    chave_idempotencia VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL,
    detalhe VARCHAR(500),
    CONSTRAINT fk_tentativa_pagamento_pagamento
        FOREIGN KEY (pagamento_id) REFERENCES pagamento(id) ON DELETE CASCADE,
    CONSTRAINT uk_tentativa_pagamento_numero
        UNIQUE (pagamento_id, numero_tentativa),
    CONSTRAINT uk_tentativa_pagamento_idempotencia
        UNIQUE (chave_idempotencia)
);