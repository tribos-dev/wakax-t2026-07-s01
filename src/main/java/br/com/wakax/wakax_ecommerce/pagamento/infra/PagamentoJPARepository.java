package br.com.wakax.wakax_ecommerce.pagamento.infra;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;

public interface PagamentoJPARepository extends JpaRepository<Pagamento, UUID> {

  @Query("SELECT p FROM Pagamento p JOIN FETCH p.pedido WHERE p.id = :idPagamento")
  java.util.Optional<Pagamento> findByIdComPedido(@Param("idPagamento") UUID idPagamento);

  @Query("SELECT p FROM Pagamento p WHERE p.pedido.id = :idPedido")
  Optional<Pagamento> findByPedidoId(@Param("idPedido") UUID idPedido);
}
