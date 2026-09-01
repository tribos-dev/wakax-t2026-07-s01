package br.com.wakax.wakax_ecommerce.cliente.application.api.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import br.com.wakax.wakax_ecommerce.pessoa.application.api.response.EnderecoResponse;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ClienteResponse {
  private UUID id;
  private String nome;
  private String documento;
  private List<String> emails;
  private List<String> telefones;
  private List<EnderecoResponse> enderecos;
  private StatusPessoa statusPessoa;

  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private LocalDateTime dataCriacao;

  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private LocalDateTime dataEdicao;

  public ClienteResponse(Cliente cliente) {
    this.id = cliente.getId();
    this.nome = cliente.getPessoa().getNome();
    this.documento = cliente.getPessoa().getCpfCnpj();
    this.emails = new ArrayList<>(cliente.getPessoa().getEmails());
    this.telefones = new ArrayList<>(cliente.getPessoa().getTelefones());
    this.enderecos =
        cliente.getPessoa().getEnderecos().stream()
            .map(EnderecoResponse::new)
            .collect(Collectors.toList());
    this.statusPessoa = cliente.getPessoa().getStatus();
    this.dataCriacao = cliente.getDataCriacao();
    this.dataEdicao = cliente.getDataEdicao();
  }
}
