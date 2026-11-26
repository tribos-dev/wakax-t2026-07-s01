package br.com.wakax.wakax_ecommerce.pagamento.application.api.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstornoPagamentoRequest {
  @NotBlank(message = "{validacao.motivo.estorno}")
  @Size(max = 255)
  private String motivo;
}
