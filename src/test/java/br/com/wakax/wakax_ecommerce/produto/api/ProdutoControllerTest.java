package br.com.wakax.wakax_ecommerce.produto.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.wakax.wakax_ecommerce.produto.api.request.PrecoRequest;
import br.com.wakax.wakax_ecommerce.produto.api.request.ProdutoRequest;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoAtivoPaginadoResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoListResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoResponse;
import br.com.wakax.wakax_ecommerce.produto.application.service.ProdutoService;

@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {

  @Mock private ProdutoService produtoService;

  @InjectMocks private ProdutoController produtoController;

  private ProdutoRequest produtoRequest;
  private ProdutoResponse produtoResponse;
  private ProdutoListResponse produtoListResponse;
  private UUID produtoId;

  @BeforeEach
  void setUp() {
    produtoId = UUID.randomUUID();
    PrecoRequest precoRequest = new PrecoRequest();
    produtoRequest =
        ProdutoRequest.builder()
            .descricao("Produto Teste")
            .pesoLiquido(new BigDecimal("1.5"))
            .pesoBruto(new BigDecimal("2.0"))
            .descricaoComplementar("Descrição complementar")
            .preco(new BigDecimal("29.99"))
            .grupo("Eletrônicos")
            .unidade("UN")
            .estoqueMinimo(10)
            .estoqueMaximo(100)
            .precos(Collections.emptyList())
            .build();

    produtoResponse = mock(ProdutoResponse.class);
    produtoListResponse = mock(ProdutoListResponse.class);
  }

  @Test
  void deveCadastrarProdutoComSucesso() {
    when(produtoService.cadastraProduto(any(ProdutoRequest.class))).thenReturn(produtoResponse);
    ProdutoResponse response = produtoController.cadastraProduto(produtoRequest);
    assertNotNull(response);
    verify(produtoService, times(1)).cadastraProduto(produtoRequest);
  }

  @Test
  void deveBuscarProdutoPorIdComSucesso() {
    when(produtoService.buscaProdutoPorId(any(UUID.class))).thenReturn(produtoListResponse);
    ProdutoListResponse response = produtoController.buscaProdutoPorId(produtoId);
    assertNotNull(response);
    verify(produtoService, times(1)).buscaProdutoPorId(produtoId);
  }

  @Test
  void deveDelegarListagemDeProdutosAtivosComPaginacao() {
    ProdutoAtivoPaginadoResponse responseEsperada = mock(ProdutoAtivoPaginadoResponse.class);
    when(produtoService.listarProdutosAtivos(2, 15)).thenReturn(responseEsperada);

    ProdutoAtivoPaginadoResponse response = produtoController.listarProdutosAtivos(2, 15);

    assertSame(responseEsperada, response);
    verify(produtoService).listarProdutosAtivos(2, 15);
  }
}
