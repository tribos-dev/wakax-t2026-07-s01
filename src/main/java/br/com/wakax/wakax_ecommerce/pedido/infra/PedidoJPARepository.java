package br.com.wakax.wakax_ecommerce.pedido.infra;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoResumoProjection;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;

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
      value =
          "SELECT p.id as id, "
              + "p.dataPedido as dataPedido, "
              + "p.status as status, "
              + "p.valorTotal as valorTotal "
              + "FROM Pedido p "
              + "WHERE p.cliente.id = :idCliente "
              + "AND (:status IS NULL OR p.status = :status)",
      countQuery =
          "SELECT COUNT(p) "
              + "FROM Pedido p "
              + "WHERE p.cliente.id = :idCliente "
              + "AND (:status IS NULL OR p.status = :status)")
  Page<PedidoResumoProjection> buscaPedidosDoCliente(
      @Param("idCliente") UUID idCliente, @Param("status") StatusPedido status, Pageable pageable);
}
