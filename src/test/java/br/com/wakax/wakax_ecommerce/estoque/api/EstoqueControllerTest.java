package br.com.wakax.wakax_ecommerce.estoque.api;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import br.com.wakax.wakax_ecommerce.auth.credencial.application.service.CredencialService;
import br.com.wakax.wakax_ecommerce.auth.security.service.AutenticacaoSecurityService;
import br.com.wakax.wakax_ecommerce.auth.security.service.TokenService;
import br.com.wakax.wakax_ecommerce.estoque.api.response.EstoqueListagemResponse;
import br.com.wakax.wakax_ecommerce.estoque.api.response.EstoqueResponse;
import br.com.wakax.wakax_ecommerce.estoque.application.service.EstoqueService;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.handler.MessageUtil;

@WebMvcTest(EstoqueController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(MessageUtil.class)
class EstoqueControllerTest {
  @Autowired private MockMvc mockMvc;
  @MockBean private EstoqueService estoqueService;
  @MockBean private TokenService tokenService;
  @MockBean private CredencialService credencialService;
  @MockBean private AutenticacaoSecurityService autenticacaoSecurityService;

  @Test
  void deveBuscarTodosEstoquesComParametrosPadrao() throws Exception {
    EstoqueResponse estoqueResponse =
        EstoqueResponse.builder()
            .id(UUID.randomUUID())
            .idProduto(UUID.randomUUID())
            .descricaoProduto("Produto A")
            .quantidadeDisponivel(10)
            .custoMedio(new BigDecimal("20.00"))
            .custoTotal(new BigDecimal("200.00"))
            .precoVenda(new BigDecimal("35.00"))
            .build();
    EstoqueListagemResponse response =
        EstoqueListagemResponse.builder()
            .estoques(List.of(estoqueResponse))
            .valorTotalInventario(new BigDecimal("200.00"))
            .pagina(0)
            .tamanho(20)
            .totalItens(1)
            .build();

    when(estoqueService.buscaTodosEstoques(false, false, 0, 20)).thenReturn(response);

    mockMvc
        .perform(get("/estoque"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.estoques").isArray())
        .andExpect(jsonPath("$.estoques[0].descricaoProduto").value("Produto A"))
        .andExpect(jsonPath("$.estoques[0].quantidadeDisponivel").value(10))
        .andExpect(jsonPath("$.valorTotalInventario").value(200.00))
        .andExpect(jsonPath("$.pagina").value(0))
        .andExpect(jsonPath("$.tamanho").value(20))
        .andExpect(jsonPath("$.totalItens").value(1));

    verify(estoqueService).buscaTodosEstoques(false, false, 0, 20);
  }

  @Test
  void deveBuscarTodosEstoquesComFiltroQuantidadeMinima() throws Exception {
    EstoqueResponse estoqueResponse =
        EstoqueResponse.builder()
            .id(UUID.randomUUID())
            .idProduto(UUID.randomUUID())
            .descricaoProduto("Produto A")
            .quantidadeDisponivel(10)
            .custoMedio(new BigDecimal("20.00"))
            .custoTotal(new BigDecimal("200.00"))
            .precoVenda(new BigDecimal("35.00"))
            .build();
    EstoqueListagemResponse response =
        EstoqueListagemResponse.builder()
            .estoques(List.of(estoqueResponse))
            .valorTotalInventario(new BigDecimal("200.00"))
            .pagina(0)
            .tamanho(20)
            .totalItens(1)
            .build();

    when(estoqueService.buscaTodosEstoques(true, false, 0, 20)).thenReturn(response);

    mockMvc
        .perform(get("/estoque").param("quantidadeMinima", "true"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.estoques").isArray())
        .andExpect(jsonPath("$.estoques[0].descricaoProduto").value("Produto A"))
        .andExpect(jsonPath("$.estoques[0].quantidadeDisponivel").value(10))
        .andExpect(jsonPath("$.valorTotalInventario").value(200.00))
        .andExpect(jsonPath("$.pagina").value(0))
        .andExpect(jsonPath("$.tamanho").value(20))
        .andExpect(jsonPath("$.totalItens").value(1));

    verify(estoqueService).buscaTodosEstoques(true, false, 0, 20);
  }

  @Test
  void deveBuscarTodosEstoquesComFiltroEmFalta() throws Exception {
    EstoqueResponse estoqueResponse =
        EstoqueResponse.builder()
            .id(UUID.randomUUID())
            .idProduto(UUID.randomUUID())
            .descricaoProduto("Produto A")
            .quantidadeDisponivel(10)
            .custoMedio(new BigDecimal("20.00"))
            .custoTotal(new BigDecimal("200.00"))
            .precoVenda(new BigDecimal("35.00"))
            .build();
    EstoqueListagemResponse response =
        EstoqueListagemResponse.builder()
            .estoques(List.of(estoqueResponse))
            .valorTotalInventario(new BigDecimal("200.00"))
            .pagina(0)
            .tamanho(20)
            .totalItens(1)
            .build();

    when(estoqueService.buscaTodosEstoques(false, true, 0, 20)).thenReturn(response);

    mockMvc
        .perform(get("/estoque").param("emFalta", "true"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.estoques").isArray())
        .andExpect(jsonPath("$.estoques[0].descricaoProduto").value("Produto A"))
        .andExpect(jsonPath("$.estoques[0].quantidadeDisponivel").value(10))
        .andExpect(jsonPath("$.valorTotalInventario").value(200.00))
        .andExpect(jsonPath("$.pagina").value(0))
        .andExpect(jsonPath("$.tamanho").value(20))
        .andExpect(jsonPath("$.totalItens").value(1));

    verify(estoqueService).buscaTodosEstoques(false, true, 0, 20);
  }

  @Test
  void deveRetornarBadRequestQuandoFiltrosForemMutuamenteExclusivos() throws Exception {
    when(estoqueService.buscaTodosEstoques(true, true, 0, 20))
        .thenThrow(
            new APIException(
                HttpStatus.BAD_REQUEST, ErrorCode.ESTOQUE_FILTROS_MUTUAMENTE_EXCLUSIVOS));

    mockMvc
        .perform(get("/estoque").param("quantidadeMinima", "true").param("emFalta", "true"))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.message").value("Utilize somente um filtro: quantidadeMinima ou emFalta."));

    verify(estoqueService).buscaTodosEstoques(true, true, 0, 20);
  }

  @Test
  void deveBuscarTodosEstoquesComPaginacao() throws Exception {
    EstoqueResponse estoqueResponse =
        EstoqueResponse.builder()
            .id(UUID.randomUUID())
            .idProduto(UUID.randomUUID())
            .descricaoProduto("Produto B")
            .quantidadeDisponivel(5)
            .custoMedio(new BigDecimal("12.00"))
            .custoTotal(new BigDecimal("60.00"))
            .precoVenda(new BigDecimal("20.00"))
            .build();
    EstoqueListagemResponse response =
        EstoqueListagemResponse.builder()
            .estoques(List.of(estoqueResponse))
            .valorTotalInventario(new BigDecimal("60.00"))
            .pagina(1)
            .tamanho(10)
            .totalItens(11)
            .build();

    when(estoqueService.buscaTodosEstoques(false, false, 1, 10)).thenReturn(response);

    mockMvc
        .perform(get("/estoque").param("pagina", "1").param("tamanho", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.estoques").isArray())
        .andExpect(jsonPath("$.estoques[0].descricaoProduto").value("Produto B"))
        .andExpect(jsonPath("$.valorTotalInventario").value(60.00))
        .andExpect(jsonPath("$.pagina").value(1))
        .andExpect(jsonPath("$.tamanho").value(10))
        .andExpect(jsonPath("$.totalItens").value(11));

    verify(estoqueService).buscaTodosEstoques(false, false, 1, 10);
  }
}
