package br.com.wakax.wakax_ecommerce.pagamento.application.api.request;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelaPagamentoRequest {
  private String motivo;
}
