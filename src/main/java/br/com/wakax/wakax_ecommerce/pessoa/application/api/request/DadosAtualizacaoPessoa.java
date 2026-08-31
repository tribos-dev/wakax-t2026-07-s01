package br.com.wakax.wakax_ecommerce.pessoa.application.api.request;

import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;

import java.util.List;

public interface DadosAtualizacaoPessoa {
    String getNome();

    List<String> getEmails();

    List<String> getTelefones();

    List<Endereco> getEnderecos();
}
