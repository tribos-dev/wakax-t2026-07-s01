package br.com.wakax.wakax_ecommerce.produto.api.response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import lombok.Getter;

@Getter
public class ProdutoResponse {
  private final UUID idProduto;
  private final String descricao;
  private final List<PrecoResponse> precos;

  public ProdutoResponse(Produto produto) {
    this.idProduto = produto.getId();
    this.descricao = produto.getDescricao();
    this.precos = produto.getPrecos().stream().map(PrecoResponse::new).collect(Collectors.toList());
  }
}
