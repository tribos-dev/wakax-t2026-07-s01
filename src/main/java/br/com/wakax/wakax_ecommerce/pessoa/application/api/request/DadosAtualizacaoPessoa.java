package br.com.wakax.wakax_ecommerce.pessoa.application.api.request;

import java.util.List;

import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;

public interface DadosAtualizacaoPessoa {
  String getNome();

  List<String> getEmails();

  List<String> getTelefones();

  List<Endereco> getEnderecos();
}
