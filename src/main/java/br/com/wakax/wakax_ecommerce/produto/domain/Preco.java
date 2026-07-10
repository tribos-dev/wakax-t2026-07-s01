package br.com.wakax.wakax_ecommerce.produto.domain;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Preco {
  @Id @GeneratedValue private UUID id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @NotNull
  private TipoPreco tipo;

  @Column(nullable = false)
  @NotNull
  @PositiveOrZero
  private BigDecimal valor;

  @ManyToOne private Produto produto;

  public Preco(TipoPreco tipo, BigDecimal valor, Produto produto) {
    this.tipo = tipo;
    this.valor = valor;
    this.produto = produto;
  }
}
