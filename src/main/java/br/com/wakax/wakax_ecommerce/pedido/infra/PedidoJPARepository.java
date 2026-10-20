package br.com.wakax.wakax_ecommerce.pedido.infra;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoFilterRequest;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;

public interface PedidoJPARepository extends JpaRepository<Pedido, UUID> {

  @EntityGraph(
      attributePaths = {
        "itensPedido",
        "itensPedido.produto",
        "cliente",
        "cliente.pessoa",
        "enderecoEntrega"
      })
  Optional<Pedido> findById(UUID id);

  @Query(
      """
        SELECT p from Pedido p
        WHERE p.cliente.id = :idCliente
                AND (:#{#filter.status} IS NULL OR p.status = :#{#filter.status})
        """)
  Page<Pedido> findAllByClienteIdWithFilters(
      @Param("filter") PedidoFilterRequest loteFilterRequest,
      Pageable pageable,
      @Param("idCliente") UUID idCliente);
}
