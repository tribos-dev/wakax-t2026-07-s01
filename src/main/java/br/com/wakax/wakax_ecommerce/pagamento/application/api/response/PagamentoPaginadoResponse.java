package br.com.wakax.wakax_ecommerce.pagamento.application.api.response;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;

@Getter
public class PagamentoPaginadoResponse {
  private final List<PagamentoResumoResponse> pagamentos;
  private final long totalPagamentos;
  private final int totalPaginas;
  private final int paginaAtual;
  private final BigDecimal valorTotalPagamentos;

  public PagamentoPaginadoResponse(
      Page<PagamentoResumoResponse> pagamentos, BigDecimal valorTotalPagamentos) {
    this.pagamentos = pagamentos.getContent();
    this.totalPagamentos = pagamentos.getTotalElements();
    this.totalPaginas = pagamentos.getTotalPages();
    this.paginaAtual = pagamentos.getNumber();
    this.valorTotalPagamentos = valorTotalPagamentos;
  }
}
