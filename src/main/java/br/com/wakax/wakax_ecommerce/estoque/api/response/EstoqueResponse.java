package br.com.wakax.wakax_ecommerce.estoque.api.response;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.estoque.domain.Estoque;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstoqueResponse {
  private UUID id;
  private UUID idProduto;
  private String descricaoProduto;
  private Integer quantidadeDisponivel;
  private BigDecimal custoMedio;
  private BigDecimal custoTotal;
  private BigDecimal precoVenda;

  public EstoqueResponse(Estoque estoque) {
    this.id = estoque.getId();
    this.idProduto = estoque.getProduto().getId();
    this.descricaoProduto = estoque.getProduto().getDescricao();
    this.quantidadeDisponivel = estoque.getQuantidadeDisponivel();
    this.custoMedio = estoque.getCustoMedio();
    this.custoTotal = estoque.getCustoTotal();
    this.precoVenda = estoque.getProduto().getPrecoPadrao();
  }
}
