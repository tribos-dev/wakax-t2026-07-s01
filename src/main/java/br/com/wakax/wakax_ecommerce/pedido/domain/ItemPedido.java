package br.com.wakax.wakax_ecommerce.pedido.domain;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.*;

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {
  @Id @GeneratedValue private UUID id;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  @NotNull
  private Produto produto;

  @Column(nullable = false)
  @NotNull
  @Min(1)
  private Integer quantidade;

  @Column(nullable = false)
  @NotNull
  @PositiveOrZero
  private BigDecimal valorUnitario;

  @ManyToOne(optional = false)
  @JoinColumn(name = "pedido_id", nullable = false)
  @NotNull
  private Pedido pedido;

  public BigDecimal getValorTotal() {
    return this.valorUnitario.multiply(BigDecimal.valueOf(this.quantidade));
  }
}
