package br.com.wakax.wakax_ecommerce.pagamento.application.notificacao;

public interface NotificadorPagamento {

  void notificaReprocessamentoSolicitado(NotificacaoReprocessamentoPagamento notificacao);
}
