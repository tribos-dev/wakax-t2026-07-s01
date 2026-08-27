package br.com.wakax.wakax_ecommerce.pedido.application.api.response;

import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface PedidoResumoProjection {
    UUID getId();

    LocalDateTime getDataPedido();

    StatusPedido getStatus();

    BigDecimal getValorTotal();
}
