package br.com.wakax.wakax_ecommerce.cliente.application.api.response;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ClienteResponse {
  private UUID id;
  private String nome;
  private StatusPessoa statusPessoa;

  public ClienteResponse(Cliente cliente) {
    this.id = cliente.getId();
    this.nome = cliente.getPessoa().getNome();
    this.statusPessoa = cliente.getPessoa().getStatus();
  }
}
