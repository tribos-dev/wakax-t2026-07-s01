CREATE TABLE pagamento (
    id UUID PRIMARY KEY,
    pedido_id UUID NOT NULL UNIQUE,
    status_pagamento VARCHAR(20) NOT NULL,
    data_pagamento TIMESTAMP NOT NULL,
    valor NUMERIC(15,2) NOT NULL CHECK (valor >= 0),
    CONSTRAINT fk_pagamento_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE
); 