package br.com.wakax.wakax_ecommerce.pagamento.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.gateway.GatewayPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.application.gateway.GatewayPagamentoException;
import br.com.wakax.wakax_ecommerce.pagamento.application.gateway.SolicitacaoReprocessamentoPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.application.notificacao.NotificacaoReprocessamentoPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.application.notificacao.NotificadorPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;

@ExtendWith(MockitoExtension.class)
class ReprocessamentoPagamentoServiceTest {

  @Mock private ReprocessamentoPagamentoTransacionalService transacionalService;
  @Mock private GatewayPagamento gatewayPagamento;
  @Mock private NotificadorPagamento notificadorPagamento;

  @InjectMocks private ReprocessamentoPagamentoService reprocessamentoPagamentoService;

  private SolicitacaoReprocessamentoPagamento solicitacao;
  private Pagamento pagamento;

  @BeforeEach
  void setUp() {
    pagamento = PagamentoDataHelper.criaPagamentoValido(PagamentoDataHelper.criaPedidoValido());
    pagamento.setStatusPagamento(StatusPagamento.AGUARDANDO);
    pagamento.setNumeroTentativas(2);
    solicitacao =
        SolicitacaoReprocessamentoPagamento.builder()
            .idPagamento(pagamento.getId())
            .idPedido(pagamento.getPedido().getId())
            .valor(pagamento.getValor())
            .formaPagamento(pagamento.getPedido().getFormaPagamento())
            .numeroTentativa(2)
            .chaveIdempotencia("pagamento:" + pagamento.getId() + ":tentativa:2")
            .build();
  }

  @Test
  void deveEnviarNovaTentativaAoGatewayENotificarCliente() {
    when(transacionalService.preparaReprocessamento(pagamento.getId())).thenReturn(solicitacao);
    when(transacionalService.registraEnvioAceito(
            pagamento.getId(), solicitacao.getChaveIdempotencia()))
        .thenReturn(pagamento);

    PagamentoResponse response =
        reprocessamentoPagamentoService.reprocessaPagamento(pagamento.getId());

    assertEquals(StatusPagamento.AGUARDANDO, response.getStatusPagamento());
    assertEquals(2, response.getNumeroTentativas());
    verify(gatewayPagamento).reprocessa(solicitacao);
    verify(transacionalService)
        .registraEnvioAceito(pagamento.getId(), solicitacao.getChaveIdempotencia());

    ArgumentCaptor<NotificacaoReprocessamentoPagamento> notificacaoCaptor =
        ArgumentCaptor.forClass(NotificacaoReprocessamentoPagamento.class);
    verify(notificadorPagamento).notificaReprocessamentoSolicitado(notificacaoCaptor.capture());
    assertEquals(pagamento.getId(), notificacaoCaptor.getValue().getIdPagamento());
    assertEquals(2, notificacaoCaptor.getValue().getNumeroTentativa());
  }

  @Test
  void deveConsumirTentativaQuandoGatewayFalhar() {
    when(transacionalService.preparaReprocessamento(pagamento.getId())).thenReturn(solicitacao);
    doThrow(new GatewayPagamentoException("timeout"))
        .when(gatewayPagamento)
        .reprocessa(solicitacao);

    APIException exception =
        assertThrows(
            APIException.class,
            () -> reprocessamentoPagamentoService.reprocessaPagamento(pagamento.getId()));

    assertEquals(HttpStatus.BAD_GATEWAY, exception.getStatusException());
    assertEquals(ErrorCode.GATEWAY_PAGAMENTO_INDISPONIVEL, exception.getErrorCode());
    verify(transacionalService)
        .registraFalhaEnvio(pagamento.getId(), solicitacao.getChaveIdempotencia(), "timeout");
    verify(transacionalService, never()).registraEnvioAceito(any(), any());
    verify(notificadorPagamento, never()).notificaReprocessamentoSolicitado(any());
  }

  @Test
  void naoDeveTratarErroInesperadoComoIndisponibilidadeDoGateway() {
    when(transacionalService.preparaReprocessamento(pagamento.getId())).thenReturn(solicitacao);
    RuntimeException erroInesperado = new NullPointerException("erro inesperado");
    doThrow(erroInesperado).when(gatewayPagamento).reprocessa(solicitacao);

    RuntimeException exception =
        assertThrows(
            RuntimeException.class,
            () -> reprocessamentoPagamentoService.reprocessaPagamento(pagamento.getId()));

    assertSame(erroInesperado, exception);
    verify(transacionalService, never()).registraFalhaEnvio(any(), any(), any());
    verify(transacionalService, never()).registraEnvioAceito(any(), any());
    verify(notificadorPagamento, never()).notificaReprocessamentoSolicitado(any());
  }

  @Test
  void naoDeveDesfazerReprocessamentoQuandoNotificacaoFalhar() {
    when(transacionalService.preparaReprocessamento(pagamento.getId())).thenReturn(solicitacao);
    when(transacionalService.registraEnvioAceito(
            pagamento.getId(), solicitacao.getChaveIdempotencia()))
        .thenReturn(pagamento);
    doThrow(new RuntimeException("canal indisponivel"))
        .when(notificadorPagamento)
        .notificaReprocessamentoSolicitado(any());

    PagamentoResponse response =
        reprocessamentoPagamentoService.reprocessaPagamento(pagamento.getId());

    assertEquals(StatusPagamento.AGUARDANDO, response.getStatusPagamento());
    verify(gatewayPagamento).reprocessa(solicitacao);
    verify(transacionalService)
        .registraEnvioAceito(pagamento.getId(), solicitacao.getChaveIdempotencia());
  }
}
