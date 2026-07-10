package br.com.wakax.wakax_ecommerce.carrinho.application.strategy;

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;

public interface ProcessadorEstoque {
  void aoAdicionarItem(Produto produto, Integer quantidade);

  void aoAlterarQuantidade(Produto produto, Integer quantidadeAtual, Integer novaQuantidade);

  void aoRemoverItem(Produto produto, Integer quantidade);
}
