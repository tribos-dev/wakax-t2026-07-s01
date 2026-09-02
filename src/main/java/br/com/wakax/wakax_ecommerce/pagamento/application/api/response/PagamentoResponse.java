package br.com.wakax.wakax_ecommerce.pagamento.application.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;
import br.com.wakax.wakax_ecommerce.pedido.domain.FormaPagamento;
import lombok.Getter;

@Getter
public class PagamentoResponse {
  private final UUID idPagamento;
  private final UUID pedidoId;
  private final StatusPagamento statusPagamento;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private final String motivoCancelamento;

  private final LocalDateTime dataPagamento;
  private final BigDecimal valor;
  private final FormaPagamento formaPagamento;

  public PagamentoResponse(Pagamento pagamento) {
    this.idPagamento = pagamento.getId();
    this.pedidoId = pagamento.getPedido().getId();
    this.statusPagamento = pagamento.getStatusPagamento();
    this.motivoCancelamento = pagamento.getMotivoCancelamento();
    this.dataPagamento = pagamento.getDataPagamento();
    this.valor = pagamento.getValor();
    this.formaPagamento = pagamento.getPedido().getFormaPagamento();
  }
}
