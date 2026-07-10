package br.com.wakax.wakax_ecommerce.pessoa.application.api.request;

import java.util.List;

import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;

public interface DadosPessoa {
  String getNome();

  String getDocumento();

  List<String> getEmails();

  List<String> getTelefones();

  List<Endereco> getEnderecos();
}
