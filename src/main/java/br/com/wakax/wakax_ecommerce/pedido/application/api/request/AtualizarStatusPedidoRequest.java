package br.com.wakax.wakax_ecommerce.pedido.application.api.request;

import javax.validation.constraints.NotNull;

import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarStatusPedidoRequest {
  @NotNull private StatusPedido status;
}
