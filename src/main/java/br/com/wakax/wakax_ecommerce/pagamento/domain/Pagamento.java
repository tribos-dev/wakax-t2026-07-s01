package br.com.wakax.wakax_ecommerce.pagamento.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.*;

import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {
  public static final int LIMITE_TENTATIVAS = 3;

  @Id @GeneratedValue private UUID id;

  @OneToOne(optional = false)
  @JoinColumn(nullable = false, unique = true)
  @NotNull
  private Pedido pedido;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @NotNull
  private StatusPagamento statusPagamento;

  @Column private String motivoCancelamento;

  @Column(nullable = false)
  @NotNull
  private LocalDateTime dataPagamento;

  @Column(nullable = false)
  @NotNull
  @PositiveOrZero
  private BigDecimal valor;

  @Builder.Default
  @Column(name = "numero_tentativas", nullable = false)
  @Min(1)
  @Max(LIMITE_TENTATIVAS)
  private int numeroTentativas = 1;

  public Pagamento(Pedido pedido) {
    this.pedido = pedido;
    this.statusPagamento = StatusPagamento.AGUARDANDO;
    this.dataPagamento = LocalDateTime.now();
    this.valor = pedido.getValorTotal();
    this.numeroTentativas = 1;
  }

  public void confirmarPagamento() {
    this.statusPagamento = StatusPagamento.PAGO;
  }

  public void aguardarPagamento() {
    this.statusPagamento = StatusPagamento.AGUARDANDO;
  }

  public void cancelarPagamento(String motivo) {
    this.statusPagamento = StatusPagamento.CANCELADO;
    this.motivoCancelamento = motivo;
  }

  public void iniciarReprocessamento() {
    this.numeroTentativas++;
    this.statusPagamento = StatusPagamento.AGUARDANDO;
  }

  public void registrarFalha() {
    this.statusPagamento = StatusPagamento.FALHOU;
  }

  public boolean atingiuLimiteTentativas() {
    return numeroTentativas >= LIMITE_TENTATIVAS;
  }
}
