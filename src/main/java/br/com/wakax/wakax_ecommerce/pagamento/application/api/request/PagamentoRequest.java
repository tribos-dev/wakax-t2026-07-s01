package br.com.wakax.wakax_ecommerce.pagamento.application.api.request;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoRequest {

  @NotNull(message = "{validacao.pedido}")
  private UUID pedidoId;
}
