package br.com.wakax.wakax_ecommerce.pagamento.infra.gateway;

import org.springframework.stereotype.Component;

import br.com.wakax.wakax_ecommerce.pagamento.application.gateway.GatewayPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.application.gateway.SolicitacaoReprocessamentoPagamento;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class GatewayPagamentoSimulado implements GatewayPagamento {

  @Override
  public void reprocessa(SolicitacaoReprocessamentoPagamento solicitacao) {
    log.info(
        "Reprocessamento simulado enviado ao gateway. pagamento={}, tentativa={}, idempotencia={}",
        solicitacao.getIdPagamento(),
        solicitacao.getNumeroTentativa(),
        solicitacao.getChaveIdempotencia());
  }
}
