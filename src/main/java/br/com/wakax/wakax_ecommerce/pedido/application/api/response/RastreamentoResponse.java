package br.com.wakax.wakax_ecommerce.pedido.application.api.response;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import br.com.wakax.wakax_ecommerce.pedido.domain.Rastreamento;
import br.com.wakax.wakax_ecommerce.pedido.domain.StatusRastreamento;
import lombok.Getter;

@Getter
public class RastreamentoResponse {
  private final String codigo;
  private final String transportadora;
  private final StatusRastreamento statusAtual;
  private final LocalDate previsaoEntrega;
  private final List<HistoricoRastreamentoResponse> historico;

  public RastreamentoResponse(Rastreamento rastreamento) {
    this.codigo = rastreamento.getCodigo();
    this.transportadora = rastreamento.getTransportadora();
    this.statusAtual = rastreamento.getStatusAtual();
    this.previsaoEntrega = rastreamento.getPrevisaoEntrega();
    this.historico =
        rastreamento.getEventos() == null
            ? Collections.emptyList()
            : rastreamento.getEventos().stream()
                .map(HistoricoRastreamentoResponse::new)
                .collect(Collectors.toList());
  }
}
