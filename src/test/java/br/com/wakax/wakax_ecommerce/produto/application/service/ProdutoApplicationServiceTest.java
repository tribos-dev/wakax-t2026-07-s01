package br.com.wakax.wakax_ecommerce.produto.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.wakax.wakax_ecommerce.auth.credencial.domain.Credencial;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.produto.api.request.PrecoUpdateRequest;
import br.com.wakax.wakax_ecommerce.produto.api.request.ProdutoRequest;
import br.com.wakax.wakax_ecommerce.produto.api.response.PrecoResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoListResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoPaginadoResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoResumoResponse;
import br.com.wakax.wakax_ecommerce.produto.application.repository.ProdutoRepository;
import br.com.wakax.wakax_ecommerce.produto.domain.HistoricoPreco;
import br.com.wakax.wakax_ecommerce.produto.domain.Preco;
import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;
import br.com.wakax.wakax_ecommerce.produto.domain.TipoPreco;

@ExtendWith(MockitoExtension.class)
class ProdutoApplicationServiceTest {

  @Mock private ProdutoRepository produtoRepository;

  @InjectMocks private ProdutoApplicationService produtoApplicationService;

  private ProdutoRequest produtoRequest;
  private Produto produto;
  private UUID produtoId;

  @AfterEach
  void limpaContextoDeSeguranca() {
    SecurityContextHolder.clearContext();
  }

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
                  Produto p = invocation.getArgument(0);
                  p.setId(produtoId);
                  return p;
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
            APIException.class,
            () -> {
              produtoApplicationService.cadastraProduto(produtoRequest);
            });

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
            APIException.class,
            () -> {
              produtoApplicationService.buscaProdutoPorId(produtoId);
            });

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
        () -> {
          produtoApplicationService.cadastraProduto(requestComPrecosNulos);
        });

    verify(produtoRepository, times(1)).salva(any(Produto.class));
  }

  // Teste BDD task WX-298
  private Produto criaProduto(String descricao, StatusProduto status, String precoPadrao) {
    Produto produto =
        Produto.builder()
            .id(UUID.randomUUID())
            .descricao(descricao)
            .status(status)
            .pesoLiquido(new BigDecimal("1.0"))
            .pesoBruto(new BigDecimal("1.5"))
            .dataCriacao(LocalDateTime.now())
            .build();
    produto.setPrecos(List.of(new Preco(TipoPreco.PADRAO, new BigDecimal(precoPadrao), produto)));
    return produto;
  }

  // Cenario 1: Listar produtos com sucesso
  @Test
  void deveListarTodosOsProdutosComSucesso() {
    Produto arroz = criaProduto("Arroz", StatusProduto.ATIVO, "10.00");
    Produto feijao = criaProduto("Feijao", StatusProduto.INATIVO, "20.00");
    Pageable pageable = PageRequest.of(0, 10, Sort.by("descricao"));
    Page<Produto> pagina = new PageImpl<>(List.of(arroz, feijao), pageable, 2);
    when(produtoRepository.listaTodos(any(Pageable.class))).thenReturn(pagina);

    ProdutoPaginadoResponse response = produtoApplicationService.listaProduto(0, 10);

    assertNotNull(response);
    assertEquals(2, response.getProdutos().size());
    assertEquals(2L, response.getTotal());
    assertEquals(0, response.getPagina());
    assertEquals(1, response.getTotalPaginas());

    // Regra: mostrar descricao, status, preco atual e data de cadastro
    ProdutoResumoResponse primeiro = response.getProdutos().get(0);
    assertEquals("Arroz", primeiro.getDescricao());
    assertEquals(StatusProduto.ATIVO, primeiro.getStatus());
    assertEquals(new BigDecimal("10.00"), primeiro.getPrecoAtual());
    assertNotNull(primeiro.getDataCadastro());

    // Regra: retornar produtos independente do status
    assertEquals(StatusProduto.INATIVO, response.getProdutos().get(1).getStatus());

    verify(produtoRepository, times(1)).listaTodos(any(Pageable.class));
  }

  // Regra: ordenar por descricao
  @Test
  void deveSolicitarOrdenacaoPorDescricaoComPaginacaoCorreta() {
    when(produtoRepository.listaTodos(any(Pageable.class)))
        .thenReturn(new PageImpl<>(Collections.emptyList()));

    produtoApplicationService.listaProduto(2, 15);

    ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
    verify(produtoRepository).listaTodos(captor.capture());
    Pageable pageableUsado = captor.getValue();

    assertEquals(2, pageableUsado.getPageNumber());
    assertEquals(15, pageableUsado.getPageSize());
    Sort.Order ordemPorDescricao = pageableUsado.getSort().getOrderFor("descricao");
    assertNotNull(ordemPorDescricao);
    assertTrue(ordemPorDescricao.isAscending());
  }

  // Cenario 2: Sistema sem produtos
  @Test
  void deveRetornarListaVaziaQuandoNaoHaProdutos() {
    Pageable pageable = PageRequest.of(0, 10, Sort.by("descricao"));
    Page<Produto> paginaVazia = new PageImpl<>(Collections.emptyList(), pageable, 0);
    when(produtoRepository.listaTodos(any(Pageable.class))).thenReturn(paginaVazia);

    ProdutoPaginadoResponse response = produtoApplicationService.listaProduto(0, 10);

    assertNotNull(response);
    assertTrue(response.getProdutos().isEmpty());
    assertEquals(0L, response.getTotal());
    assertEquals(0, response.getTotalPaginas());
    verify(produtoRepository, times(1)).listaTodos(any(Pageable.class));
  }

  // Teste BDD task WX-28
  private Produto criaProdutoComPreco(TipoPreco tipo, BigDecimal valor) {
    Produto produtoComPreco =
        Produto.builder()
            .id(UUID.randomUUID())
            .descricao("Produto Teste Preco")
            .status(StatusProduto.ATIVO)
            .pesoLiquido(new BigDecimal("1.0"))
            .pesoBruto(new BigDecimal("1.5"))
            .dataCriacao(LocalDateTime.now())
            .build();

    Preco preco =
        Preco.builder()
            .id(UUID.randomUUID())
            .tipo(tipo)
            .valor(valor)
            .produto(produtoComPreco)
            .build();

    produtoComPreco.setPrecos(new ArrayList<>(List.of(preco)));
    return produtoComPreco;
  }

  private void autenticaComo(String usuario) {
    Credencial credencial = Credencial.builder().usuario(usuario).build();
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken(credencial, null));
  }

  // Cenario 1: Atualizar preco com sucesso
  @Test
  void deveAtualizarPrecoComSucesso() {
    Produto produtoComPreco = criaProdutoComPreco(TipoPreco.PADRAO, new BigDecimal("100.00"));
    UUID idDoProduto = produtoComPreco.getId();
    autenticaComo("gestor.comercial@teste.com");

    PrecoUpdateRequest request =
        PrecoUpdateRequest.builder()
            .tipo(TipoPreco.PADRAO)
            .valor(new BigDecimal("120.00"))
            .motivo("Ajuste de mercado")
            .build();

    when(produtoRepository.buscaProdutoPorId(idDoProduto)).thenReturn(produtoComPreco);
    when(produtoRepository.salva(produtoComPreco)).thenReturn(produtoComPreco);

    PrecoResponse response = produtoApplicationService.atualizaPreco(idDoProduto, request);

    // novo preco e salvo
    assertNotNull(response);
    assertEquals(new BigDecimal("120.00"), response.getValor());
    assertEquals(TipoPreco.PADRAO, response.getTipo());
    Preco precoAtualizado = produtoComPreco.getPrecos().get(0);
    assertEquals(new BigDecimal("120.00"), precoAtualizado.getValor());
    verify(produtoRepository, times(1)).salva(produtoComPreco);

    // historico de precos e atualizado
    assertEquals(1, precoAtualizado.getHistorico().size());
    HistoricoPreco evento = precoAtualizado.getHistorico().get(0);
    assertEquals(new BigDecimal("100.00"), evento.getValorDe());
    assertEquals(new BigDecimal("120.00"), evento.getValorPara());
    assertEquals(TipoPreco.PADRAO, evento.getTipo());
    assertEquals("Ajuste de mercado", evento.getMotivo());
    assertEquals("gestor.comercial@teste.com", evento.getUsuario());

    // recebo confirmacao com novo preco
    assertEquals(precoAtualizado.getId(), response.getId());
  }

  // Regra: apenas precos existentes podem ser atualizados
  @Test
  void deveLancarExcecaoQuandoTipoDePrecoNaoExisteNoProduto() {
    Produto produtoComPreco = criaProdutoComPreco(TipoPreco.PADRAO, new BigDecimal("100.00"));
    UUID idDoProduto = produtoComPreco.getId();

    PrecoUpdateRequest request =
        PrecoUpdateRequest.builder()
            .tipo(TipoPreco.PROMOCIONAL)
            .valor(new BigDecimal("80.00"))
            .motivo("Campanha")
            .build();

    when(produtoRepository.buscaProdutoPorId(idDoProduto)).thenReturn(produtoComPreco);

    APIException exception =
        assertThrows(
            APIException.class,
            () -> produtoApplicationService.atualizaPreco(idDoProduto, request));

    assertEquals(ErrorCode.PRECO_NAO_ENCONTRADO, exception.getErrorCode());
    verify(produtoRepository, never()).salva(any(Produto.class));
  }

  // Cenario 2: Falha por preco invalido
  @Test
  void deveFalharValidacaoQuandoPrecoForZeroOuNegativo() {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    Produto produtoComPreco = criaProdutoComPreco(TipoPreco.PADRAO, new BigDecimal("100.00"));
    Preco precoOriginal = produtoComPreco.getPrecos().get(0);

    PrecoUpdateRequest precoZero =
        PrecoUpdateRequest.builder()
            .tipo(TipoPreco.PADRAO)
            .valor(BigDecimal.ZERO)
            .motivo("Tentativa invalida")
            .build();

    PrecoUpdateRequest precoNegativo =
        PrecoUpdateRequest.builder()
            .tipo(TipoPreco.PADRAO)
            .valor(new BigDecimal("-10.00"))
            .motivo("Tentativa invalida")
            .build();

    // recebo erro de validacao
    Set<ConstraintViolation<PrecoUpdateRequest>> violacoesZero = validator.validate(precoZero);
    Set<ConstraintViolation<PrecoUpdateRequest>> violacoesNegativo =
        validator.validate(precoNegativo);

    assertFalse(violacoesZero.isEmpty());
    assertFalse(violacoesNegativo.isEmpty());
    assertTrue(
        violacoesZero.stream()
            .anyMatch(violacao -> violacao.getPropertyPath().toString().equals("valor")));

    // preco original e mantido (o service nunca chega a ser acionado)
    assertEquals(new BigDecimal("100.00"), precoOriginal.getValor());
    verifyNoInteractions(produtoRepository);
  }
}
