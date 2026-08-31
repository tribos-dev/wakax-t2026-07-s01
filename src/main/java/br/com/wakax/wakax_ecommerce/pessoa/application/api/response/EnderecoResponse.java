package br.com.wakax.wakax_ecommerce.pessoa.application.api.response;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EnderecoResponse {
    private UUID id;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private boolean principal;

    public EnderecoResponse(Endereco endereco) {
        this.id = endereco.getId();
        this.logradouro = endereco.getLogradouro();
        this.numero = endereco.getNumero();
        this.complemento = endereco.getComplemento();
        this.bairro = endereco.getBairro();
        this.cidade = endereco.getCidade();
        this.estado = endereco.getEstado();
        this.cep = endereco.getCep();
        this.principal = endereco.isPrincipal();
    }
}
