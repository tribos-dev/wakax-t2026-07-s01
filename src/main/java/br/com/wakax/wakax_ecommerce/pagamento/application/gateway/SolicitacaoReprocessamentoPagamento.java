package br.com.wakax.wakax_ecommerce.pagamento.application.gateway;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pedido.domain.FormaPagamento;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SolicitacaoReprocessamentoPagamento {
  UUID idPagamento;
  UUID idPedido;
  BigDecimal valor;
  FormaPagamento formaPagamento;
  int numeroTentativa;
  String chaveIdempotencia;
}
