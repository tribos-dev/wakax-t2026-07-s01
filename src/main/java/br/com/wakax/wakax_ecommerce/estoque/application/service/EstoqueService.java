package br.com.wakax.wakax_ecommerce.estoque.application.service;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.estoque.api.request.EstoqueRequest;
import br.com.wakax.wakax_ecommerce.estoque.api.response.EstoqueListagemResponse;
import br.com.wakax.wakax_ecommerce.estoque.api.response.EstoqueResponse;

public interface EstoqueService {
  EstoqueResponse criaEstoque(UUID idProduto, EstoqueRequest request);

  EstoqueResponse buscaEstoquePorIdProduto(UUID idProduto);

  EstoqueListagemResponse buscaTodosEstoques(
      Boolean quantidadeMinima, Boolean emFalta, int pagina, int tamanho);

  boolean temQuantidadeDisponivel(UUID idProduto, Integer quantidade);

  void reservaQuantidade(UUID idProduto, Integer quantidade);

  void liberaReserva(UUID idProduto, Integer quantidade);
}
