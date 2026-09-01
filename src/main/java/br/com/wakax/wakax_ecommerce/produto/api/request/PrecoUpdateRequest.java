package br.com.wakax.wakax_ecommerce.produto.api.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import br.com.wakax.wakax_ecommerce.produto.domain.TipoPreco;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class PrecoUpdateRequest {

  @NotNull @Positive private BigDecimal valor;
  @NotNull private TipoPreco tipo;
  @NotBlank private String motivo;
}
