package br.com.wakax.wakax_ecommerce.pedido.application.api.request;

import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PedidoFilterRequest {
  private StatusPedido status;

  public PedidoFilterRequest (StatusPedido status) {
    this.status = status;
  }
}
