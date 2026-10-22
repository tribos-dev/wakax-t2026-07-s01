package br.com.wakax.wakax_ecommerce.pedido.application.repository;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoFilterRequest;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;

public interface PedidoRepository {

  Pedido salva(Pedido pedido);

  Pedido buscaPedidoPorId(UUID idPedido);

  Page<Pedido> buscarPedidosPorClienteComFiltro(
          StatusPedido status, Pageable pageable, UUID idCliente);
}
