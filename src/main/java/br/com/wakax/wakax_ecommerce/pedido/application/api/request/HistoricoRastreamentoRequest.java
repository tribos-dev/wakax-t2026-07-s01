package br.com.wakax.wakax_ecommerce.pedido.application.api.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.wakax.wakax_ecommerce.pedido.domain.StatusRastreamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoRastreamentoRequest {
  @NotNull private LocalDateTime dataEvento;

  @NotBlank private String local;

  @NotBlank private String descricao;

  @NotNull private StatusRastreamento status;
}
