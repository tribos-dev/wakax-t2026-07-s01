package br.com.wakax.wakax_ecommerce.cliente.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.ClienteRequest;
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

  @Column(name = "data_ativacao")
  private LocalDateTime dataAtivacao;

  @Column(name = "data_desativacao")
  private LocalDateTime dataDesativacao;

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
  }

  public void ativar() {
    pessoa.ativar();
    this.dataAtivacao = LocalDateTime.now();
  }

  public void desativar() {
    pessoa.desativar();
    this.dataDesativacao = LocalDateTime.now();
  }
}
