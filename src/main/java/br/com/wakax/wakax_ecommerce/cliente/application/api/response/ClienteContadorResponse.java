package br.com.wakax.wakax_ecommerce.cliente.application.api.response;

import java.time.LocalDateTime;
import java.util.List;

public record ClienteContadorResponse(

        Long totalClientes,
        List<ClienteResumoResponse>clientes


) {
}
