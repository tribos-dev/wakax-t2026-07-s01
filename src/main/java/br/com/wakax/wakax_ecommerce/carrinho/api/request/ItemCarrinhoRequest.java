package br.com.wakax.wakax_ecommerce.carrinho.api.request;

import java.util.UUID;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class ItemCarrinhoRequest {

  @NotNull(message = "{validacao.itemcarrinho.idproduto.obrigatorio}")
  private UUID idProduto;

  @NotNull(message = "{validacao.itemcarrinho.quantidade.obrigatoria}")
  @Min(value = 1, message = "{item-carrinho.quantidade.menor.que.um}")
  private Integer quantidade;
}
