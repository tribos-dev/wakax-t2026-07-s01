package br.com.wakax.wakax_ecommerce.estoque.application.repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.wakax.wakax_ecommerce.estoque.domain.Estoque;

public interface EstoqueRepository {
  Estoque salva(Estoque estoque);

  Optional<Estoque> buscaEstoquePorIdProduto(UUID idProduto);

  Estoque buscaEstoquePorId(UUID idEstoque);

  Page<Estoque> buscaTodosEstoques(Boolean quantidadeMinima, Boolean emFalta, Pageable pageable);

  BigDecimal calculaValorTotalInventario(Boolean quantidadeMinima, Boolean emFalta);
}
