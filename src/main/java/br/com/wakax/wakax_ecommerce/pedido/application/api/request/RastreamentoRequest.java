package br.com.wakax.wakax_ecommerce.pedido.application.api.request;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.wakax.wakax_ecommerce.pedido.domain.StatusRastreamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RastreamentoRequest {
  @NotBlank private String codigo;

  @NotBlank private String transportadora;

  @NotNull private StatusRastreamento statusAtual;

  private LocalDate previsaoEntrega;

  @Valid private List<HistoricoRastreamentoRequest> eventos;
}
