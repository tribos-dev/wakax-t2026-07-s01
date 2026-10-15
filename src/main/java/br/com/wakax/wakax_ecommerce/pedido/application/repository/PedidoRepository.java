package br.com.wakax.wakax_ecommerce.pedido.application.repository;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;

public interface PedidoRepository {

  Pedido salva(Pedido pedido);

  Pedido buscaPedidoPorId(UUID idPedido);
}
