package br.com.wakax.wakax_ecommerce.produto.api.response;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.ProdutoDisponivel;
import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;
import lombok.Getter;

@Getter
public class ProdutoAtivoResponse {
  private final UUID idProduto;
  private final String descricao;
  private final String descricaoResumida;
  private final StatusProduto status;
  private final String grupo;
  private final BigDecimal precoAtual;
  private final Integer quantidadeDisponivel;

  public ProdutoAtivoResponse(ProdutoDisponivel produtoDisponivel) {
    Produto produto = produtoDisponivel.getProduto();
    this.idProduto = produto.getId();
    this.descricao = produto.getDescricao();
    this.descricaoResumida = produto.getDescricaoComplementar();
    this.status = produto.getStatus();
    this.grupo = produto.getGrupo();
    this.precoAtual = produto.getPrecoAtual();
    this.quantidadeDisponivel = produtoDisponivel.getQuantidadeDisponivel();
  }
}
