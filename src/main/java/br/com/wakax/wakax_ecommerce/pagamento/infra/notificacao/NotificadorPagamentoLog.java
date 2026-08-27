package br.com.wakax.wakax_ecommerce.pagamento.infra.notificacao;

import org.springframework.stereotype.Component;

import br.com.wakax.wakax_ecommerce.pagamento.application.notificacao.NotificacaoReprocessamentoPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.application.notificacao.NotificadorPagamento;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class NotificadorPagamentoLog implements NotificadorPagamento {

  @Override
  public void notificaReprocessamentoSolicitado(NotificacaoReprocessamentoPagamento notificacao) {
    log.info(
        "Notificacao de reprocessamento registrada. pagamento={}, pedido={}, tentativa={}",
        notificacao.getIdPagamento(),
        notificacao.getIdPedido(),
        notificacao.getNumeroTentativa());
  }
}
