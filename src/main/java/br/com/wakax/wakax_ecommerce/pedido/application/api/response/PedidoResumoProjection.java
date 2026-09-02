package br.com.wakax.wakax_ecommerce.pedido.application.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;

public interface PedidoResumoProjection {
  UUID getId();

  LocalDateTime getDataPedido();

  StatusPedido getStatus();

  BigDecimal getValorTotal();
}
