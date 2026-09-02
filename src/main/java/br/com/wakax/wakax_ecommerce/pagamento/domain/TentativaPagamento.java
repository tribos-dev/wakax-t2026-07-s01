package br.com.wakax.wakax_ecommerce.pagamento.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "tentativa_pagamento",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uk_tentativa_pagamento_numero",
            columnNames = {"pagamento_id", "numero_tentativa"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TentativaPagamento {
  public static final int LIMITE_TENTATIVAS = 3;

  @Id @GeneratedValue private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pagamento_id", nullable = false)
  @NotNull
  private Pagamento pagamento;

  @Column(name = "numero_tentativa", nullable = false)
  @Min(1)
  @Max(LIMITE_TENTATIVAS)
  private int numeroTentativa;

  @Column(name = "data_tentativa", nullable = false)
  @NotNull
  private LocalDateTime dataTentativa;

  public static TentativaPagamento nova(Pagamento pagamento, int numeroTentativa) {
    return TentativaPagamento.builder()
        .pagamento(pagamento)
        .numeroTentativa(numeroTentativa)
        .dataTentativa(LocalDateTime.now())
        .build();
  }
}
