package br.com.wakax.wakax_ecommerce.pagamento.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.http.HttpStatus;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {
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

  public Pagamento(Pedido pedido) {
    this.pedido = pedido;
    this.statusPagamento = StatusPagamento.AGUARDANDO;
    this.dataPagamento = LocalDateTime.now();
    this.valor = pedido.getValorTotal();
  }

  public void confirmarPagamento() {
    this.statusPagamento = StatusPagamento.PAGO;
  }

  public void aguardarPagamento() {
    this.statusPagamento = StatusPagamento.AGUARDANDO;
  }

  public void cancelarPagamento(String motivo) {
    if (this.statusPagamento != StatusPagamento.AGUARDANDO) {
      throw new APIException(
          HttpStatus.CONFLICT, ErrorCode.PAGAMENTO_JA_PROCESSADO, this.statusPagamento);
    }

    this.statusPagamento = StatusPagamento.FALHOU;
    this.motivoCancelamento = motivo;
  }
}
