package br.com.wakax.wakax_ecommerce.fornecedor.application.api.response;

import java.util.List;

import org.springframework.data.domain.Page;

import br.com.wakax.wakax_ecommerce.fornecedor.domain.Fornecedor;
import lombok.Getter;

@Getter
public class FornecedorPageResponse {

  private long totalElementos;
  private int paginaAtual;
  private int totalPaginas;
  private int tamanho;
  private List<FornecedorDadosResumidos> fornecedoresResumidos;

  public FornecedorPageResponse(Page<Fornecedor> page) {
    this.totalElementos = page.getTotalElements();
    this.paginaAtual = page.getNumber();
    this.totalPaginas = page.getTotalPages();
    this.tamanho = page.getSize();
    this.fornecedoresResumidos =
        page.getContent().stream().map(FornecedorDadosResumidos::new).toList();
  }
}
