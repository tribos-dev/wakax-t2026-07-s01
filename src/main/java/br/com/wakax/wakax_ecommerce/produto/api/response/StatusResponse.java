package br.com.wakax.wakax_ecommerce.produto.api.response;

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class StatusResponse {

    private final UUID id;
    private final StatusProduto statusProduto;

    public StatusResponse(Produto produto) {
        this.id= produto.getId();
        this.statusProduto = produto.getStatus();
    }


}

