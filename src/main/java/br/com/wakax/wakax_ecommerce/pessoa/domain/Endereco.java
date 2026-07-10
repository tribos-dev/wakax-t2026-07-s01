package br.com.wakax.wakax_ecommerce.pessoa.domain;

import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {
  @Id @GeneratedValue private UUID id;

  @Column(length = 150)
  @Size(max = 150)
  private String logradouro;

  @Column(length = 20)
  @Size(max = 20)
  private String numero;

  @Column(length = 100)
  @Size(max = 100)
  private String complemento;

  @Column(length = 100)
  @Size(max = 100)
  private String bairro;

  @Column(length = 100)
  @Size(max = 100)
  private String cidade;

  @Column(length = 50)
  @Size(max = 50)
  private String estado;

  @Column(length = 20)
  @Size(max = 20)
  private String cep;

  @Column(nullable = false)
  @NotNull
  private boolean principal;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "pessoa_id", nullable = false)
  private Pessoa pessoa;
}
