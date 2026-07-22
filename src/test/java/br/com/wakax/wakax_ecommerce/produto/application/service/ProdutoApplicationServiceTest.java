package br.com.wakax.wakax_ecommerce.produto.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.produto.api.request.ProdutoRequest;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoAtivoResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoListResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoResponse;
import br.com.wakax.wakax_ecommerce.produto.application.repository.ProdutoRepository;
import br.com.wakax.wakax_ecommerce.produto.domain.Preco;
import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.ProdutoDisponivel;
import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;
import br.com.wakax.wakax_ecommerce.produto.domain.TipoPreco;

@ExtendWith(MockitoExtension.class)
class ProdutoApplicationServiceTest {

  @Mock private ProdutoRepository produtoRepository;

  @InjectMocks private ProdutoApplicationService produtoApplicationService;

  private ProdutoRequest produtoRequest;
  private UUID produtoId;

  @BeforeEach
  void setUp() {
    produtoId = UUID.randomUUID();

    produtoRequest =
        ProdutoRequest.builder()
            .descricao("Produto Teste")
            .pesoLiquido(new BigDecimal("1.5"))
            .pesoBruto(new BigDecimal("2.0"))
            .descricaoComplementar("Descrição complementar do produto")
            .preco(new BigDecimal("29.99"))
            .grupo("Eletrônicos")
            .unidade("UN")
            .estoqueMinimo(10)
            .estoqueMaximo(100)
            .precos(Collections.emptyList())
            .build();
  }

  private void mockProdutoRepositorySalvaComId() {
    when(produtoRepository.salva(any(Produto.class)))
        .thenAnswer(
            (Answer<Produto>)
                invocation -> {
                  Produto produto = invocation.getArgument(0);
                  produto.setId(produtoId);
                  return produto;
                });
  }

  @Test
  void deveCadastrarProdutoComSucesso() {
    mockProdutoRepositorySalvaComId();

    ProdutoResponse response = produtoApplicationService.cadastraProduto(produtoRequest);

    assertNotNull(response);
    assertEquals(produtoId, response.getIdProduto());
    assertEquals(produtoRequest.getDescricao(), response.getDescricao());

    verify(produtoRepository, times(1)).salva(any(Produto.class));
  }

  @Test
  void deveLancarExcecaoQuandoProdutoDuplicado() {
    when(produtoRepository.salva(any(Produto.class)))
        .thenThrow(
            new APIException(
                org.springframework.http.HttpStatus.CONFLICT,
                ErrorCode.PRODUTO_DUPLICADO,
                produtoRequest.getDescricao()));

    APIException exception =
        assertThrows(
            APIException.class, () -> produtoApplicationService.cadastraProduto(produtoRequest));

    assertEquals(ErrorCode.PRODUTO_DUPLICADO, exception.getErrorCode());
    assertEquals(produtoRequest.getDescricao(), exception.getArgs()[0]);

    verify(produtoRepository, times(1)).salva(any(Produto.class));
  }

  @Test
  void deveBuscarProdutoPorIdComSucesso() {
    Produto produto = new Produto(produtoRequest);
    produto.setId(produtoId);

    when(produtoRepository.buscaProdutoPorId(produtoId)).thenReturn(produto);

    ProdutoListResponse response = produtoApplicationService.buscaProdutoPorId(produtoId);

    assertNotNull(response);
    assertEquals(produtoId, response.getIdProduto());
    assertEquals(produtoRequest.getDescricao(), response.getDescricao());
    assertEquals(StatusProduto.ATIVO, response.getStatus());
    assertEquals(produtoRequest.getPesoLiquido(), response.getPesoLiquido());
    assertEquals(produtoRequest.getPesoBruto(), response.getPesoBruto());
    assertEquals(produtoRequest.getDescricaoComplementar(), response.getDescricaoComplementar());
    assertEquals(produtoRequest.getGrupo(), response.getGrupo());
    assertEquals(produtoRequest.getUnidade(), response.getUnidade());
    assertEquals(produtoRequest.getEstoqueMinimo(), response.getEstoqueMinimo());
    assertEquals(produtoRequest.getEstoqueMaximo(), response.getEstoqueMaximo());

    verify(produtoRepository, times(1)).buscaProdutoPorId(produtoId);
  }

  @Test
  void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
    when(produtoRepository.buscaProdutoPorId(produtoId))
        .thenThrow(
            new APIException(
                org.springframework.http.HttpStatus.NOT_FOUND,
                ErrorCode.PRODUTO_NAO_ENCONTRADO,
                produtoId));

    APIException exception =
        assertThrows(
            APIException.class, () -> produtoApplicationService.buscaProdutoPorId(produtoId));

    assertEquals(ErrorCode.PRODUTO_NAO_ENCONTRADO, exception.getErrorCode());
    assertEquals(produtoId, exception.getArgs()[0]);

    verify(produtoRepository, times(1)).buscaProdutoPorId(produtoId);
  }

  @Test
  void deveCriarProdutoComPrecoCorreto() {
    mockProdutoRepositorySalvaComId();

    ProdutoResponse response = produtoApplicationService.cadastraProduto(produtoRequest);

    assertNotNull(response);
    assertEquals(produtoId, response.getIdProduto());
    assertEquals(produtoRequest.getDescricao(), response.getDescricao());

    verify(produtoRepository, times(1)).salva(any(Produto.class));
  }

  @Test
  void deveCadastrarProdutoComCamposOpcionaisNulos() {
    ProdutoRequest requestComCamposNulos =
        ProdutoRequest.builder()
            .descricao("Produto Mínimo")
            .pesoLiquido(new BigDecimal("1.0"))
            .pesoBruto(new BigDecimal("1.5"))
            .preco(new BigDecimal("19.99"))
            .precos(Collections.emptyList())
            .build();

    mockProdutoRepositorySalvaComId();

    ProdutoResponse response = produtoApplicationService.cadastraProduto(requestComCamposNulos);

    assertNotNull(response);
    assertEquals(produtoId, response.getIdProduto());
    assertEquals(requestComCamposNulos.getDescricao(), response.getDescricao());

    verify(produtoRepository, times(1)).salva(any(Produto.class));
  }

  @Test
  void deveCadastrarProdutoComListaDePrecosNula() {
    ProdutoRequest requestComPrecosNulos =
        ProdutoRequest.builder()
            .descricao("Produto sem Preços")
            .pesoLiquido(new BigDecimal("1.0"))
            .pesoBruto(new BigDecimal("1.5"))
            .preco(new BigDecimal("19.99"))
            .precos(null)
            .build();

    mockProdutoRepositorySalvaComId();

    assertThrows(
        NullPointerException.class,
        () -> produtoApplicationService.cadastraProduto(requestComPrecosNulos));

    verify(produtoRepository, times(1)).salva(any(Produto.class));
  }

  @Test
  void deveListarProdutosAtivosComEstoqueDisponivel() {
    Produto produtoAtivo = criaProdutoAtivoComPreco();
    ProdutoDisponivel produtoDisponivel = new ProdutoDisponivel(produtoAtivo, 10);

    when(produtoRepository.listaProdutosAtivosComEstoque()).thenReturn(List.of(produtoDisponivel));

    List<ProdutoAtivoResponse> response = produtoApplicationService.listaProdutosAtivos();

    assertNotNull(response);
    assertEquals(1, response.size());

    ProdutoAtivoResponse produtoResponse = response.get(0);

    assertEquals(produtoAtivo.getId(), produtoResponse.getIdProduto());
    assertEquals("Arroz Branco", produtoResponse.getDescricao());
    assertEquals("Arroz branco tipo 1", produtoResponse.getDescricaoResumida());
    assertEquals(StatusProduto.ATIVO, produtoResponse.getStatus());
    assertEquals("Alimentos", produtoResponse.getGrupo());
    assertEquals(0, new BigDecimal("29.90").compareTo(produtoResponse.getPreco()));
    assertEquals(10, produtoResponse.getQuantidadeDisponivel());

    verify(produtoRepository, times(1)).listaProdutosAtivosComEstoque();
  }

  @Test
  void deveRetornarListaVaziaQuandoNaoExistiremProdutosAtivosComEstoque() {
    when(produtoRepository.listaProdutosAtivosComEstoque()).thenReturn(Collections.emptyList());

    List<ProdutoAtivoResponse> response = produtoApplicationService.listaProdutosAtivos();

    assertNotNull(response);
    assertTrue(response.isEmpty());

    verify(produtoRepository, times(1)).listaProdutosAtivosComEstoque();
  }

  private Produto criaProdutoAtivoComPreco() {
    Produto produto =
        Produto.builder()
            .id(UUID.randomUUID())
            .descricao("Arroz Branco")
            .status(StatusProduto.ATIVO)
            .pesoLiquido(new BigDecimal("5.00"))
            .pesoBruto(new BigDecimal("5.10"))
            .descricaoComplementar("Arroz branco tipo 1")
            .grupo("Alimentos")
            .unidade("UN")
            .estoqueMinimo(5)
            .estoqueMaximo(100)
            .build();

    Preco preco =
        Preco.builder()
            .id(UUID.randomUUID())
            .tipo(TipoPreco.PADRAO)
            .valor(new BigDecimal("29.90"))
            .produto(produto)
            .build();

    produto.setPrecos(List.of(preco));

    return produto;
  }
}
