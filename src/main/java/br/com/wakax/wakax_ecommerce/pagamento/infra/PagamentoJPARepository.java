package br.com.wakax.wakax_ecommerce.pagamento.infra;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResumoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;

public interface PagamentoJPARepository extends JpaRepository<Pagamento, UUID> {

  @Query("SELECT p FROM Pagamento p JOIN FETCH p.pedido WHERE p.id = :idPagamento")
  java.util.Optional<Pagamento> findByIdComPedido(@Param("idPagamento") UUID idPagamento);

  @Query("SELECT p FROM Pagamento p JOIN FETCH p.pedido WHERE p.pedido.id = :idPedido")
  Optional<Pagamento> findByPedidoId(@Param("idPedido") UUID idPedido);

  @Query(
      value =
          "select new br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResumoResponse("
              + "p.id, p.pedido.id, p.statusPagamento, p.dataPagamento, p.valor) "
              + "FROM Pagamento p "
              + "WHERE (:status IS NULL OR p.statusPagamento = :status) "
              + "ORDER BY p.dataPagamento DESC",
      countQuery =
          "select count(p) "
              + "from Pagamento p "
              + "where (:status is null or p.statusPagamento = :status)")
  Page<PagamentoResumoResponse> buscaPagamentos(
      @Param("status") StatusPagamento status, Pageable pageable);

  @Query(
      value =
          "select coalesce(sum(p.valor), 0) "
              + "from Pagamento p "
              + "where (:status is null or p.statusPagamento = :status)")
  BigDecimal somaValores(@Param("status") StatusPagamento status);
}
