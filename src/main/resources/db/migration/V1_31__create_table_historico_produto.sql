CREATE TABLE historico_produto(
    Id UUID PRIMARY KEY ,
    produto_id UUID NOT NULL ,
    usuario_id UUID NOT NULL ,
    data_registro TIMESTAMP NOT NULL ,
    CONSTRAINT fk_hitorico_produto_produto FOREIGN KEY(produto_id) REFERENCES  produto(id) ON DELETE CASCADE ,
    CONSTRAINT fk_hitorico_produto_usuario FOREIGN KEY(usuario_id) REFERENCES  usuario(id_usuario)
);

CREATE INDEX idx_historico_produto_produto ON historico_produto(produto_id);
CREATE INDEX idx_historico_produto_usuario ON historico_produto(usuario_id);
CREATE INDEX idx_historico_produto_data ON historico_produto(data_registro);