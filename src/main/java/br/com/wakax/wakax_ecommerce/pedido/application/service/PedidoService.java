package br.com.wakax.wakax_ecommerce.pedido.application.service;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoPaginadoResponse;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoResponse;
import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;

public interface PedidoService {

  PedidoResponse cadastraPedido(PedidoRequest pedidoRequest);

  PedidoResponse buscaPedidoPorId(UUID idPedido);

  PedidoPaginadoResponse buscaPedidosDoCliente(
      UUID idCliente, StatusPedido status, int pagina, int tamanho);
}
