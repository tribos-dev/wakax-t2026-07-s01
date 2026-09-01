package br.com.wakax.wakax_ecommerce.produto.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

  @OneToMany(mappedBy = "preco", cascade = CascadeType.ALL)
  @OrderBy("dataEvento DESC")
  @Builder.Default
  private List<HistoricoPreco> historico = new ArrayList<>();

  public Preco(TipoPreco tipo, BigDecimal valor, Produto produto) {
    this.tipo = tipo;
    this.valor = valor;
    this.produto = produto;
  }

  public void atualizaValor(BigDecimal valorPara, String motivo, String usuario) {
    BigDecimal valorDe = this.valor;
    this.valor = valorPara;

    HistoricoPreco historicoPreco =
        HistoricoPreco.builder()
            .preco(this)
            .tipo(this.tipo)
            .valorDe(valorDe)
            .valorPara(valorPara)
            .motivo(motivo)
            .usuario(usuario)
            .build();

    this.historico.add(historicoPreco);
  }
}
