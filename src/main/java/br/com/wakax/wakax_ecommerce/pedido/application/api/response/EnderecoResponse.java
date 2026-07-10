package br.com.wakax.wakax_ecommerce.pedido.application.api.response;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;
import lombok.Getter;

@Getter
public class EnderecoResponse {
  private UUID id;
  private String logradouro;
  private String numero;
  private String complemento;
  private String bairro;
  private String cidade;
  private String estado;
  private String cep;

  public EnderecoResponse(Endereco endereco) {
    this.id = endereco.getId();
    this.logradouro = endereco.getLogradouro();
    this.numero = endereco.getNumero();
    this.complemento = endereco.getComplemento();
    this.bairro = endereco.getBairro();
    this.cidade = endereco.getCidade();
    this.estado = endereco.getEstado();
    this.cep = endereco.getCep();
  }
}
