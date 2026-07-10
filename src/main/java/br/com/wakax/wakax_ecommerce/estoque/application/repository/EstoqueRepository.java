package br.com.wakax.wakax_ecommerce.estoque.application.repository;

import java.util.Optional;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.estoque.domain.Estoque;

public interface EstoqueRepository {
  Estoque salva(Estoque estoque);

  Optional<Estoque> buscaEstoquePorIdProduto(UUID idProduto);

  Estoque buscaEstoquePorId(UUID idEstoque);
}
