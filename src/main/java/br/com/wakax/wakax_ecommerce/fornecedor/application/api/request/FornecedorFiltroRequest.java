package br.com.wakax.wakax_ecommerce.fornecedor.application.api.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FornecedorFiltroRequest {
  private StatusPessoa status = StatusPessoa.ATIVO;
  private boolean todos = false;
  private int page = 0;
  private int size = 10;
  private String sortBy = "razaoSocial";
  private String sortDirection = "ASC";

  public Pageable toPageable() {
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    return PageRequest.of(page, size, Sort.by(direction, sortBy));
  }
}
