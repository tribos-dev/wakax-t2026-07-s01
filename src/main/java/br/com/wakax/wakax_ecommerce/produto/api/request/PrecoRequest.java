package br.com.wakax.wakax_ecommerce.produto.api.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import br.com.wakax.wakax_ecommerce.produto.domain.TipoPreco;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PrecoRequest {
  @NotNull private TipoPreco tipo;
  @NotNull private BigDecimal valor;
}
