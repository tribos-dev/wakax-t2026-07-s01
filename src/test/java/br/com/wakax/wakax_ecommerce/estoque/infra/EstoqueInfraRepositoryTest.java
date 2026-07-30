package br.com.wakax.wakax_ecommerce.estoque.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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

import br.com.wakax.wakax_ecommerce.estoque.domain.Estoque;

@ExtendWith(MockitoExtension.class)
class EstoqueInfraRepositoryTest {

  @Mock private EstoqueJPARepository estoqueJPARepository;

  @InjectMocks private EstoqueInfraRepository estoqueInfraRepository;

  @Test
  void deveBuscarTodosEstoquesSemFiltroEReordenarResultadoHidratado() {
    Pageable pageable = PageRequest.of(0, 2);
    UUID primeiroId = UUID.randomUUID();
    UUID segundoId = UUID.randomUUID();
    Page<UUID> paginaIds = new PageImpl<>(List.of(primeiroId, segundoId), pageable, 4);
    Estoque primeiroEstoque = criaEstoque(primeiroId);
    Estoque segundoEstoque = criaEstoque(segundoId);

    when(estoqueJPARepository.buscaIdsTodosEstoques(pageable)).thenReturn(paginaIds);
    when(estoqueJPARepository.buscaEstoquesComProdutoEPrecos(List.of(primeiroId, segundoId)))
        .thenReturn(new ArrayList<>(List.of(segundoEstoque, primeiroEstoque)));

    Page<Estoque> response = estoqueInfraRepository.buscaTodosEstoques(false, false, pageable);

    assertEquals(2, response.getContent().size());
    assertEquals(primeiroId, response.getContent().get(0).getId());
    assertEquals(segundoId, response.getContent().get(1).getId());
    assertEquals(0, response.getNumber());
    assertEquals(2, response.getSize());
    assertEquals(4, response.getTotalElements());
    verify(estoqueJPARepository).buscaIdsTodosEstoques(pageable);
    verify(estoqueJPARepository).buscaEstoquesComProdutoEPrecos(List.of(primeiroId, segundoId));
  }

  @Test
  void deveBuscarEstoquesComFiltroQuantidadeMinima() {
    Pageable pageable = PageRequest.of(0, 20);
    UUID estoqueId = UUID.randomUUID();
    Page<UUID> paginaIds = new PageImpl<>(List.of(estoqueId), pageable, 1);
    Estoque estoque = criaEstoque(estoqueId);

    when(estoqueJPARepository.buscaIdsEstoquesComQuantidadeMinima(pageable)).thenReturn(paginaIds);
    when(estoqueJPARepository.buscaEstoquesComProdutoEPrecos(List.of(estoqueId)))
        .thenReturn(new ArrayList<>(List.of(estoque)));

    Page<Estoque> response = estoqueInfraRepository.buscaTodosEstoques(true, false, pageable);

    assertEquals(1, response.getContent().size());
    assertEquals(estoqueId, response.getContent().get(0).getId());
    verify(estoqueJPARepository).buscaIdsEstoquesComQuantidadeMinima(pageable);
    verify(estoqueJPARepository, never()).buscaIdsTodosEstoques(pageable);
    verify(estoqueJPARepository, never()).buscaIdsEstoquesEmFalta(pageable);
  }

  @Test
  void deveBuscarEstoquesComFiltroEmFalta() {
    Pageable pageable = PageRequest.of(0, 20);
    UUID estoqueId = UUID.randomUUID();
    Page<UUID> paginaIds = new PageImpl<>(List.of(estoqueId), pageable, 1);
    Estoque estoque = criaEstoque(estoqueId);

    when(estoqueJPARepository.buscaIdsEstoquesEmFalta(pageable)).thenReturn(paginaIds);
    when(estoqueJPARepository.buscaEstoquesComProdutoEPrecos(List.of(estoqueId)))
        .thenReturn(new ArrayList<>(List.of(estoque)));

    Page<Estoque> response = estoqueInfraRepository.buscaTodosEstoques(false, true, pageable);

    assertEquals(1, response.getContent().size());
    assertEquals(estoqueId, response.getContent().get(0).getId());
    verify(estoqueJPARepository).buscaIdsEstoquesEmFalta(pageable);
    verify(estoqueJPARepository, never()).buscaIdsTodosEstoques(pageable);
    verify(estoqueJPARepository, never()).buscaIdsEstoquesComQuantidadeMinima(pageable);
  }

  @Test
  void deveRetornarPaginaVaziaSemExecutarHidratacao() {
    Pageable pageable = PageRequest.of(2, 20);
    Page<UUID> paginaIds = new PageImpl<>(List.of(), pageable, 50);

    when(estoqueJPARepository.buscaIdsTodosEstoques(pageable)).thenReturn(paginaIds);

    Page<Estoque> response = estoqueInfraRepository.buscaTodosEstoques(false, false, pageable);

    assertTrue(response.getContent().isEmpty());
    assertEquals(2, response.getNumber());
    assertEquals(20, response.getSize());
    assertEquals(50, response.getTotalElements());
    verify(estoqueJPARepository).buscaIdsTodosEstoques(pageable);
    verify(estoqueJPARepository, never()).buscaEstoquesComProdutoEPrecos(List.of());
  }

  @Test
  void deveCalcularValorTotalInventarioSemFiltro() {
    BigDecimal valorTotal = new BigDecimal("300.00");

    when(estoqueJPARepository.calculaValorTotalInventario()).thenReturn(valorTotal);

    BigDecimal response = estoqueInfraRepository.calculaValorTotalInventario(false, false);

    assertEquals(valorTotal, response);
    verify(estoqueJPARepository).calculaValorTotalInventario();
  }

  @Test
  void deveCalcularValorTotalInventarioComFiltroQuantidadeMinima() {
    BigDecimal valorTotal = new BigDecimal("50.00");

    when(estoqueJPARepository.calculaValorTotalInventarioQuantidadeMinima()).thenReturn(valorTotal);

    BigDecimal response = estoqueInfraRepository.calculaValorTotalInventario(true, false);

    assertEquals(valorTotal, response);
    verify(estoqueJPARepository).calculaValorTotalInventarioQuantidadeMinima();
    verify(estoqueJPARepository, never()).calculaValorTotalInventario();
    verify(estoqueJPARepository, never()).calculaValorTotalInventarioEmFalta();
  }

  @Test
  void deveCalcularValorTotalInventarioComFiltroEmFalta() {
    BigDecimal valorTotal = BigDecimal.ZERO;

    when(estoqueJPARepository.calculaValorTotalInventarioEmFalta()).thenReturn(valorTotal);

    BigDecimal response = estoqueInfraRepository.calculaValorTotalInventario(false, true);

    assertEquals(valorTotal, response);
    verify(estoqueJPARepository).calculaValorTotalInventarioEmFalta();
    verify(estoqueJPARepository, never()).calculaValorTotalInventario();
    verify(estoqueJPARepository, never()).calculaValorTotalInventarioQuantidadeMinima();
  }

  @Test
  void deveRetornarZeroQuandoSomaDoInventarioForNula() {
    when(estoqueJPARepository.calculaValorTotalInventario()).thenReturn(null);

    BigDecimal response = estoqueInfraRepository.calculaValorTotalInventario(false, false);

    assertEquals(BigDecimal.ZERO, response);
    verify(estoqueJPARepository).calculaValorTotalInventario();
  }

  private Estoque criaEstoque(UUID id) {
    return Estoque.builder().id(id).build();
  }
}
