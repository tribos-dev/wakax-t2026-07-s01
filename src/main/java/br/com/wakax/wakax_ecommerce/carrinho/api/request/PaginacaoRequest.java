package br.com.wakax.wakax_ecommerce.carrinho.api.request;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
public class PaginacaoRequest {

    private int page = 0;
    private int size = 20;

    public Pageable paraPageable() {
        return PageRequest.of(page, size);
    }
}
