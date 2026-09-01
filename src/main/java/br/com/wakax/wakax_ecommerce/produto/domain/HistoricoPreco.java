package br.com.wakax.wakax_ecommerce.produto.domain;

import java.math.BigDecimal;
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
@Table(name = "historico_preco")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoPreco {
  @Id @GeneratedValue private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "preco_id", nullable = false)
  private Preco preco;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @NotNull
  private TipoPreco tipo;

  @Column(nullable = false)
  @NotNull
  private BigDecimal valorDe;

  @Column(nullable = false)
  @NotNull
  private BigDecimal valorPara;

  @Column(name = "data_evento", nullable = false)
  @NotNull
  private LocalDateTime dataEvento;

  @Column(nullable = false)
  @NotBlank
  private String motivo;

  @Column(nullable = false)
  @NotBlank
  private String usuario;

  @PrePersist
  protected void onCreate() {
    dataEvento = LocalDateTime.now();
  }
}
