package br.com.wakax.wakax_ecommerce.pedido.application.api.request;

import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoFilterRequest {
  private StatusPedido status;
}
