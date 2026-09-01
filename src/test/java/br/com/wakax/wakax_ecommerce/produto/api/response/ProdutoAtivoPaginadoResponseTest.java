package br.com.wakax.wakax_ecommerce.produto.api.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import br.com.wakax.wakax_ecommerce.produto.domain.Preco;
import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.ProdutoDisponivel;
import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;
import br.com.wakax.wakax_ecommerce.produto.domain.TipoPreco;

class ProdutoAtivoPaginadoResponseTest {

  @Test
  void deveMapearProdutoDisponivelEInformacoesDaPagina() {
    Produto produto =
        Produto.builder()
            .id(UUID.randomUUID())
            .descricao("Notebook")
            .descricaoComplementar("Notebook para trabalho")
            .status(StatusProduto.ATIVO)
            .grupo("Eletronicos")
            .build();
    produto.setPrecos(
        List.of(
            new Preco(TipoPreco.PADRAO, new BigDecimal("4500.00"), produto),
            new Preco(TipoPreco.PROMOCIONAL, new BigDecimal("3999.90"), produto)));

    ProdutoDisponivel produtoDisponivel = new ProdutoDisponivel(produto, 7);
    Page<ProdutoDisponivel> pagina =
        new PageImpl<>(List.of(produtoDisponivel), PageRequest.of(1, 2), 5);

    ProdutoAtivoPaginadoResponse response = new ProdutoAtivoPaginadoResponse(pagina);

    assertEquals(5L, response.getTotal());
    assertEquals(1, response.getPagina());
    assertEquals(3, response.getTotalPaginas());
    assertEquals(1, response.getProdutos().size());

    ProdutoAtivoResponse item = response.getProdutos().get(0);
    assertEquals(produto.getId(), item.getIdProduto());
    assertEquals("Notebook", item.getDescricao());
    assertEquals("Notebook para trabalho", item.getDescricaoResumida());
    assertEquals(StatusProduto.ATIVO, item.getStatus());
    assertEquals("Eletronicos", item.getGrupo());
    assertEquals(new BigDecimal("3999.90"), item.getPrecoAtual());
    assertEquals(7, item.getQuantidadeDisponivel());
  }
}
