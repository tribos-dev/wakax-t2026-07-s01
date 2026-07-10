package br.com.wakax.wakax_ecommerce.pagamento.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.wakax.wakax_ecommerce.pagamento.application.api.PagamentoController;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.request.PagamentoRequest;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.service.PagamentoDataHelper;
import br.com.wakax.wakax_ecommerce.pagamento.application.service.PagamentoService;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;

@ExtendWith(MockitoExtension.class)
class PagamentoControllerTest {

  @Mock private PagamentoService pagamentoService;

  @InjectMocks private PagamentoController pagamentoController;

  private PagamentoRequest pagamentoRequest;
  private PagamentoResponse pagamentoResponse;
  private UUID pedidoId;
  private UUID pagamentoId;

  @BeforeEach
  void setUp() {
    pedidoId = UUID.randomUUID();
    pagamentoId = UUID.randomUUID();

    pagamentoRequest = PagamentoDataHelper.criaPagamentoRequestValido(pedidoId);

    Pedido pedido = PagamentoDataHelper.criaPedidoValido();
    Pagamento pagamento = PagamentoDataHelper.criaPagamentoValido(pedido);
    pagamento.setId(pagamentoId);

    pagamentoResponse = new PagamentoResponse(pagamento);
  }

  @Test
  void deveCriarPagamentoComSucesso() {
    when(pagamentoService.processaPagamento(pagamentoRequest)).thenReturn(pagamentoResponse);

    PagamentoResponse response = pagamentoController.processaPagamento(pagamentoRequest);

    assertNotNull(response);
    assertEquals(pagamentoResponse.getIdPagamento(), response.getIdPagamento());
    assertEquals(pagamentoResponse.getPedidoId(), response.getPedidoId());
    assertEquals(pagamentoResponse.getStatusPagamento(), response.getStatusPagamento());

    verify(pagamentoService).processaPagamento(pagamentoRequest);
  }

  @Test
  void deveBuscarPagamentoPorIdComSucesso() {
    when(pagamentoService.buscaPagamentoPorId(pagamentoId)).thenReturn(pagamentoResponse);

    PagamentoResponse response = pagamentoController.buscaPagamentoPorId(pagamentoId);

    assertNotNull(response);
    assertEquals(pagamentoResponse.getIdPagamento(), response.getIdPagamento());
    assertEquals(pagamentoResponse.getPedidoId(), response.getPedidoId());

    verify(pagamentoService).buscaPagamentoPorId(pagamentoId);
  }

  @Test
  void deveRepassarExcecaoDoService() {
    RuntimeException exception = new RuntimeException("Erro no service");
    when(pagamentoService.processaPagamento(pagamentoRequest)).thenThrow(exception);

    assertThrows(
        RuntimeException.class, () -> pagamentoController.processaPagamento(pagamentoRequest));

    verify(pagamentoService).processaPagamento(pagamentoRequest);
  }

  @Test
  void deveRepassarExcecaoAoBuscarPorId() {
    RuntimeException exception = new RuntimeException("Pagamento não encontrado");
    when(pagamentoService.buscaPagamentoPorId(pagamentoId)).thenThrow(exception);

    assertThrows(
        RuntimeException.class, () -> pagamentoController.buscaPagamentoPorId(pagamentoId));

    verify(pagamentoService).buscaPagamentoPorId(pagamentoId);
  }
}
