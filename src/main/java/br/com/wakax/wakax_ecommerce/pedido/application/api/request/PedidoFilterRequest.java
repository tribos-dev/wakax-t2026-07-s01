package br.com.wakax.wakax_ecommerce.pedido.application.api.request;

import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PedidoFilterRequest {
    private StatusPedido status;

    public PedidoFilterRequest (StatusPedido status) {
        this.status = status;
    }
}
