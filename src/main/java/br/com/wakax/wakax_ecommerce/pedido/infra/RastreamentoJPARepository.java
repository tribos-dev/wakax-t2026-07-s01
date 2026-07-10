package br.com.wakax.wakax_ecommerce.pedido.infra;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wakax.wakax_ecommerce.pedido.domain.Rastreamento;

public interface RastreamentoJPARepository extends JpaRepository<Rastreamento, UUID> {

  @EntityGraph(attributePaths = {"eventos"})
  Optional<Rastreamento> findByPedidoId(UUID pedidoId);
}
