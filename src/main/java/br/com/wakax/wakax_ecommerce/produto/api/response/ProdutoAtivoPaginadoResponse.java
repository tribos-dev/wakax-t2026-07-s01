package br.com.wakax.wakax_ecommerce.produto.api.response;

import java.util.List;

import org.springframework.data.domain.Page;

import br.com.wakax.wakax_ecommerce.produto.domain.ProdutoDisponivel;
import lombok.Getter;

@Getter
public class ProdutoAtivoPaginadoResponse {
  private final List<ProdutoAtivoResponse> produtos;
  private final long total;
  private final int pagina;
  private final int totalPaginas;

  public ProdutoAtivoPaginadoResponse(Page<ProdutoDisponivel> produtosDisponiveis) {
    this.produtos =
        produtosDisponiveis.getContent().stream().map(ProdutoAtivoResponse::new).toList();
    this.total = produtosDisponiveis.getTotalElements();
    this.pagina = produtosDisponiveis.getNumber();
    this.totalPaginas = produtosDisponiveis.getTotalPages();
  }
}
