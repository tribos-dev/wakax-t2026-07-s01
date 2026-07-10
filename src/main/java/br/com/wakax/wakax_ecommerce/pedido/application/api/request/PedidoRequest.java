package br.com.wakax.wakax_ecommerce.pedido.application.api.request;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import br.com.wakax.wakax_ecommerce.pedido.domain.FormaPagamento;
import lombok.Getter;

@Getter
public class PedidoRequest {
  @NotNull private UUID idCarrinho;

  @NotNull private FormaPagamento formaPagamento;
}
