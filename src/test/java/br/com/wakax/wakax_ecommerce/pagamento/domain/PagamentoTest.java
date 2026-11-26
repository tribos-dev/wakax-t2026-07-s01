package br.com.wakax.wakax_ecommerce.pagamento.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pagamento.application.service.PagamentoDataHelper;

class PagamentoTest {

  private Pagamento pagamento;

  @BeforeEach
  void setUp() {
    pagamento = PagamentoDataHelper.criaPagamentoValido(PagamentoDataHelper.criaPedidoValido());
  }

  @Test
  void deveEstornarPagamentoPagoRegistrandoMotivoEData() {
    pagamento.confirmarPagamento();

    pagamento.estornoPagamento("Cliente desistiu da compra");

    assertEquals(StatusPagamento.ESTORNO, pagamento.getStatusPagamento());
    assertEquals("Cliente desistiu da compra", pagamento.getMotivoEstorno());
    assertNotNull(pagamento.getDataEstorno());
  }

  @ParameterizedTest
  @EnumSource(value = StatusPagamento.class, names = "PAGO", mode = EnumSource.Mode.EXCLUDE)
  void naoDeveEstornarPagamentoQueNaoEstaPago(StatusPagamento status) {
    pagamento.setStatusPagamento(status);

    APIException exception =
        assertThrows(APIException.class, () -> pagamento.estornoPagamento("Motivo"));

    assertEquals(HttpStatus.CONFLICT, exception.getStatusException());
    assertEquals(ErrorCode.PAGAMENTO_NAO_ELEGIVEL_ESTORNO, exception.getErrorCode());
    assertEquals(status, pagamento.getStatusPagamento());
    assertNull(pagamento.getMotivoEstorno());
  }
}
