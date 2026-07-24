package br.com.wakax.wakax_ecommerce.fornecedor.application.api.request;

import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Getter
@Setter
public class FornecedorFiltroRequest {
    private StatusPessoa status;
    private int page = 0;
    private int size = 10;
    private String sortBy = "razaoSocial";
    private String sortDirection = "ASC";

    public Pageable toPageable() {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
}
