package br.com.wakax.wakax_ecommerce.produto.api.response;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;
import lombok.Getter;

@Getter
public class ProdutoAtivoResponse {

  private final UUID idProduto;
  private final String descricao;
  private final String descricaoResumida;
  private final StatusProduto status;
  private final String grupo;
  private final BigDecimal preco;
  private final Integer quantidadeDisponivel;

  public ProdutoAtivoResponse(Produto produto, Integer quantidadeDisponivel) {
    this.idProduto = produto.getId();
    this.descricao = produto.getDescricao();
    this.descricaoResumida = produto.getDescricaoComplementar();
    this.status = produto.getStatus();
    this.grupo = produto.getGrupo();
    this.preco = produto.getPrecoPadrao();
    this.quantidadeDisponivel = quantidadeDisponivel;
  }
}
