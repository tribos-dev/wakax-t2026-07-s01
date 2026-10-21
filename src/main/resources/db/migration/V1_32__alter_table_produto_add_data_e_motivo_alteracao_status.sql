ALTER TABLE produto
ADD COLUMN data_alteracao_status TIMESTAMP;

ALTER TABLE produto
ADD COLUMN motivo_alteracao_status VARCHAR(255);