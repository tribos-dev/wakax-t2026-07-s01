package br.com.wakax.wakax_ecommerce.pagamento.application.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.gateway.GatewayPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.application.gateway.GatewayPagamentoException;
import br.com.wakax.wakax_ecommerce.pagamento.application.gateway.SolicitacaoReprocessamentoPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.application.notificacao.NotificacaoReprocessamentoPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.application.notificacao.NotificadorPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReprocessamentoPagamentoService {

  private final ReprocessamentoPagamentoTransacionalService transacionalService;
  private final GatewayPagamento gatewayPagamento;
  private final NotificadorPagamento notificadorPagamento;

  public PagamentoResponse reprocessaPagamento(UUID idPagamento) {
    log.debug("[start] ReprocessamentoPagamentoService - reprocessaPagamento");

    SolicitacaoReprocessamentoPagamento solicitacao =
        transacionalService.preparaReprocessamento(idPagamento);

    enviaAoGateway(solicitacao);
    Pagamento pagamento =
        transacionalService.registraEnvioAceito(
            solicitacao.getIdPagamento(), solicitacao.getChaveIdempotencia());
    notificaCliente(solicitacao);

    log.debug("[finish] ReprocessamentoPagamentoService - reprocessaPagamento");
    return new PagamentoResponse(pagamento);
  }

  private void enviaAoGateway(SolicitacaoReprocessamentoPagamento solicitacao) {
    try {
      gatewayPagamento.reprocessa(solicitacao);
    } catch (GatewayPagamentoException exception) {
      transacionalService.registraFalhaEnvio(
          solicitacao.getIdPagamento(), solicitacao.getChaveIdempotencia(), exception.getMessage());
      throw new APIException(
          HttpStatus.BAD_GATEWAY, ErrorCode.GATEWAY_PAGAMENTO_INDISPONIVEL, exception);
    }
  }

  private void notificaCliente(SolicitacaoReprocessamentoPagamento solicitacao) {
    NotificacaoReprocessamentoPagamento notificacao =
        NotificacaoReprocessamentoPagamento.builder()
            .idPagamento(solicitacao.getIdPagamento())
            .idPedido(solicitacao.getIdPedido())
            .numeroTentativa(solicitacao.getNumeroTentativa())
            .build();
    try {
      notificadorPagamento.notificaReprocessamentoSolicitado(notificacao);
    } catch (RuntimeException exception) {
      log.error(
          "Falha ao notificar reprocessamento do pagamento {}",
          solicitacao.getIdPagamento(),
          exception);
    }
  }
}
