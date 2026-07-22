package br.com.wakax.wakax_ecommerce.cliente.application.api.mapper;

import br.com.wakax.wakax_ecommerce.cliente.application.api.response.ClienteResumoResponse;
import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ClienteMapper {

    public ClienteResumoResponse paraResumo(Cliente cliente) {
        return new ClienteResumoResponse(
                cliente.getPessoa().getNome(),
                cliente.getPessoa().getEmails().toString(),
                cliente.getPessoa().getStatus(),
                cliente.getDataCriacao(),
                cliente.getDataEdicao()
        );
    }


    }




