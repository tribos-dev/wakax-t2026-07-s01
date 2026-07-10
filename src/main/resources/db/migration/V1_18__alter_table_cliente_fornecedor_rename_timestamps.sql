-- Alterar tabela FORNECEDOR: remover colunas antigas e adicionar novas
ALTER TABLE fornecedor 
DROP COLUMN IF EXISTS created_at,
DROP COLUMN IF EXISTS updated_at;

ALTER TABLE fornecedor 
ADD COLUMN data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN data_edicao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Alterar tabela CLIENTE: adicionar colunas novas (não existem as antigas)
ALTER TABLE cliente 
ADD COLUMN data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN data_edicao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

