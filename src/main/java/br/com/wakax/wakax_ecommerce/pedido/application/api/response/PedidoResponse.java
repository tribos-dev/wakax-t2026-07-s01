package br.com.wakax.wakax_ecommerce.pedido.application.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.wakax.wakax_ecommerce.pedido.domain.FormaPagamento;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;
import lombok.Getter;

@Getter
public class PedidoResponse {
  private UUID idPedido;
  private UUID clienteId;
  private String nomeCliente;
  private LocalDateTime dataPedido;
  private StatusPedido status;
  private List<ItemPedidoResponse> itensPedido;
  private BigDecimal valorTotal;
  private FormaPagamento formaPagamento;
  private EnderecoResponse enderecoEntrega;

  public PedidoResponse(Pedido pedido) {
    this.idPedido = pedido.getId();
    this.clienteId = pedido.getCliente().getId();
    this.nomeCliente = pedido.getCliente().getPessoa().getNome();
    this.dataPedido = pedido.getDataPedido();
    this.status = pedido.getStatus();
    this.itensPedido =
        pedido.getItensPedido().stream().map(ItemPedidoResponse::new).collect(Collectors.toList());
    this.valorTotal = pedido.getValorTotal();
    this.formaPagamento = pedido.getFormaPagamento();
    this.enderecoEntrega = new EnderecoResponse(pedido.getEnderecoEntrega());
  }
}
