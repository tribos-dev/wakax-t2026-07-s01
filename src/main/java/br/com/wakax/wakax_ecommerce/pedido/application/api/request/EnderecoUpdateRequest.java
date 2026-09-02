package br.com.wakax.wakax_ecommerce.pedido.application.api.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoUpdateRequest {

  @Size(max = 150)
  private String logradouro;

  @NotBlank
  @Size(max = 20)
  private String numero;

  @Size(max = 100)
  private String complemento;

  @NotBlank
  @Size(max = 100)
  private String bairro;

  @NotBlank
  @Size(max = 100)
  private String cidade;

  @NotBlank
  @Size(max = 50)
  private String estado;

  @NotBlank
  @Size(max = 20)
  private String cep;
}
