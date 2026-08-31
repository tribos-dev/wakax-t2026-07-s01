package br.com.wakax.wakax_ecommerce.cliente.application.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.AtualizaClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.application.api.request.ClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Pessoa;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;

public final class ClienteDataHelper {

    private ClienteDataHelper() {}

    public static Pessoa criaPessoaValida() {
        return new Pessoa(
                UUID.randomUUID(),
                "João Silva",
                "123.456.789-00",
                new ArrayList<>(List.of("joao@teste.com")),
                new ArrayList<>(List.of("11999999999")),
                new ArrayList<>(),
                StatusPessoa.ATIVO);
    }

    public static Cliente criaClienteValido() {
        return Cliente.builder()
                .id(UUID.randomUUID())
                .pessoa(criaPessoaValida())
                .dataCriacao(LocalDateTime.now())
                .dataEdicao(LocalDateTime.now())
                .build();
    }

    public static ClienteRequest criaClienteRequestValido() {
        return new ClienteRequest(
                "João Silva",
                "123.456.789-00",
                List.of("joao@teste.com"),
                List.of("11999999999"),
                List.of());
    }

    public static Endereco criaEnderecoValido() {
        return Endereco.builder()
                .cep("01234-567")
                .logradouro("Rua das Flores")
                .numero("123")
                .complemento("Apto 45")
                .bairro("Centro")
                .cidade("São Paulo")
                .estado("SP")
                .principal(true)
                .build();
    }

    public static AtualizaClienteRequest criaAtualizaClienteRequestValido() {
        return new AtualizaClienteRequest(
                "João Silva Atualizado",
                List.of("joao.novo@teste.com"),
                List.of("11988888888"),
                new ArrayList<>(List.of(criaEnderecoValido())));
    }
}
