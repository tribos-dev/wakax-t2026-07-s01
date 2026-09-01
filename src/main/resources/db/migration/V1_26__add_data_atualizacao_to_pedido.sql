ALTER TABLE pedido
    ADD COLUMN data_atualizacao TIMESTAMP;

UPDATE pedido
SET data_atualizacao = data_pedido;

ALTER TABLE pedido
    ALTER COLUMN data_atualizacao SET NOT NULL;
