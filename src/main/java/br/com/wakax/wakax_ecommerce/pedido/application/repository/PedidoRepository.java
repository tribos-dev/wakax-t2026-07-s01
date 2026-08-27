package br.com.wakax.wakax_ecommerce.pedido.application.repository;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoResumoProjection;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoRepository {

  Pedido salva(Pedido pedido);

  Pedido buscaPedidoPorId(UUID idPedido);

    Page<PedidoResumoProjection> buscaPedidosDoCliente(UUID idCliente, StatusPedido status, Pageable pageable);
}
