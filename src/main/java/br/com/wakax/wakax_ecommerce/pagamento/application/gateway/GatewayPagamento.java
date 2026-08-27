package br.com.wakax.wakax_ecommerce.pagamento.application.gateway;

public interface GatewayPagamento {

  void reprocessa(SolicitacaoReprocessamentoPagamento solicitacao);
}
