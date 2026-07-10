package br.com.wakax.wakax_ecommerce.cliente.application.api.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import br.com.wakax.wakax_ecommerce.pessoa.application.api.request.DadosPessoa;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest implements DadosPessoa {

  @NotBlank(message = "{validacao.nome.obrigatorio}")
  @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
  private String nome;

  @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "{validacao.cpf.formato}")
  @NotBlank(message = "{validacao.documento.obrigatorio}")
  private String documento;

  @NotNull(message = "{validacao.emails.obrigatorio}")
  private List<@NotBlank @Size(max = 150) String> emails;

  private List<@NotBlank @Size(max = 20) String> telefones;

  @NotNull(message = "{validacao.enderecos.obrigatorio}")
  private List<Endereco> enderecos;
}
