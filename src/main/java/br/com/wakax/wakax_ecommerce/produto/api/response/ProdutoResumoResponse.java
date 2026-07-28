package br.com.wakax.wakax_ecommerce.produto.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;
import lombok.Getter;

@Getter
public class ProdutoResumoResponse {
  private final String descricao;
  private final StatusProduto status;
  private final BigDecimal preco;

  private final LocalDateTime dataCadastro;

  public ProdutoResumoResponse(Produto produto) {
    this.descricao = produto.getDescricao();
    this.status = produto.getStatus();
    this.preco = produto.getPrecoAtual();
    this.dataCadastro = produto.getDataCriacao();
  }
}
