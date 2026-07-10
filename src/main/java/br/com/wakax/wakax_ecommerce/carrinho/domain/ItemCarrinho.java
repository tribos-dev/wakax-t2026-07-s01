package br.com.wakax.wakax_ecommerce.carrinho.domain;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import br.com.wakax.wakax_ecommerce.carrinho.api.request.ItemCarrinhoRequest;
import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarrinho {
  @Id @GeneratedValue private UUID id;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  @NotNull
  private Carrinho carrinho;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  @NotNull
  private Produto produto;

  @Column(nullable = false)
  @NotNull
  @Min(1)
  private Integer quantidade;

  public ItemCarrinho(Carrinho carrinho, Produto produto, ItemCarrinhoRequest itemCarrinhoRequest) {
    this.carrinho = carrinho;
    this.produto = produto;
    this.quantidade = itemCarrinhoRequest.getQuantidade();
  }

  public BigDecimal getValorTotalDoItem() {
    BigDecimal valorUnitario = this.produto.getPrecoPadrao();
    return valorUnitario.multiply(new BigDecimal(this.quantidade));
  }
}
