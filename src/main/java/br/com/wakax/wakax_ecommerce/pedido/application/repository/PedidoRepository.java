package br.com.wakax.wakax_ecommerce.pedido.application.repository;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoFilterRequest;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoRepository {

  Pedido salva(Pedido pedido);

  Pedido buscaPedidoPorId(UUID idPedido);

  Page<Pedido> buscarPedidosPorClienteComFiltro
          (PedidoFilterRequest loteFilterRequest, Pageable pageable, UUID idCliente);
}
