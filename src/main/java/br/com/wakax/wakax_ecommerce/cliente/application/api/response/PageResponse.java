package br.com.wakax.wakax_ecommerce.cliente.application.api.response;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageResponse<T> {

  private List<T> content;
  private Long totalElements;
  private int paginaAtual;
  private int totalPaginas;
  private long totalUsuarios;

  public static <T> PageResponse<T> from(Page<T> page) {
    return new PageResponse<>(
        page.getContent(),
        page.getTotalElements(),
        page.getNumber(),
        page.getTotalPages(),
        page.getNumberOfElements());
  }
}
