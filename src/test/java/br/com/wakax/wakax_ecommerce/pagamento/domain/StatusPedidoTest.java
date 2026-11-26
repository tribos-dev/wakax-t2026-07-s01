package br.com.wakax.wakax_ecommerce.pagamento.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;

class StatusPedidoTest {

  @Test
  void devePermitirEstornoAPartirDePago() {
    assertTrue(StatusPedido.PAGO.podeTransicionarPara(StatusPedido.ESTORNADO));
  }

  @ParameterizedTest
  @EnumSource(value = StatusPedido.class, names = "PAGO", mode = EnumSource.Mode.EXCLUDE)
  void naoDevePermitirEstornoAPartirDeOutrosStatus(StatusPedido origem) {
    assertFalse(origem.podeTransicionarPara(StatusPedido.ESTORNADO));
  }

  @ParameterizedTest
  @EnumSource(StatusPedido.class)
  void estornadoDeveSerStatusFinal(StatusPedido destino) {
    assertFalse(StatusPedido.ESTORNADO.podeTransicionarPara(destino));
  }
}
