package br.com.wakax.wakax_ecommerce.pagamento.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pagamento.application.gateway.SolicitacaoReprocessamentoPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.application.repository.PagamentoRepository;
import br.com.wakax.wakax_ecommerce.pagamento.application.repository.TentativaPagamentoRepository;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusTentativaPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.TentativaPagamento;

@ExtendWith(MockitoExtension.class)
class ReprocessamentoPagamentoTransacionalServiceTest {

  @Mock private PagamentoRepository pagamentoRepository;
  @Mock private TentativaPagamentoRepository tentativaPagamentoRepository;

  @InjectMocks
  private ReprocessamentoPagamentoTransacionalService reprocessamentoTransacionalService;

  private Pagamento pagamento;
  private UUID idPagamento;

  @BeforeEach
  void setUp() {
    pagamento = PagamentoDataHelper.criaPagamentoValido(PagamentoDataHelper.criaPedidoValido());
    pagamento.setStatusPagamento(StatusPagamento.FALHOU);
    pagamento.setNumeroTentativas(1);
    idPagamento = pagamento.getId();
  }

  @Test
  void devePrepararSegundaTentativaComSucesso() {
    when(pagamentoRepository.buscaPagamentoPorIdParaAtualizacao(idPagamento)).thenReturn(pagamento);

    SolicitacaoReprocessamentoPagamento solicitacao =
        reprocessamentoTransacionalService.preparaReprocessamento(idPagamento);

    assertEquals(StatusPagamento.AGUARDANDO, pagamento.getStatusPagamento());
    assertEquals(2, pagamento.getNumeroTentativas());
    assertEquals(idPagamento, solicitacao.getIdPagamento());
    assertEquals(2, solicitacao.getNumeroTentativa());
    assertEquals("pagamento:" + idPagamento + ":tentativa:2", solicitacao.getChaveIdempotencia());

    ArgumentCaptor<TentativaPagamento> tentativaCaptor =
        ArgumentCaptor.forClass(TentativaPagamento.class);
    verify(pagamentoRepository).salva(pagamento);
    verify(tentativaPagamentoRepository).salva(tentativaCaptor.capture());
    assertEquals(StatusTentativaPagamento.PENDENTE_ENVIO, tentativaCaptor.getValue().getStatus());
  }

  @Test
  void deveRejeitarPagamentoJaProcessadoComSucesso() {
    pagamento.setStatusPagamento(StatusPagamento.PAGO);
    when(pagamentoRepository.buscaPagamentoPorIdParaAtualizacao(idPagamento)).thenReturn(pagamento);

    APIException exception =
        assertThrows(
            APIException.class,
            () -> reprocessamentoTransacionalService.preparaReprocessamento(idPagamento));

    assertEquals(HttpStatus.CONFLICT, exception.getStatusException());
    assertEquals(ErrorCode.PAGAMENTO_JA_PROCESSADO_COM_SUCESSO, exception.getErrorCode());
    verify(pagamentoRepository, never()).salva(any());
    verify(tentativaPagamentoRepository, never()).salva(any());
  }

  @ParameterizedTest
  @EnumSource(
      value = StatusPagamento.class,
      names = {"AGUARDANDO", "CANCELADO"})
  void deveRejeitarPagamentoQueNaoFalhou(StatusPagamento statusPagamento) {
    pagamento.setStatusPagamento(statusPagamento);
    when(pagamentoRepository.buscaPagamentoPorIdParaAtualizacao(idPagamento)).thenReturn(pagamento);

    APIException exception =
        assertThrows(
            APIException.class,
            () -> reprocessamentoTransacionalService.preparaReprocessamento(idPagamento));

    assertEquals(ErrorCode.PAGAMENTO_NAO_PODE_SER_REPROCESSADO, exception.getErrorCode());
    assertEquals(statusPagamento, exception.getArgs()[0]);
    verify(pagamentoRepository, never()).salva(any());
  }

  @Test
  void devePropagarPagamentoNaoEncontrado() {
    when(pagamentoRepository.buscaPagamentoPorIdParaAtualizacao(idPagamento))
        .thenThrow(
            new APIException(
                HttpStatus.NOT_FOUND, ErrorCode.PAGAMENTO_NAO_ENCONTRADO, idPagamento));

    APIException exception =
        assertThrows(
            APIException.class,
            () -> reprocessamentoTransacionalService.preparaReprocessamento(idPagamento));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
    assertEquals(ErrorCode.PAGAMENTO_NAO_ENCONTRADO, exception.getErrorCode());
    verify(pagamentoRepository, never()).salva(any());
  }

  @Test
  void deveRejeitarPagamentoAoAtingirTresTentativas() {
    pagamento.setNumeroTentativas(Pagamento.LIMITE_TENTATIVAS);
    when(pagamentoRepository.buscaPagamentoPorIdParaAtualizacao(idPagamento)).thenReturn(pagamento);

    APIException exception =
        assertThrows(
            APIException.class,
            () -> reprocessamentoTransacionalService.preparaReprocessamento(idPagamento));

    assertEquals(HttpStatus.CONFLICT, exception.getStatusException());
    assertEquals(ErrorCode.LIMITE_TENTATIVAS_PAGAMENTO_EXCEDIDO, exception.getErrorCode());
    verify(pagamentoRepository, never()).salva(any());
    verify(tentativaPagamentoRepository, never()).salva(any());
  }

  @Test
  void deveMarcarHistoricoComoEnviado() {
    TentativaPagamento tentativa = criaTentativaPendente();
    when(pagamentoRepository.buscaPagamentoPorIdParaAtualizacao(idPagamento)).thenReturn(pagamento);
    when(tentativaPagamentoRepository.buscaPorChaveIdempotencia(tentativa.getChaveIdempotencia()))
        .thenReturn(tentativa);

    Pagamento resultado =
        reprocessamentoTransacionalService.registraEnvioAceito(
            idPagamento, tentativa.getChaveIdempotencia());

    assertEquals(pagamento, resultado);
    assertEquals(StatusTentativaPagamento.ENVIADA, tentativa.getStatus());
    verify(tentativaPagamentoRepository).salva(tentativa);
  }

  @Test
  void deveMarcarPagamentoEHistoricoComoFalhaSemDecrementarTentativa() {
    pagamento.iniciarReprocessamento();
    TentativaPagamento tentativa = TentativaPagamento.pendente(pagamento);
    when(pagamentoRepository.buscaPagamentoPorIdParaAtualizacao(idPagamento)).thenReturn(pagamento);
    when(tentativaPagamentoRepository.buscaPorChaveIdempotencia(tentativa.getChaveIdempotencia()))
        .thenReturn(tentativa);

    reprocessamentoTransacionalService.registraFalhaEnvio(
        idPagamento, tentativa.getChaveIdempotencia(), "timeout");

    assertEquals(StatusPagamento.FALHOU, pagamento.getStatusPagamento());
    assertEquals(2, pagamento.getNumeroTentativas());
    assertEquals(StatusTentativaPagamento.FALHA_ENVIO, tentativa.getStatus());
    assertEquals("timeout", tentativa.getDetalhe());
    verify(pagamentoRepository).salva(pagamento);
    verify(tentativaPagamentoRepository).salva(tentativa);
  }

  private TentativaPagamento criaTentativaPendente() {
    pagamento.iniciarReprocessamento();
    return TentativaPagamento.pendente(pagamento);
  }
}
