package br.com.wakax.wakax_ecommerce.carrinho.api.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Data;

@Data
public class CarrinhoPaginacaoRequest {

    private int page = 0;
    private int size = 20;
    private String sortBy = "dataCriacao";
    private String sortDirection = "DESC";

    public Pageable paraPageable() {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
}