package br.com.wakax.wakax_ecommerce.pedido.application.api.response;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pedido.domain.ItemPedido;
import lombok.Getter;

@Getter
public class ItemPedidoResponse {
  private UUID id;
  private UUID produtoId;
  private String descricaoProduto;
  private Integer quantidade;
  private BigDecimal valorUnitario;
  private BigDecimal valorTotal;

  public ItemPedidoResponse(ItemPedido item) {
    this.id = item.getId();
    this.produtoId = item.getProduto().getId();
    this.descricaoProduto = item.getProduto().getDescricao();
    this.quantidade = item.getQuantidade();
    this.valorUnitario = item.getValorUnitario();
    this.valorTotal = item.getValorTotal();
  }
}
