package br.com.wakax.wakax_ecommerce.pedido.application.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PedidoResumoResponse {
  private final UUID idPedido;
  private final LocalDateTime dataPedido;
  private final StatusPedido status;
  private final BigDecimal valorTotal;
}
