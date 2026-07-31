package br.com.wakax.wakax_ecommerce.pagamento.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.request.PagamentoRequest;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoPaginadoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResumoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.factory.ProcessadorPagamentoFactory;
import br.com.wakax.wakax_ecommerce.pagamento.application.repository.PagamentoRepository;
import br.com.wakax.wakax_ecommerce.pagamento.application.strategy.PagamentoAguardandoStrategy;
import br.com.wakax.wakax_ecommerce.pagamento.application.strategy.PagamentoImediatoStrategy;
import br.com.wakax.wakax_ecommerce.pagamento.application.strategy.ProcessadorPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;
import br.com.wakax.wakax_ecommerce.pedido.application.repository.PedidoRepository;
import br.com.wakax.wakax_ecommerce.pedido.domain.FormaPagamento;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;

@ExtendWith(MockitoExtension.class)
class PagamentoApplicationServiceTest {

  @Mock private PagamentoRepository pagamentoRepository;
  @Mock private PedidoRepository pedidoRepository;
  @Mock private ProcessadorPagamentoFactory processadorFactory;
  @Mock private ProcessadorPagamento processadorPagamento;
  @Mock private PagamentoImediatoStrategy pagamentoImediatoStrategy;
  @Mock private PagamentoAguardandoStrategy pagamentoAguardandoStrategy;

  @InjectMocks private PagamentoApplicationService pagamentoApplicationService;
  @InjectMocks private ProcessadorPagamentoFactory realProcessadorFactory;

  private PagamentoRequest pagamentoRequest;
  private Pedido pedido;
  private Pagamento pagamento;
  private UUID pedidoId;
  private UUID pagamentoId;

  @BeforeEach
  void setUp() {
    pedidoId = UUID.randomUUID();
    pagamentoId = UUID.randomUUID();

    pagamentoRequest = PagamentoDataHelper.criaPagamentoRequestValido(pedidoId);
    pedido = PagamentoDataHelper.criaPedidoValido();
    pagamento = PagamentoDataHelper.criaPagamentoValido(pedido);
    pagamento.setId(pagamentoId);
  }

  @Test
  void deveCriarPagamentoComSucesso() {
    when(pagamentoRepository.buscaPagamentoPorPedidoId(pedidoId)).thenReturn(Optional.empty());
    when(pedidoRepository.buscaPedidoPorId(pedidoId)).thenReturn(pedido);
    when(processadorFactory.obterProcessador(pedido.getFormaPagamento()))
        .thenReturn(processadorPagamento);
    when(pagamentoRepository.salva(any(Pagamento.class)))
        .thenAnswer(
            invocation -> {
              Pagamento p = invocation.getArgument(0);
              p.setId(pagamentoId);
              return p;
            });

    PagamentoResponse response = pagamentoApplicationService.processaPagamento(pagamentoRequest);

    assertNotNull(response);
    assertEquals(pagamentoId, response.getIdPagamento());
    assertEquals(pedido.getId(), response.getPedidoId());
    assertEquals(StatusPagamento.AGUARDANDO, response.getStatusPagamento());
    assertEquals(pedido.getFormaPagamento(), response.getFormaPagamento());

    verify(pagamentoRepository).buscaPagamentoPorPedidoId(pedidoId);
    verify(pedidoRepository).buscaPedidoPorId(pedidoId);
    verify(processadorFactory).obterProcessador(pedido.getFormaPagamento());
    verify(pagamentoRepository).salva(any(Pagamento.class));
    verify(pedidoRepository).salva(pedido);
  }

  @Test
  void deveLancarExcecaoQuandoPedidoJaPossuiPagamento() {
    Pagamento pagamentoExistente = PagamentoDataHelper.criaPagamentoValido(pedido);
    when(pagamentoRepository.buscaPagamentoPorPedidoId(pedidoId))
        .thenReturn(Optional.of(pagamentoExistente));

    APIException exception =
        assertThrows(
            APIException.class,
            () -> pagamentoApplicationService.processaPagamento(pagamentoRequest));

    assertEquals(HttpStatus.CONFLICT, exception.getStatusException());
    assertEquals(ErrorCode.PEDIDO_JA_POSSUI_PAGAMENTO, exception.getErrorCode());

    verify(pagamentoRepository).buscaPagamentoPorPedidoId(pedidoId);
    verify(pedidoRepository, never()).buscaPedidoPorId(any());
    verify(pagamentoRepository, never()).salva(any());
  }

  @Test
  void deveBuscarPagamentoPorIdComSucesso() {
    when(pagamentoRepository.buscaPagamentoPorId(pagamentoId)).thenReturn(pagamento);

    PagamentoResponse response = pagamentoApplicationService.buscaPagamentoPorId(pagamentoId);

    assertNotNull(response);
    assertEquals(pagamentoId, response.getIdPagamento());
    assertEquals(pedido.getId(), response.getPedidoId());
    assertEquals(pedido.getFormaPagamento(), response.getFormaPagamento());

    verify(pagamentoRepository).buscaPagamentoPorId(pagamentoId);
  }

  @Test
  void deveBuscarPagamentoPorPedidoIdComSucesso() {
    when(pedidoRepository.buscaPedidoPorId(pedidoId)).thenReturn(pedido);
    when(pagamentoRepository.buscaPagamentoPorPedidoId(pedidoId))
        .thenReturn(Optional.of(pagamento));

    PagamentoResponse response = pagamentoApplicationService.buscaPagamentoPorPedidoId(pedidoId);

    assertNotNull(response);
    assertEquals(pagamentoId, response.getIdPagamento());
    assertEquals(pedido.getId(), response.getPedidoId());
    assertEquals(StatusPagamento.AGUARDANDO, response.getStatusPagamento());
    assertEquals(pedido.getValorTotal(), response.getValor());
    assertEquals(pedido.getFormaPagamento(), response.getFormaPagamento());

    verify(pedidoRepository).buscaPedidoPorId(pedidoId);
    verify(pagamentoRepository).buscaPagamentoPorPedidoId(pedidoId);
  }

  @Test
  void devePropagarExcecaoQuandoPedidoNaoExisteAoBuscarPagamentoPorPedidoId() {
    when(pedidoRepository.buscaPedidoPorId(pedidoId))
        .thenThrow(
            new APIException(HttpStatus.NOT_FOUND, ErrorCode.PEDIDO_NAO_ENCONTRADO, pedidoId));

    APIException exception =
        assertThrows(
            APIException.class,
            () -> pagamentoApplicationService.buscaPagamentoPorPedidoId(pedidoId));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
    assertEquals(ErrorCode.PEDIDO_NAO_ENCONTRADO, exception.getErrorCode());

    verify(pedidoRepository).buscaPedidoPorId(pedidoId);
    verify(pagamentoRepository, never()).buscaPagamentoPorPedidoId(any());
  }

  @Test
  void deveLancarExcecaoQuandoPedidoNaoPossuiPagamento() {
    when(pedidoRepository.buscaPedidoPorId(pedidoId)).thenReturn(pedido);
    when(pagamentoRepository.buscaPagamentoPorPedidoId(pedidoId)).thenReturn(Optional.empty());

    APIException exception =
        assertThrows(
            APIException.class,
            () -> pagamentoApplicationService.buscaPagamentoPorPedidoId(pedidoId));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
    assertEquals(ErrorCode.PAGAMENTO_DO_PEDIDO_NAO_ENCONTRADO, exception.getErrorCode());

    verify(pedidoRepository).buscaPedidoPorId(pedidoId);
    verify(pagamentoRepository).buscaPagamentoPorPedidoId(pedidoId);
  }

  @Test
  void deveLancarExcecaoQuandoPagamentoNaoEncontrado() {
    when(pagamentoRepository.buscaPagamentoPorId(pagamentoId))
        .thenThrow(
            new APIException(
                HttpStatus.NOT_FOUND, ErrorCode.PAGAMENTO_NAO_ENCONTRADO, pagamentoId));

    APIException exception =
        assertThrows(
            APIException.class, () -> pagamentoApplicationService.buscaPagamentoPorId(pagamentoId));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
    assertEquals(ErrorCode.PAGAMENTO_NAO_ENCONTRADO, exception.getErrorCode());

    verify(pagamentoRepository).buscaPagamentoPorId(pagamentoId);
  }

  @Test
  void deveProcessarPagamentoComProcessadorCorreto() {
    when(pagamentoRepository.buscaPagamentoPorPedidoId(pedidoId)).thenReturn(Optional.empty());
    when(pedidoRepository.buscaPedidoPorId(pedidoId)).thenReturn(pedido);
    when(processadorFactory.obterProcessador(pedido.getFormaPagamento()))
        .thenReturn(processadorPagamento);
    when(pagamentoRepository.salva(any(Pagamento.class))).thenReturn(pagamento);

    pagamentoApplicationService.processaPagamento(pagamentoRequest);

    verify(processadorPagamento).processar(any(Pagamento.class), eq(pedido));
  }

  @Test
  void deveCriarPagamentoComValorCorreto() {
    when(pagamentoRepository.buscaPagamentoPorPedidoId(pedidoId)).thenReturn(Optional.empty());
    when(pedidoRepository.buscaPedidoPorId(pedidoId)).thenReturn(pedido);
    when(processadorFactory.obterProcessador(pedido.getFormaPagamento()))
        .thenReturn(processadorPagamento);
    when(pagamentoRepository.salva(any(Pagamento.class))).thenReturn(pagamento);

    PagamentoResponse response = pagamentoApplicationService.processaPagamento(pagamentoRequest);

    assertEquals(pedido.getValorTotal(), response.getValor());
    verify(pagamentoRepository).salva(any(Pagamento.class));
  }

  @Test
  void deveUsarProcessadorCorretoParaCartaoCredito() {
    pedido.setFormaPagamento(FormaPagamento.CARTAO_CREDITO);
    when(pagamentoRepository.buscaPagamentoPorPedidoId(pedidoId)).thenReturn(Optional.empty());
    when(pedidoRepository.buscaPedidoPorId(pedidoId)).thenReturn(pedido);
    when(processadorFactory.obterProcessador(FormaPagamento.CARTAO_CREDITO))
        .thenReturn(processadorPagamento);
    when(pagamentoRepository.salva(any(Pagamento.class))).thenReturn(pagamento);

    pagamentoApplicationService.processaPagamento(pagamentoRequest);

    verify(processadorFactory).obterProcessador(FormaPagamento.CARTAO_CREDITO);
    verify(processadorPagamento).processar(any(Pagamento.class), eq(pedido));
  }

  @Test
  void deveUsarProcessadorCorretoParaPix() {
    pedido.setFormaPagamento(FormaPagamento.PIX);
    when(pagamentoRepository.buscaPagamentoPorPedidoId(pedidoId)).thenReturn(Optional.empty());
    when(pedidoRepository.buscaPedidoPorId(pedidoId)).thenReturn(pedido);
    when(processadorFactory.obterProcessador(FormaPagamento.PIX)).thenReturn(processadorPagamento);
    when(pagamentoRepository.salva(any(Pagamento.class))).thenReturn(pagamento);

    pagamentoApplicationService.processaPagamento(pagamentoRequest);

    verify(processadorFactory).obterProcessador(FormaPagamento.PIX);
    verify(processadorPagamento).processar(any(Pagamento.class), eq(pedido));
  }

  @Test
  void deveUsarProcessadorCorretoParaBoleto() {
    pedido.setFormaPagamento(FormaPagamento.BOLETO);
    when(pagamentoRepository.buscaPagamentoPorPedidoId(pedidoId)).thenReturn(Optional.empty());
    when(pedidoRepository.buscaPedidoPorId(pedidoId)).thenReturn(pedido);
    when(processadorFactory.obterProcessador(FormaPagamento.BOLETO))
        .thenReturn(processadorPagamento);
    when(pagamentoRepository.salva(any(Pagamento.class))).thenReturn(pagamento);

    pagamentoApplicationService.processaPagamento(pagamentoRequest);

    verify(processadorFactory).obterProcessador(FormaPagamento.BOLETO);
    verify(processadorPagamento).processar(any(Pagamento.class), eq(pedido));
  }

  @Test
  void deveRetornarPagamentoImediatoParaCartaoCredito() {
    ProcessadorPagamento processador =
        realProcessadorFactory.obterProcessador(FormaPagamento.CARTAO_CREDITO);
    assertEquals(pagamentoImediatoStrategy, processador);
  }

  @Test
  void deveRetornarPagamentoImediatoParaCartaoDebito() {
    ProcessadorPagamento processador =
        realProcessadorFactory.obterProcessador(FormaPagamento.CARTAO_DEBITO);
    assertEquals(pagamentoImediatoStrategy, processador);
  }

  @Test
  void deveRetornarPagamentoImediatoParaDinheiro() {
    ProcessadorPagamento processador =
        realProcessadorFactory.obterProcessador(FormaPagamento.DINHEIRO);
    assertEquals(pagamentoImediatoStrategy, processador);
  }

  @Test
  void deveRetornarPagamentoAguardandoParaPix() {
    ProcessadorPagamento processador = realProcessadorFactory.obterProcessador(FormaPagamento.PIX);
    assertEquals(pagamentoAguardandoStrategy, processador);
  }

  @Test
  void deveRetornarPagamentoAguardandoParaBoleto() {
    ProcessadorPagamento processador =
        realProcessadorFactory.obterProcessador(FormaPagamento.BOLETO);
    assertEquals(pagamentoAguardandoStrategy, processador);
  }

  @Test
  void pagamentoImediatoStrategyDeveProcessarCorretamente() {
    PagamentoImediatoStrategy strategy = new PagamentoImediatoStrategy();
    Pagamento pagamentoTeste = PagamentoDataHelper.criaPagamentoValido(pedido);

    strategy.processar(pagamentoTeste, pedido);

    assertEquals(StatusPagamento.PAGO, pagamentoTeste.getStatusPagamento());
    assertEquals(StatusPedido.PAGO, pedido.getStatus());
  }

  @Test
  void pagamentoAguardandoStrategyDeveProcessarCorretamente() {
    PagamentoAguardandoStrategy strategy = new PagamentoAguardandoStrategy();
    Pagamento pagamentoTeste = PagamentoDataHelper.criaPagamentoValido(pedido);

    strategy.processar(pagamentoTeste, pedido);

    assertEquals(StatusPagamento.AGUARDANDO, pagamentoTeste.getStatusPagamento());
    assertEquals(StatusPedido.AGUARDANDO_PAGAMENTO, pedido.getStatus());
  }

  @Test
  void deveListarTodosOsPagamentosSemFiltro() {
    PagamentoResumoResponse aguardando =
        new PagamentoResumoResponse(
            UUID.randomUUID(),
            UUID.randomUUID(),
            StatusPagamento.AGUARDANDO,
            LocalDateTime.now(),
            new BigDecimal("103.60"));
    PagamentoResumoResponse pago =
        new PagamentoResumoResponse(
            UUID.randomUUID(),
            UUID.randomUUID(),
            StatusPagamento.PAGO,
            LocalDateTime.now().minusMinutes(1),
            new BigDecimal("51.80"));
    Page<PagamentoResumoResponse> pagina =
        new PageImpl<>(List.of(aguardando, pago), PageRequest.of(0, 10), 2);
    when(pagamentoRepository.buscaPagamentos(isNull(), any(Pageable.class))).thenReturn(pagina);
    when(pagamentoRepository.somaValores(isNull())).thenReturn(new BigDecimal("155.40"));

    PagamentoPaginadoResponse response = pagamentoApplicationService.buscaPagamentos(null, 0, 10);

    assertNotNull(response);
    assertEquals(2, response.getPagamentos().size());
    assertEquals(2L, response.getTotalPagamentos());
    assertEquals(1, response.getTotalPaginas());
    assertEquals(0, response.getPaginaAtual());
    assertEquals(new BigDecimal("155.40"), response.getValorTotalPagamentos());
    assertEquals(StatusPagamento.AGUARDANDO, response.getPagamentos().get(0).getStatusPagamento());
    assertEquals(StatusPagamento.PAGO, response.getPagamentos().get(1).getStatusPagamento());

    ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
    verify(pagamentoRepository).buscaPagamentos(isNull(), captor.capture());
    assertEquals(0, captor.getValue().getPageNumber());
    assertEquals(10, captor.getValue().getPageSize());
    verify(pagamentoRepository).somaValores(isNull());
  }

  @Test
  void deveListarApenasPagamentosDoStatusInformado() {
    PagamentoResumoResponse pago =
        new PagamentoResumoResponse(
            UUID.randomUUID(),
            UUID.randomUUID(),
            StatusPagamento.PAGO,
            LocalDateTime.now(),
            new BigDecimal("51.80"));
    Page<PagamentoResumoResponse> pagina = new PageImpl<>(List.of(pago), PageRequest.of(0, 10), 1);
    when(pagamentoRepository.buscaPagamentos(eq(StatusPagamento.PAGO), any(Pageable.class)))
        .thenReturn(pagina);
    when(pagamentoRepository.somaValores(StatusPagamento.PAGO)).thenReturn(new BigDecimal("51.80"));

    PagamentoPaginadoResponse response =
        pagamentoApplicationService.buscaPagamentos(StatusPagamento.PAGO, 0, 10);

    assertNotNull(response);
    assertEquals(1, response.getPagamentos().size());
    assertEquals(1L, response.getTotalPagamentos());
    assertEquals(StatusPagamento.PAGO, response.getPagamentos().get(0).getStatusPagamento());
    assertEquals(new BigDecimal("51.80"), response.getValorTotalPagamentos());

    verify(pagamentoRepository).buscaPagamentos(eq(StatusPagamento.PAGO), any(Pageable.class));
    verify(pagamentoRepository).somaValores(StatusPagamento.PAGO);
  }
}
