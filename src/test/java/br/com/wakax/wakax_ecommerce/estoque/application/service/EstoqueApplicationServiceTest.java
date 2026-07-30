package br.com.wakax.wakax_ecommerce.estoque.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import br.com.wakax.wakax_ecommerce.estoque.api.response.EstoqueListagemResponse;
import br.com.wakax.wakax_ecommerce.estoque.application.repository.EstoqueRepository;
import br.com.wakax.wakax_ecommerce.estoque.domain.Estoque;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.produto.application.repository.ProdutoRepository;
import br.com.wakax.wakax_ecommerce.produto.domain.Preco;
import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;
import br.com.wakax.wakax_ecommerce.produto.domain.TipoPreco;

@ExtendWith(MockitoExtension.class)
class EstoqueApplicationServiceTest {

  @Mock private EstoqueRepository estoqueRepository;

  @Mock private ProdutoRepository produtoRepository;

  @InjectMocks private EstoqueApplicationService estoqueApplicationService;

  @Test
  void deveLancarBadRequestQuandoFiltrosForemMutuamenteExclusivos() {
    APIException exception =
        assertThrows(
            APIException.class,
            () -> estoqueApplicationService.buscaTodosEstoques(true, true, 0, 20));

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusException());
    assertEquals(ErrorCode.ESTOQUE_FILTROS_MUTUAMENTE_EXCLUSIVOS, exception.getErrorCode());
    verifyNoInteractions(estoqueRepository);
    verifyNoInteractions(produtoRepository);
  }

  @Test
  void deveBuscarTodosEstoquesComParametrosPadrao() {
    Pageable pageable = PageRequest.of(0, 20);
    Estoque primeiroEstoque = criaEstoque("Produto A", 10, "20.00", "200.00", "35.00");
    Estoque segundoEstoque = criaEstoque("Produto B", 5, "12.00", "60.00", "20.00");
    Page<Estoque> pagina = new PageImpl<>(List.of(primeiroEstoque, segundoEstoque), pageable, 2);
    BigDecimal valorTotalInventario = new BigDecimal("1000.00");

    when(estoqueRepository.buscaTodosEstoques(false, false, pageable)).thenReturn(pagina);
    when(estoqueRepository.calculaValorTotalInventario(false, false))
        .thenReturn(valorTotalInventario);

    EstoqueListagemResponse response =
        estoqueApplicationService.buscaTodosEstoques(false, false, 0, 20);

    assertNotNull(response);
    assertEquals(2, response.getEstoques().size());
    assertEquals(valorTotalInventario, response.getValorTotalInventario());
    assertEquals(0, response.getPagina());
    assertEquals(20, response.getTamanho());
    assertEquals(2, response.getTotalItens());
    assertEquals(primeiroEstoque.getId(), response.getEstoques().get(0).getId());
    assertEquals(
        primeiroEstoque.getProduto().getId(), response.getEstoques().get(0).getIdProduto());
    assertEquals("Produto A", response.getEstoques().get(0).getDescricaoProduto());
    assertEquals(10, response.getEstoques().get(0).getQuantidadeDisponivel());
    assertEquals(new BigDecimal("20.00"), response.getEstoques().get(0).getCustoMedio());
    assertEquals(new BigDecimal("200.00"), response.getEstoques().get(0).getCustoTotal());
    assertEquals(new BigDecimal("35.00"), response.getEstoques().get(0).getPrecoVenda());

    verify(estoqueRepository).buscaTodosEstoques(false, false, pageable);
    verify(estoqueRepository).calculaValorTotalInventario(false, false);
    verifyNoInteractions(produtoRepository);
  }

  @Test
  void deveRetornarListaVaziaComValorTotalZero() {
    Pageable pageable = PageRequest.of(0, 20);
    Page<Estoque> pagina = new PageImpl<>(List.of(), pageable, 0);

    when(estoqueRepository.buscaTodosEstoques(false, false, pageable)).thenReturn(pagina);
    when(estoqueRepository.calculaValorTotalInventario(false, false)).thenReturn(BigDecimal.ZERO);

    EstoqueListagemResponse response =
        estoqueApplicationService.buscaTodosEstoques(false, false, 0, 20);

    assertTrue(response.getEstoques().isEmpty());
    assertEquals(BigDecimal.ZERO, response.getValorTotalInventario());
    assertEquals(0, response.getPagina());
    assertEquals(20, response.getTamanho());
    assertEquals(0, response.getTotalItens());
    verify(estoqueRepository).buscaTodosEstoques(false, false, pageable);
    verify(estoqueRepository).calculaValorTotalInventario(false, false);
  }

  @Test
  void deveBuscarEstoquesComFiltroQuantidadeMinima() {
    Pageable pageable = PageRequest.of(0, 20);
    Page<Estoque> pagina =
        new PageImpl<>(
            List.of(criaEstoque("Produto A", 5, "10.00", "50.00", "15.00")), pageable, 1);

    when(estoqueRepository.buscaTodosEstoques(true, false, pageable)).thenReturn(pagina);
    when(estoqueRepository.calculaValorTotalInventario(true, false))
        .thenReturn(new BigDecimal("50.00"));

    EstoqueListagemResponse response =
        estoqueApplicationService.buscaTodosEstoques(true, false, 0, 20);

    assertEquals(1, response.getEstoques().size());
    assertEquals(new BigDecimal("50.00"), response.getValorTotalInventario());
    verify(estoqueRepository).buscaTodosEstoques(true, false, pageable);
    verify(estoqueRepository).calculaValorTotalInventario(true, false);
  }

  @Test
  void deveBuscarEstoquesComFiltroEmFalta() {
    Pageable pageable = PageRequest.of(0, 20);
    Page<Estoque> pagina =
        new PageImpl<>(List.of(criaEstoque("Produto C", 0, "0.00", "0.00", "30.00")), pageable, 1);

    when(estoqueRepository.buscaTodosEstoques(false, true, pageable)).thenReturn(pagina);
    when(estoqueRepository.calculaValorTotalInventario(false, true)).thenReturn(BigDecimal.ZERO);

    EstoqueListagemResponse response =
        estoqueApplicationService.buscaTodosEstoques(false, true, 0, 20);

    assertEquals(1, response.getEstoques().size());
    assertEquals(0, response.getEstoques().get(0).getQuantidadeDisponivel());
    assertEquals(BigDecimal.ZERO, response.getValorTotalInventario());
    verify(estoqueRepository).buscaTodosEstoques(false, true, pageable);
    verify(estoqueRepository).calculaValorTotalInventario(false, true);
  }

  @Test
  void deveTratarFiltrosNulosComoDesativados() {
    Pageable pageable = PageRequest.of(0, 20);
    Page<Estoque> pagina =
        new PageImpl<>(
            List.of(criaEstoque("Produto A", 10, "20.00", "200.00", "35.00")), pageable, 1);

    when(estoqueRepository.buscaTodosEstoques(false, false, pageable)).thenReturn(pagina);
    when(estoqueRepository.calculaValorTotalInventario(false, false))
        .thenReturn(new BigDecimal("200.00"));

    EstoqueListagemResponse response =
        estoqueApplicationService.buscaTodosEstoques(null, null, 0, 20);

    assertEquals(1, response.getEstoques().size());
    assertEquals(new BigDecimal("200.00"), response.getValorTotalInventario());
    verify(estoqueRepository).buscaTodosEstoques(false, false, pageable);
    verify(estoqueRepository).calculaValorTotalInventario(false, false);
  }

  @Test
  void deveManterMetadadosDePaginacao() {
    Pageable pageable = PageRequest.of(1, 10);
    Page<Estoque> pagina =
        new PageImpl<>(
            List.of(criaEstoque("Produto B", 5, "12.00", "60.00", "20.00")), pageable, 11);

    when(estoqueRepository.buscaTodosEstoques(false, false, pageable)).thenReturn(pagina);
    when(estoqueRepository.calculaValorTotalInventario(false, false))
        .thenReturn(new BigDecimal("300.00"));

    EstoqueListagemResponse response =
        estoqueApplicationService.buscaTodosEstoques(false, false, 1, 10);

    assertEquals(1, response.getPagina());
    assertEquals(10, response.getTamanho());
    assertEquals(11, response.getTotalItens());
    assertEquals(new BigDecimal("300.00"), response.getValorTotalInventario());
    verify(estoqueRepository).buscaTodosEstoques(false, false, pageable);
    verify(estoqueRepository).calculaValorTotalInventario(false, false);
  }

  private Estoque criaEstoque(
      String descricaoProduto,
      Integer quantidadeDisponivel,
      String custoMedio,
      String custoTotal,
      String precoVenda) {
    Produto produto = criaProduto(descricaoProduto, precoVenda);

    return Estoque.builder()
        .id(UUID.randomUUID())
        .produto(produto)
        .quantidadeDisponivel(quantidadeDisponivel)
        .custoMedio(new BigDecimal(custoMedio))
        .custoTotal(new BigDecimal(custoTotal))
        .build();
  }

  private Produto criaProduto(String descricao, String precoVenda) {
    Produto produto =
        Produto.builder()
            .id(UUID.randomUUID())
            .descricao(descricao)
            .status(StatusProduto.ATIVO)
            .pesoLiquido(new BigDecimal("1.000"))
            .pesoBruto(new BigDecimal("1.200"))
            .grupo("HARDWARE")
            .unidade("UN")
            .estoqueMinimo(10)
            .estoqueMaximo(100)
            .precos(new ArrayList<>())
            .build();

    produto
        .getPrecos()
        .add(new Preco(UUID.randomUUID(), TipoPreco.PADRAO, new BigDecimal(precoVenda), produto));
    return produto;
  }
}
