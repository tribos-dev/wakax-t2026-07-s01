package br.com.wakax.wakax_ecommerce.pedido.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "historico_rastreamento")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoRastreamento {
  @Id @GeneratedValue private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "rastreamento_id", nullable = false)
  @NotNull
  private Rastreamento rastreamento;

  @Column(name = "data_evento", nullable = false)
  @NotNull
  private LocalDateTime dataEvento;

  @Column(nullable = false)
  @NotBlank
  private String local;

  @Column(nullable = false)
  @NotBlank
  private String descricao;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @NotNull
  private StatusRastreamento status;
}
