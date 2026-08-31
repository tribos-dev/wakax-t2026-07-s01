CREATE TABLE tentativa_pagamento (
    id UUID PRIMARY KEY,
    pagamento_id UUID NOT NULL,
    numero_tentativa INTEGER NOT NULL CHECK (numero_tentativa BETWEEN 1 AND 3),
    data_tentativa TIMESTAMP NOT NULL,
    CONSTRAINT fk_tentativa_pagamento_pagamento
        FOREIGN KEY (pagamento_id) REFERENCES pagamento(id),
    CONSTRAINT uk_tentativa_pagamento_numero
        UNIQUE (pagamento_id, numero_tentativa)
);
