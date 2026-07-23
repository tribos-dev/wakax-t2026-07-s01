package br.com.wakax.wakax_ecommerce.cliente.application.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ClienteResumoResponse {

  private UUID id;
  private String nome;
  private String email;
  private StatusPessoa status;

  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private LocalDateTime dataCriacao;

  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private LocalDateTime dataEdicao;

  public ClienteResumoResponse(Cliente cliente) {
    this.id = cliente.getId();
    this.nome = cliente.getPessoa().getNome();
    this.email =
        cliente.getPessoa().getEmails() != null
            ? cliente.getPessoa().getEmails().stream().findFirst().orElse(null)
            : null;
    this.status = cliente.getPessoa().getStatus();
    this.dataCriacao = cliente.getDataCriacao();
    this.dataEdicao = cliente.getDataEdicao();
  }
}
