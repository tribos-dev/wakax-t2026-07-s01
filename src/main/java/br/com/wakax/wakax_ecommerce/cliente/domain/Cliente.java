package br.com.wakax.wakax_ecommerce.cliente.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.ClienteRequest;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Pessoa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

  @Id @GeneratedValue private UUID id;

  @OneToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(nullable = false, unique = true)
  @NotNull
  private Pessoa pessoa;

  @Column(nullable = false, name = "data_criacao")
  @NotNull
  private LocalDateTime dataCriacao;

  @Column(nullable = false, name = "data_edicao")
  @NotNull
  private LocalDateTime dataEdicao;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, name = "status")
  private StatusCliente status;

  @PrePersist
  protected void onCreate() {
    dataCriacao = LocalDateTime.now();
    dataEdicao = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    dataEdicao = LocalDateTime.now();
  }

  public Cliente(ClienteRequest request) {
    this.pessoa = Pessoa.criarDe(request);
    this.status = StatusCliente.ATIVO;
  }

  public void ativar() {
    if (this.status != StatusCliente.INATIVO) {
      throw APIException.build(HttpStatus.CONFLICT, "Cliente já está ativo.");
    }
    this.status = StatusCliente.ATIVO;
  }
}
