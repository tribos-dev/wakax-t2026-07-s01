package br.com.wakax.wakax_ecommerce.fornecedor.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.fornecedor.application.api.request.FornecedorAlteracaoRequest;
import br.com.wakax.wakax_ecommerce.fornecedor.domain.Fornecedor;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Pessoa;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;

public class FornecedorDataHelper {
  public static Fornecedor criarFornecedor(
      String razaoSocial, String documento, StatusPessoa status) {
    Pessoa pessoa = new Pessoa();
    pessoa.setNome("Nome " + razaoSocial);
    pessoa.setCpfCnpj(documento);
    pessoa.setEmails(List.of("contato@empresa.com"));
    pessoa.setTelefones(List.of("(11) 99999-9999"));
    pessoa.setStatus(status);

    return Fornecedor.builder()
        .id(UUID.randomUUID())
        .pessoa(pessoa)
        .documento(documento)
        .razaoSocial(razaoSocial)
        .nomeFantasia("Fantasia " + razaoSocial)
        .inscricaoEstadual("123.456.789")
        .dataCriacao(LocalDateTime.now())
        .dataEdicao(LocalDateTime.now())
        .build();
  }

  public static Fornecedor criarFornecedorAtivo() {
    return criarFornecedor("Empresa Ativa LTDA", "11.111.111/0001-11", StatusPessoa.ATIVO);
  }

  public static Fornecedor criarFornecedorInativo() {
    return criarFornecedor("Empresa Inativa LTDA", "22.222.222/0001-22", StatusPessoa.INATIVO);
  }

  public static FornecedorAlteracaoRequest criarRequestAlteracao(
      String nome,
      List<String> emails,
      List<String> telefones,
      List<Endereco> enderecos,
      String inscricaoEstadual,
      String razaoSocial,
      String nomeFantasia) {
    return new FornecedorAlteracaoRequest(
        nome,
        emails,
        telefones,
        enderecos,
        inscricaoEstadual,
        razaoSocial,
        nomeFantasia);
  }

  public static FornecedorAlteracaoRequest criarRequestAlteracaoRazaoSocialENomeFantasia() {
    return criarRequestAlteracao(
         null, null, null, null, null, "Nova Razao Social", "Nova Fantasia");
  }

  public static FornecedorAlteracaoRequest criarRequestAlteracaoEmailsETelefones(
      List<String> emails, List<String> telefones) {

    return criarRequestAlteracao(null, emails, telefones, null, null, null, null);
  }
}
