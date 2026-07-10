ALTER TABLE estoque DROP COLUMN valor_venda;
ALTER TABLE estoque ADD CONSTRAINT uk_estoque_produto UNIQUE (produto_id);