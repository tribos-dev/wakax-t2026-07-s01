package br.com.wakax.wakax_ecommerce.pagamento.application.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;

public interface PagamentoResumoProjection {
  UUID getId();

  UUID getPedidoId();

  StatusPagamento getStatusPagamento();

  LocalDateTime getDataPagamento();

  BigDecimal getValor();
}
