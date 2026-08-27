package br.com.wakax.wakax_ecommerce.pagamento.application.notificacao;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NotificacaoReprocessamentoPagamento {
  UUID idPagamento;
  UUID idPedido;
  int numeroTentativa;
}
