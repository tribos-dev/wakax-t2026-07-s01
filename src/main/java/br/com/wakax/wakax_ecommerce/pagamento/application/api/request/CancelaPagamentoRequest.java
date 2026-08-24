package br.com.wakax.wakax_ecommerce.pagamento.application.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelaPagamentoRequest {
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String motivo;
}
