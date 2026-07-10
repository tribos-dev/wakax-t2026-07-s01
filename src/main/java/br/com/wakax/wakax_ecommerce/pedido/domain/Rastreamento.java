package br.com.wakax.wakax_ecommerce.pedido.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.wakax.wakax_ecommerce.pedido.application.api.request.HistoricoRastreamentoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.RastreamentoRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rastreamento {
  @Id @GeneratedValue private UUID id;

  @Column(nullable = false, unique = true)
  @NotBlank
  private String codigo;

  @Column(nullable = false)
  @NotBlank
  private String transportadora;

  @Enumerated(EnumType.STRING)
  @Column(name = "status_atual", nullable = false)
  @NotNull
  private StatusRastreamento statusAtual;

  @Column(name = "previsao_entrega")
  private LocalDate previsaoEntrega;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pedido_id", nullable = false, unique = true)
  @NotNull
  private Pedido pedido;

  @OneToMany(mappedBy = "rastreamento", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("dataEvento DESC")
  @Builder.Default
  private List<HistoricoRastreamento> eventos = new ArrayList<>();

  public Rastreamento(RastreamentoRequest request, Pedido pedido) {
    this.codigo = request.getCodigo();
    this.transportadora = request.getTransportadora();
    this.statusAtual = request.getStatusAtual();
    this.previsaoEntrega = request.getPrevisaoEntrega();
    this.pedido = pedido;
    this.eventos = mapearEventos(request.getEventos());
  }

  private List<HistoricoRastreamento> mapearEventos(
      List<HistoricoRastreamentoRequest> eventosRequest) {
    return Objects.requireNonNullElse(
            eventosRequest, Collections.<HistoricoRastreamentoRequest>emptyList())
        .stream()
        .map(
            evento ->
                HistoricoRastreamento.builder()
                    .rastreamento(this)
                    .dataEvento(evento.getDataEvento())
                    .local(evento.getLocal())
                    .descricao(evento.getDescricao())
                    .status(evento.getStatus())
                    .build())
        .collect(Collectors.toList());
  }
}
