package br.com.wakax.wakax_ecommerce.pagamento.application.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PagamentoResumoResponse {
  private final UUID idPagamento;
  private final UUID pedidoId;
  private final StatusPagamento statusPagamento;
  private final LocalDateTime dataPagamento;
  private final BigDecimal valor;
}
