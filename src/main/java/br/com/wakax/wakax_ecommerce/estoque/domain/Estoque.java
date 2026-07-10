package br.com.wakax.wakax_ecommerce.estoque.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.http.HttpStatus;

import br.com.wakax.wakax_ecommerce.estoque.api.request.EstoqueRequest;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Estoque {
  @Id @GeneratedValue private UUID id;

  @OneToOne(optional = false)
  @JoinColumn(nullable = false)
  @NotNull
  private Produto produto;

  @Column(nullable = false)
  @NotNull
  @PositiveOrZero
  private Integer quantidadeDisponivel;

  @Column(nullable = false)
  @NotNull
  @PositiveOrZero
  private BigDecimal custoMedio;

  @Column(nullable = false)
  @NotNull
  @PositiveOrZero
  private BigDecimal custoTotal;

  public Estoque(EstoqueRequest request, Produto produto) {
    this.produto = produto;
    this.quantidadeDisponivel = request.getQuantidadeDisponivel();
    this.custoMedio = request.getCustoMedio();
    this.custoTotal = request.getCustoTotal();
  }

  public void adicionaQuantidade(Integer quantidade, BigDecimal custoUnitario) {
    validaQuantidade(quantidade);
    validaCustoUnitario(custoUnitario);

    BigDecimal valorAdicionado = custoUnitario.multiply(BigDecimal.valueOf(quantidade));
    this.custoTotal = this.custoTotal.add(valorAdicionado);
    this.quantidadeDisponivel += quantidade;
    this.custoMedio = calculaCustoMedio(this.custoTotal, this.quantidadeDisponivel);
  }

  private BigDecimal calculaCustoMedio(BigDecimal novoCustoTotal, Integer quantidade) {
    if (quantidade <= 0) {
      return BigDecimal.ZERO;
    }
    return novoCustoTotal.divide(BigDecimal.valueOf(quantidade), 2, RoundingMode.HALF_UP);
  }

  public void removeQuantidade(Integer quantidade) {
    validaQuantidade(quantidade);
    verificaSeQuantidadeEhSuficiente(quantidade);

    this.quantidadeDisponivel -= quantidade;

    if (this.quantidadeDisponivel > 0) {
      this.custoTotal = this.custoMedio.multiply(BigDecimal.valueOf(this.quantidadeDisponivel));
    } else {
      this.custoTotal = BigDecimal.ZERO;
      this.custoMedio = BigDecimal.ZERO;
    }
  }

  private void verificaSeQuantidadeEhSuficiente(Integer quantidade) {
    if (!temQuantidadeDisponivel(quantidade)) {
      throw new APIException(HttpStatus.BAD_REQUEST, ErrorCode.QUANTIDADE_INSUFICIENTE_ESTOQUE);
    }
  }

  public boolean temQuantidadeDisponivel(Integer quantidade) {
    return this.quantidadeDisponivel >= quantidade;
  }

  public void reservaQuantidade(Integer quantidade) {
    validaQuantidade(quantidade);
    verificaSeQuantidadeEhSuficiente(quantidade);
    this.quantidadeDisponivel -= quantidade;
  }

  public void liberaReserva(Integer quantidade) {
    validaQuantidade(quantidade);
    this.quantidadeDisponivel += quantidade;
  }

  private void validaQuantidade(Integer quantidade) {
    if (quantidade == null || quantidade == 0) {
      throw new APIException(HttpStatus.BAD_REQUEST, ErrorCode.QUANTIDADE_INVALIDA);
    }
  }

  private void validaCustoUnitario(BigDecimal custoUnitario) {
    if (custoUnitario == null || custoUnitario.compareTo(BigDecimal.ZERO) < 0) {
      throw new APIException(HttpStatus.BAD_REQUEST, ErrorCode.CUSTO_INVALIDO);
    }
  }
}
