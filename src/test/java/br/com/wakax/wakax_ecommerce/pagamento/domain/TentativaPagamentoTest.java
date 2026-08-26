package br.com.wakax.wakax_ecommerce.pagamento.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import br.com.wakax.wakax_ecommerce.pagamento.application.service.PagamentoDataHelper;

class TentativaPagamentoTest {

  @Test
  void deveCriarHistoricoPendenteComChaveIdempotente() {
    Pagamento pagamento = pagamentoNaSegundaTentativa();

    TentativaPagamento tentativa = TentativaPagamento.pendente(pagamento);

    assertEquals(pagamento, tentativa.getPagamento());
    assertEquals(2, tentativa.getNumeroTentativa());
    assertNotNull(tentativa.getDataTentativa());
    assertEquals(StatusTentativaPagamento.PENDENTE_ENVIO, tentativa.getStatus());
    assertEquals(
        "pagamento:" + pagamento.getId() + ":tentativa:2", tentativa.getChaveIdempotencia());
  }

  @Test
  void deveMarcarTentativaComoEnviadaELimparDetalheAnterior() {
    TentativaPagamento tentativa = TentativaPagamento.pendente(pagamentoNaSegundaTentativa());
    tentativa.marcarComoFalha("erro temporario");

    tentativa.marcarComoEnviada();

    assertEquals(StatusTentativaPagamento.ENVIADA, tentativa.getStatus());
    assertNull(tentativa.getDetalhe());
  }

  @Test
  void deveMarcarTentativaComoFalhaLimitandoDetalhe() {
    TentativaPagamento tentativa = TentativaPagamento.pendente(pagamentoNaSegundaTentativa());
    String detalheMaiorQueAColuna = "x".repeat(600);

    tentativa.marcarComoFalha(detalheMaiorQueAColuna);

    assertEquals(StatusTentativaPagamento.FALHA_ENVIO, tentativa.getStatus());
    assertEquals(500, tentativa.getDetalhe().length());
  }

  private Pagamento pagamentoNaSegundaTentativa() {
    Pagamento pagamento =
        PagamentoDataHelper.criaPagamentoValido(PagamentoDataHelper.criaPedidoValido());
    pagamento.setStatusPagamento(StatusPagamento.FALHOU);
    pagamento.iniciarReprocessamento();
    return pagamento;
  }
}
