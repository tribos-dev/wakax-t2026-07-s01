package br.com.wakax.wakax_ecommerce.pessoa.application.api.response;

import java.util.List;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Pessoa;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PessoaResponse {
  private UUID id;
  private String nome;
  private String cpfCnpj;
  private List<String> emails;
  private List<String> telefones;
  private List<Endereco> enderecos;
  private StatusPessoa status;

  public PessoaResponse(Pessoa pessoa) {
    this.id = pessoa.getId();
    this.nome = pessoa.getNome();
    this.cpfCnpj = pessoa.getCpfCnpj();
    this.emails = pessoa.getEmails();
    this.telefones = pessoa.getTelefones();
    this.enderecos = pessoa.getEnderecos();
    this.status = pessoa.getStatus();
  }
}
