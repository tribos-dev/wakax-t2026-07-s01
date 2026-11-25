package br.com.wakax.wakax_ecommerce.estoque.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;

class EstoqueTest {

  @Test
  void deveRecalcularCustoMedioAoAdicionarQuantidade() {
    Estoque estoque = criaEstoque(10, "5.00", "50.00");

    estoque.adicionaQuantidade(10, new BigDecimal("7.00"));

    assertEquals(20, estoque.getQuantidadeDisponivel());
    assertEquals(new BigDecimal("6.00"), estoque.getCustoMedio());
    assertEquals(new BigDecimal("120.00"), estoque.getCustoTotal());
  }

  @Test
  void deveUsarCustoInformadoNaPrimeiraEntradaDeEstoqueZerado() {
    Estoque estoque = criaEstoque(0, "0.00", "0.00");

    estoque.adicionaQuantidade(5, new BigDecimal("8.00"));

    assertEquals(5, estoque.getQuantidadeDisponivel());
    assertEquals(new BigDecimal("8.00"), estoque.getCustoMedio());
    assertEquals(new BigDecimal("40.00"), estoque.getCustoTotal());
  }

  @Test
  void deveLancarErroQuandoQuantidadeForZeroOuNegativa() {
    Estoque estoque = criaEstoque(10, "5.00", "50.00");
    BigDecimal custo = new BigDecimal("7.00");

    APIException erroZero =
        assertThrows(APIException.class, () -> estoque.adicionaQuantidade(0, custo));
    APIException erroNegativo =
        assertThrows(APIException.class, () -> estoque.adicionaQuantidade(-5, custo));

    assertEquals(ErrorCode.QUANTIDADE_INVALIDA, erroZero.getErrorCode());
    assertEquals(ErrorCode.QUANTIDADE_INVALIDA, erroNegativo.getErrorCode());
    assertEquals(10, estoque.getQuantidadeDisponivel());
    assertEquals(new BigDecimal("50.00"), estoque.getCustoTotal());
  }

  @Test
  void deveLancarErroQuandoCustoForNegativo() {
    Estoque estoque = criaEstoque(10, "5.00", "50.00");
    BigDecimal custoNegativo = new BigDecimal("-1.00");

    APIException erro =
        assertThrows(APIException.class, () -> estoque.adicionaQuantidade(10, custoNegativo));

    assertEquals(ErrorCode.CUSTO_INVALIDO, erro.getErrorCode());
    assertEquals(10, estoque.getQuantidadeDisponivel());
    assertEquals(new BigDecimal("50.00"), estoque.getCustoTotal());
  }

  private Estoque criaEstoque(int quantidade, String custoMedio, String custoTotal) {
    return Estoque.builder()
        .quantidadeDisponivel(quantidade)
        .custoMedio(new BigDecimal(custoMedio))
        .custoTotal(new BigDecimal(custoTotal))
        .build();
  }
}
