package br.com.wakax.wakax_ecommerce.produto.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.ProdutoDisponivel;
import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;

@ExtendWith(MockitoExtension.class)
class ProdutoInfraRepositoryTest {

  @Mock private ProdutoJPARepository produtoJPARepository;

  @InjectMocks private ProdutoInfraRepository produtoInfraRepository;

  @Test
  void deveListarProdutosAtivosComEstoquePreservandoAOrdemDaPaginaDeIds() {
    Pageable pageable = PageRequest.of(0, 2);
    UUID primeiroId = UUID.randomUUID();
    UUID segundoId = UUID.randomUUID();
    Page<UUID> paginaDeIds = new PageImpl<>(List.of(primeiroId, segundoId), pageable, 4);
    Produto primeiroProduto = Produto.builder().id(primeiroId).build();
    Produto segundoProduto = Produto.builder().id(segundoId).build();

    when(produtoJPARepository.paginaIdsProdutosComEstoquePorStatus(StatusProduto.ATIVO, pageable))
        .thenReturn(paginaDeIds);
    when(produtoJPARepository.buscaProdutosComPrecosEQuantidadePorIds(
            List.of(primeiroId, segundoId)))
        .thenReturn(List.of(new Object[] {segundoProduto, 3}, new Object[] {primeiroProduto, 8}));

    Page<ProdutoDisponivel> response =
        produtoInfraRepository.listaProdutosAtivosComEstoque(pageable);

    assertEquals(primeiroId, response.getContent().get(0).getProduto().getId());
    assertEquals(8, response.getContent().get(0).getQuantidadeDisponivel());
    assertEquals(segundoId, response.getContent().get(1).getProduto().getId());
    assertEquals(3, response.getContent().get(1).getQuantidadeDisponivel());
    assertEquals(4L, response.getTotalElements());
  }

  @Test
  void deveRetornarPaginaVaziaSemExecutarASegundaConsulta() {
    Pageable pageable = PageRequest.of(2, 10);
    Page<UUID> paginaVazia = new PageImpl<>(List.of(), pageable, 20);

    when(produtoJPARepository.paginaIdsProdutosComEstoquePorStatus(StatusProduto.ATIVO, pageable))
        .thenReturn(paginaVazia);

    Page<ProdutoDisponivel> response =
        produtoInfraRepository.listaProdutosAtivosComEstoque(pageable);

    assertTrue(response.isEmpty());
    assertEquals(20L, response.getTotalElements());
    assertEquals(2, response.getNumber());
    verify(produtoJPARepository, never()).buscaProdutosComPrecosEQuantidadePorIds(anyList());
  }
}
