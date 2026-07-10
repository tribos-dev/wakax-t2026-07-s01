package br.com.wakax.wakax_ecommerce.fornecedor.application.api.request;

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
public class FornecedorRequest implements DadosPessoa {

  @NotBlank(message = "{validacao.nome.obrigatorio}")
  @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
  private String nome;

  @Pattern(
      regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2})$",
      message = "{validacao.documento.formato}")
  @NotBlank(message = "{validacao.documento.obrigatorio}")
  private String documento;

  @NotNull(message = "{validacao.emails.obrigatorio}")
  private List<@NotBlank @Size(max = 150) String> emails;

  private List<@NotBlank @Size(max = 20) String> telefones;

  @NotNull(message = "{validacao.enderecos.obrigatorio}")
  private List<Endereco> enderecos;

  @Size(max = 20, message = "{validacao.inscricaoestadual.tamanho}")
  private String inscricaoEstadual;

  @NotBlank(message = "{validacao.razaosocial.obrigatorio}")
  @Size(max = 100, message = "{validacao.razaosocial.tamanho}")
  private String razaoSocial;

  @Size(max = 100, message = "{validacao.nomefantasia.tamanho}")
  private String nomeFantasia;
}
