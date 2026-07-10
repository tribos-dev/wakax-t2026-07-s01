package br.com.wakax.wakax_ecommerce.fornecedor.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.*;

import br.com.wakax.wakax_ecommerce.fornecedor.application.api.request.FornecedorRequest;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Pessoa;
import lombok.*;

@Entity
@Table(name = "fornecedor")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fornecedor {
  @Id @GeneratedValue private UUID id;

  @OneToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(nullable = false, unique = true)
  @NotNull
  private Pessoa pessoa;

  @Column(length = 18, nullable = false, unique = true)
  @NotNull
  private String documento;

  @Column(nullable = false)
  @Size(max = 20)
  private String inscricaoEstadual;

  @Column(nullable = false)
  @Size(max = 100)
  private String razaoSocial;

  @Column(nullable = false)
  @Size(max = 100)
  private String nomeFantasia;

  @Column(nullable = false, name = "data_criacao")
  @NotNull
  private LocalDateTime dataCriacao;

  @Column(nullable = false, name = "data_edicao")
  @NotNull
  private LocalDateTime dataEdicao;

  @PrePersist
  protected void onCreate() {
    dataCriacao = LocalDateTime.now();
    dataEdicao = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    dataEdicao = LocalDateTime.now();
  }

  public Fornecedor(FornecedorRequest request) {
    this.pessoa = Pessoa.criarDe(request);
    this.documento = request.getDocumento();
    this.inscricaoEstadual = request.getInscricaoEstadual();
    this.razaoSocial = request.getRazaoSocial();
    this.nomeFantasia = request.getNomeFantasia();
  }
}
