package br.com.wakax.wakax_ecommerce.pessoa.application.api.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class PessoaRequest {

  @NotBlank(message = "{validacao.nome.obrigatorio}")
  @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
  protected String nome;

  @NotBlank(message = "{validacao.documento.obrigatorio}")
  protected String documento;

  @NotNull(message = "{validacao.emails.obrigatorio}")
  protected List<@NotBlank @Size(max = 150) String> emails;

  protected List<@NotBlank @Size(max = 20) String> telefones;

  @NotNull(message = "{validacao.enderecos.obrigatorio}")
  protected List<Endereco> enderecos;
}
