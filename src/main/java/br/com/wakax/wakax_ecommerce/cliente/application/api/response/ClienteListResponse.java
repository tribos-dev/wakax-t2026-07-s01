package br.com.wakax.wakax_ecommerce.cliente.application.api.response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ClienteListResponse {
  private UUID id;
  private String nome;
  private String email;

  public ClienteListResponse(Cliente cliente) {
    this.id = cliente.getId();
    this.nome = cliente.getPessoa().getNome();
    this.email =
        cliente.getPessoa().getEmails() != null
            ? cliente.getPessoa().getEmails().stream().findFirst().orElse(null)
            : null;
  }

  public static List<ClienteListResponse> converte(List<Cliente> clientes) {
    return clientes.stream().map(ClienteListResponse::new).collect(Collectors.toList());
  }
}
