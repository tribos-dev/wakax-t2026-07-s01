package br.com.wakax.wakax_ecommerce.produto.api.response;

import java.util.List;

import org.springframework.data.domain.Page;

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import lombok.Getter;

@Getter
public class ProdutoPaginadoResponse {
  private final List<ProdutoResumoResponse> produtos;
  private final long total;
  private final int pagina;
  private final int totalPaginas;

  public ProdutoPaginadoResponse(Page<Produto> produtos) {
    this.produtos = produtos.getContent().stream().map(ProdutoResumoResponse::new).toList();
    this.total = produtos.getTotalElements();
    this.pagina = produtos.getNumber();
    this.totalPaginas = produtos.getTotalPages();
  }
}
