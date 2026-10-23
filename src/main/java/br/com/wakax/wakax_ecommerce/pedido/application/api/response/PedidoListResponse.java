package br.com.wakax.wakax_ecommerce.pedido.application.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;
import lombok.Getter;

@Getter
public class PedidoListResponse {
  private UUID idPedido;
  private LocalDateTime dataPedido;
  private BigDecimal valorTotal;
  private StatusPedido status;

  public PedidoListResponse(Pedido pedido) {
    this.idPedido = pedido.getId();
    this.dataPedido = pedido.getDataPedido();
    this.valorTotal = pedido.getValorTotal();
    this.status = pedido.getStatus();
  }
}
