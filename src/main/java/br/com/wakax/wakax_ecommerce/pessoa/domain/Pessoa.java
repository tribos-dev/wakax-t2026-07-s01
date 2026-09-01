package br.com.wakax.wakax_ecommerce.pessoa.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.http.HttpStatus;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.pessoa.application.api.request.DadosPessoa;
import br.com.wakax.wakax_ecommerce.pessoa.application.api.request.PessoaRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

  @Id @GeneratedValue private UUID id;

  @Column(length = 150, nullable = false)
  @NotNull
  @Size(max = 150)
  private String nome;

  @Column(length = 20, nullable = false, unique = true)
  @NotNull
  @Size(max = 20)
  private String cpfCnpj;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "pessoa_emails", joinColumns = @JoinColumn(name = "pessoa_id"))
  @Column(name = "emails", length = 255, nullable = false)
  private List<@NotNull @Size(max = 255) String> emails;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "pessoa_telefones", joinColumns = @JoinColumn(name = "pessoa_id"))
  @Column(name = "telefones", length = 30, nullable = false)
  private List<@NotNull @Size(max = 30) String> telefones;

  @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Endereco> enderecos = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @NotNull
  private StatusPessoa status;

  public Pessoa(PessoaRequest pessoaRequest) {
    this.nome = pessoaRequest.getNome();
    this.cpfCnpj = pessoaRequest.getDocumento();
    this.emails = pessoaRequest.getEmails();
    this.telefones = pessoaRequest.getTelefones();
    this.status = StatusPessoa.ATIVO;
    this.enderecos = new ArrayList<>();
    Optional.ofNullable(pessoaRequest.getEnderecos()).ifPresent(this.enderecos::addAll);
    vincularEnderecos();
  }

  public static Pessoa criarDe(DadosPessoa dadosPessoa) {
    Pessoa pessoa = new Pessoa();
    pessoa.nome = dadosPessoa.getNome();
    pessoa.cpfCnpj = dadosPessoa.getDocumento();
    pessoa.emails = dadosPessoa.getEmails();
    pessoa.telefones = dadosPessoa.getTelefones();
    pessoa.status = StatusPessoa.ATIVO;
    pessoa.enderecos = new ArrayList<>();
    Optional.ofNullable(dadosPessoa.getEnderecos()).ifPresent(pessoa.enderecos::addAll);
    pessoa.vincularEnderecos();
    return pessoa;
  }

  private void vincularEnderecos() {
    if (this.enderecos != null) {
      this.enderecos.forEach(endereco -> endereco.setPessoa(this));
    }
  }

  public void ativar() {
    if (this.status != StatusPessoa.INATIVO) {
      throw APIException.build(HttpStatus.CONFLICT, "Cliente já está ativo.");
    }
    this.status = StatusPessoa.ATIVO;
  }

  public void desativar() {
    if (this.status != StatusPessoa.ATIVO) {
      throw APIException.build(HttpStatus.CONFLICT, "Cliente já está inativo.");
    }
    this.status = StatusPessoa.INATIVO;
  }
}
