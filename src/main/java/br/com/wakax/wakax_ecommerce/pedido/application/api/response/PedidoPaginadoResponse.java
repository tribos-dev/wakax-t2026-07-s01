package br.com.wakax.wakax_ecommerce.pedido.application.api.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PedidoPaginadoResponse {
    private final List<PedidoResumoResponse> pedidos;
    private final long totalPedidos;
    private final int totalPaginas;
    private final int paginaAtual;

    public PedidoPaginadoResponse(Page<PedidoResumoResponse> pedidos) {
        this.pedidos = pedidos.getContent();
        this.totalPedidos = pedidos.getTotalElements();
        this.totalPaginas = pedidos.getTotalPages();
        this.paginaAtual = pedidos.getNumber();
    }
}
