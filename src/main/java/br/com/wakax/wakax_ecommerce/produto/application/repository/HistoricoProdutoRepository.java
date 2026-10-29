package br.com.wakax.wakax_ecommerce.produto.application.repository;

import br.com.wakax.wakax_ecommerce.produto.domain.HistoricoProduto;

public interface HistoricoProdutoRepository {
  void salva(HistoricoProduto historico);
}
