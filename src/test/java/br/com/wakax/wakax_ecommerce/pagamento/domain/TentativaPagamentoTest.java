package br.com.wakax.wakax_ecommerce.pagamento.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import br.com.wakax.wakax_ecommerce.pagamento.application.service.PagamentoDataHelper;

class TentativaPagamentoTest {

  @Test
  void deveCriarTentativaComNumeroEDataDeRegistro() {
    Pagamento pagamento =
        PagamentoDataHelper.criaPagamentoValido(PagamentoDataHelper.criaPedidoValido());

    TentativaPagamento tentativa = TentativaPagamento.nova(pagamento, 1);

    assertEquals(pagamento, tentativa.getPagamento());
    assertEquals(1, tentativa.getNumeroTentativa());
    assertNotNull(tentativa.getDataTentativa());
  }

  @Test
  void deveLimitarEmTresTentativasDeReprocessamento() {
    assertEquals(3, TentativaPagamento.LIMITE_TENTATIVAS);
  }
}
