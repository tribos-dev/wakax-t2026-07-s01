package br.com.wakax.wakax_ecommerce.pagamento.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.PagamentoPedidoController;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.service.PagamentoDataHelper;
import br.com.wakax.wakax_ecommerce.pagamento.application.service.PagamentoService;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;

@ExtendWith(MockitoExtension.class)
class PagamentoPedidoControllerTest {

  @Mock private PagamentoService pagamentoService;

  @InjectMocks private PagamentoPedidoController pagamentoPedidoController;

  private UUID pedidoId;
  private PagamentoResponse pagamentoResponse;

  @BeforeEach
  void setUp() {
    pedidoId = UUID.randomUUID();
    Pedido pedido = PagamentoDataHelper.criaPedidoValido();
    pedido.setId(pedidoId);
    Pagamento pagamento = PagamentoDataHelper.criaPagamentoValido(pedido);
    pagamentoResponse = new PagamentoResponse(pagamento);
  }

  @Test
  void deveBuscarPagamentoPorPedidoIdComSucesso() {
    when(pagamentoService.buscaPagamentoPorPedidoId(pedidoId)).thenReturn(pagamentoResponse);

    PagamentoResponse response = pagamentoPedidoController.buscaPagamentoPorPedidoId(pedidoId);

    assertNotNull(response);
    assertEquals(pagamentoResponse.getPedidoId(), response.getPedidoId());
    assertEquals(pagamentoResponse.getFormaPagamento(), response.getFormaPagamento());

    verify(pagamentoService).buscaPagamentoPorPedidoId(pedidoId);
  }

  @Test
  void deveRepassarExcecaoDoService() {
    APIException exception =
        new APIException(
            HttpStatus.NOT_FOUND, ErrorCode.PAGAMENTO_DO_PEDIDO_NAO_ENCONTRADO, pedidoId);
    when(pagamentoService.buscaPagamentoPorPedidoId(pedidoId)).thenThrow(exception);

    APIException response =
        assertThrows(
            APIException.class,
            () -> pagamentoPedidoController.buscaPagamentoPorPedidoId(pedidoId));

    assertEquals(ErrorCode.PAGAMENTO_DO_PEDIDO_NAO_ENCONTRADO, response.getErrorCode());
    verify(pagamentoService).buscaPagamentoPorPedidoId(pedidoId);
  }
}
