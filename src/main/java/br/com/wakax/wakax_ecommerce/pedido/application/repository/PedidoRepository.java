package br.com.wakax.wakax_ecommerce.pedido.application.repository;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoListResponse;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import org.springframework.data.domain.Page;

public interface PedidoRepository {

  Pedido salva(Pedido pedido);

  Pedido buscaPedidoPorId(UUID idPedido);

  Page<PedidoListResponse> buscarPedidosComFiltro(UUID idCliente);
}
