package br.com.wakax.wakax_ecommerce.pedido.application.service;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pedido.application.api.request.EnderecoUpdateRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoListResponse;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoResponse;
import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;
import org.springframework.data.domain.Page;

public interface PedidoService {

  PedidoResponse cadastraPedido(PedidoRequest pedidoRequest);

  PedidoResponse buscaPedidoPorId(UUID idPedido);

  void atualizarStatus(UUID idPedido, StatusPedido novoStatus);

  void alteraEnderecoEntrega(UUID idPedido, EnderecoUpdateRequest enderecoUpdateRequest);

  Page<PedidoListResponse> buscarTodosPedidosComFiltro(UUID idCliente);
}
