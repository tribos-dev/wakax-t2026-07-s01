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
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_tentativa_pagamento_numero",
          columnNames = {"pagamento_id", "numero_tentativa"}),
      @UniqueConstraint(
          name = "uk_tentativa_pagamento_idempotencia",
          columnNames = "chave_idempotencia")
    })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TentativaPagamento {
  private static final int TAMANHO_MAXIMO_DETALHE = 500;

  @Id @GeneratedValue private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pagamento_id", nullable = false)
  @NotNull
  private Pagamento pagamento;

  @Column(name = "numero_tentativa", nullable = false)
  @Min(2)
  @Max(Pagamento.LIMITE_TENTATIVAS)
  private int numeroTentativa;

  @Column(name = "data_tentativa", nullable = false)
  @NotNull
  private LocalDateTime dataTentativa;

  @Column(name = "chave_idempotencia", nullable = false, length = 100)
  @NotBlank
  private String chaveIdempotencia;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  @NotNull
  private StatusTentativaPagamento status;

  @Column(length = TAMANHO_MAXIMO_DETALHE)
  private String detalhe;

  public static TentativaPagamento pendente(Pagamento pagamento) {
    int numeroTentativa = pagamento.getNumeroTentativas();
    return TentativaPagamento.builder()
        .pagamento(pagamento)
        .numeroTentativa(numeroTentativa)
        .dataTentativa(LocalDateTime.now())
        .chaveIdempotencia(criarChaveIdempotencia(pagamento.getId(), numeroTentativa))
        .status(StatusTentativaPagamento.PENDENTE_ENVIO)
        .build();
  }

  private static String criarChaveIdempotencia(UUID idPagamento, int numeroTentativa) {
    return String.format("pagamento:%s:tentativa:%d", idPagamento, numeroTentativa);
  }

  public void marcarComoEnviada() {
    this.status = StatusTentativaPagamento.ENVIADA;
    this.detalhe = null;
  }

  public void marcarComoFalha(String detalhe) {
    this.status = StatusTentativaPagamento.FALHA_ENVIO;
    this.detalhe = limitarDetalhe(detalhe);
  }

  private String limitarDetalhe(String detalhe) {
    if (detalhe == null || detalhe.length() <= TAMANHO_MAXIMO_DETALHE) {
      return detalhe;
    }
    return detalhe.substring(0, TAMANHO_MAXIMO_DETALHE);
  }
}
