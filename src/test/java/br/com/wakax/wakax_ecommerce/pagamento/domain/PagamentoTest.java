package br.com.wakax.wakax_ecommerce.pagamento.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import br.com.wakax.wakax_ecommerce.pagamento.application.service.PagamentoDataHelper;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;

class PagamentoTest {

  @Test
  void deveIniciarPagamentoNaPrimeiraTentativa() {
    Pedido pedido = PagamentoDataHelper.criaPedidoValido();

    Pagamento pagamento = new Pagamento(pedido);

    assertEquals(1, pagamento.getNumeroTentativas());
  }

  @Test
  void deveIniciarPagamentoNaPrimeiraTentativaAoUsarBuilder() {
    Pagamento pagamento =
        PagamentoDataHelper.criaPagamentoValido(PagamentoDataHelper.criaPedidoValido());

    assertEquals(1, pagamento.getNumeroTentativas());
  }

  @Test
  void deveIniciarReprocessamentoIncrementandoTentativa() {
    Pagamento pagamento = pagamentoComFalha(1);

    pagamento.iniciarReprocessamento();

    assertEquals(2, pagamento.getNumeroTentativas());
    assertEquals(StatusPagamento.AGUARDANDO, pagamento.getStatusPagamento());
  }

  @Test
  void deveRegistrarFalhaDepoisDoEnvioAoGateway() {
    Pagamento pagamento = pagamentoComFalha(1);
    pagamento.iniciarReprocessamento();

    pagamento.registrarFalha();

    assertEquals(2, pagamento.getNumeroTentativas());
    assertEquals(StatusPagamento.FALHOU, pagamento.getStatusPagamento());
  }

  @Test
  void deveIdentificarLimiteDeTresTentativas() {
    Pagamento pagamento = pagamentoComFalha(Pagamento.LIMITE_TENTATIVAS);

    assertTrue(pagamento.atingiuLimiteTentativas());
  }

  @Test
  void naoDeveIdentificarLimiteAntesDeTresTentativas() {
    Pagamento pagamento = pagamentoComFalha(Pagamento.LIMITE_TENTATIVAS - 1);

    assertFalse(pagamento.atingiuLimiteTentativas());
  }

  private Pagamento pagamentoComFalha(int numeroTentativas) {
    Pagamento pagamento =
        PagamentoDataHelper.criaPagamentoValido(PagamentoDataHelper.criaPedidoValido());
    pagamento.setStatusPagamento(StatusPagamento.FALHOU);
    pagamento.setNumeroTentativas(numeroTentativas);
    return pagamento;
  }
}
