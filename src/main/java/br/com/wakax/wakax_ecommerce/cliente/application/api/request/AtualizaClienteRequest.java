package br.com.wakax.wakax_ecommerce.cliente.application.api.request;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.wakax.wakax_ecommerce.pessoa.application.api.request.DadosAtualizacaoPessoa;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AtualizaClienteRequest implements DadosAtualizacaoPessoa {

  @NotBlank(message = "{validacao.nome.obrigatorio}")
  @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
  private String nome;

  @NotNull(message = "{validacao.emails.obrigatorio}")
  private List<@NotBlank @Email(message = "{validacao.email.formato}") @Size(max = 150) String>
      emails;

  private List<@NotBlank @Size(max = 20) String> telefones;

  @NotNull(message = "{validacao.enderecos.obrigatorio}")
  private List<Endereco> enderecos;
}
