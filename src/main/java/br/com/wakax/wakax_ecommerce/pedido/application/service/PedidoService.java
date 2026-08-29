package br.com.wakax.wakax_ecommerce.pedido.application.service;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pedido.application.api.request.EnderecoUpdateRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoResponse;

public interface PedidoService {

  PedidoResponse cadastraPedido(PedidoRequest pedidoRequest);

  PedidoResponse buscaPedidoPorId(UUID idPedido);

  void alteraEnderecoEntrega(UUID idPedido, EnderecoUpdateRequest enderecoUpdateRequest);
}
