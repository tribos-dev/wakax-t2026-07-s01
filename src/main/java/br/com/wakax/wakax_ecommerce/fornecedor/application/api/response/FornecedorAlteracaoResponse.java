package br.com.wakax.wakax_ecommerce.fornecedor.application.api.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.fornecedor.domain.Fornecedor;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;
import lombok.Getter;

@Getter
public class FornecedorAlteracaoResponse {

  private final UUID id;
  private final String documento;
  private final String razaoSocial;
  private final String nomeFantasia;
  private final String inscricaoEstadual;
  private final String nome;
  private final List<String> emails;
  private final List<String> telefones;
  private final List<Endereco> enderecos;
  private final LocalDateTime dataEdicao;

  public FornecedorAlteracaoResponse(Fornecedor fornecedor) {
    this.id = fornecedor.getId();
    this.documento = fornecedor.getDocumento();
    this.razaoSocial = fornecedor.getRazaoSocial();
    this.nomeFantasia = fornecedor.getNomeFantasia();
    this.inscricaoEstadual = fornecedor.getInscricaoEstadual();
    this.nome = fornecedor.getPessoa().getNome();
    this.emails = new ArrayList<>(fornecedor.getPessoa().getEmails());
    this.telefones = new ArrayList<>(fornecedor.getPessoa().getTelefones());
    this.enderecos = new ArrayList<>(fornecedor.getPessoa().getEnderecos());
    this.dataEdicao = fornecedor.getDataEdicao();
  }
}
