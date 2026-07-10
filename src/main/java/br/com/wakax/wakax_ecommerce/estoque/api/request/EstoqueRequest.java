package br.com.wakax.wakax_ecommerce.estoque.api.request;

import java.math.BigDecimal;

import javax.validation.constraints.*;

import lombok.*;

@Value
@Builder
public class EstoqueRequest {
  @NotNull @PositiveOrZero private Integer quantidadeDisponivel;

  @NotNull @PositiveOrZero private BigDecimal custoMedio;
  @NotNull @PositiveOrZero private BigDecimal custoTotal;
}
