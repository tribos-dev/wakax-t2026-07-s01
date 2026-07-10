package br.com.wakax.wakax_ecommerce.produto.application.repository;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;

public interface ProdutoRepository {
  Produto salva(Produto produto);

  Produto buscaProdutoPorId(UUID idProduto);
}
