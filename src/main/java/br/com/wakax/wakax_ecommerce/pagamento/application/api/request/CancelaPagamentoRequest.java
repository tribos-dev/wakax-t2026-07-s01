package br.com.wakax.wakax_ecommerce.pagamento.application.api.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelaPagamentoRequest {
  @NotBlank(message = "{validacao.motivo.cancelamento}")
  private String motivo;
}
