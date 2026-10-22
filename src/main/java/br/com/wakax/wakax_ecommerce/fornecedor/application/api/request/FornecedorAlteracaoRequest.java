package br.com.wakax.wakax_ecommerce.fornecedor.application.api.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
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
public class FornecedorAlteracaoRequest implements DadosAtualizacaoPessoa {

  @Size(max = 150, message = "{validacao.fornecedor.nome.tamanho}")
  private String nome;

  private List<
          @Email(message = "{validacao.fornecedor.email.invalido}")
          @Size(max = 150, message = "{validacao.fornecedor.email.tamanho}") String>
      emails;

  private List<@Size(max = 20, message = "{validacao.fornecedor.telefone.tamanho}") String>
      telefones;

  @Valid private List<Endereco> enderecos;

  @Size(max = 20, message = "{validacao.inscricaoestadual.tamanho}")
  private String inscricaoEstadual;

  @Size(max = 100, message = "{validacao.razaosocial.tamanho}")
  private String razaoSocial;

  @Size(max = 100, message = "{validacao.nomefantasia.tamanho}")
  private String nomeFantasia;
}
