package br.com.wakax.wakax_ecommerce.pedido.application.api.response;

import java.time.LocalDateTime;

import br.com.wakax.wakax_ecommerce.pedido.domain.HistoricoRastreamento;
import br.com.wakax.wakax_ecommerce.pedido.domain.StatusRastreamento;
import lombok.Getter;

@Getter
public class HistoricoRastreamentoResponse {
  private final LocalDateTime dataEvento;
  private final String local;
  private final String descricao;
  private final StatusRastreamento status;

  public HistoricoRastreamentoResponse(HistoricoRastreamento evento) {
    this.dataEvento = evento.getDataEvento();
    this.local = evento.getLocal();
    this.descricao = evento.getDescricao();
    this.status = evento.getStatus();
  }
}
