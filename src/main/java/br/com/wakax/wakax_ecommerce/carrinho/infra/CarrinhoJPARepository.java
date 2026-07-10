package br.com.wakax.wakax_ecommerce.carrinho.infra;

import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wakax.wakax_ecommerce.carrinho.domain.Carrinho;

public interface CarrinhoJPARepository extends JpaRepository<Carrinho, UUID> {
  @EntityGraph(
      attributePaths = {
        "itensCarrinho",
        "itensCarrinho.produto",
        "cliente",
        "cliente.pessoa",
        "cliente.pessoa.enderecos"
      })
  java.util.Optional<Carrinho> findById(UUID id);
}
