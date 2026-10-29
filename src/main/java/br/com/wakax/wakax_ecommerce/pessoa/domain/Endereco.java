package br.com.wakax.wakax_ecommerce.pessoa.domain;

import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

  @JsonIgnore
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "pessoa_id", nullable = false)
  private Pessoa pessoa;

  public void atualizar(Endereco enderecoRecebido) {
    ValidationHelper.validaStringEmBranco(enderecoRecebido.getLogradouro())
        .ifPresent(valor -> this.logradouro = valor);
    ValidationHelper.validaStringEmBranco(enderecoRecebido.getNumero())
        .ifPresent(valor -> this.numero = valor);
    ValidationHelper.validaStringEmBranco(enderecoRecebido.getComplemento())
        .ifPresent(valor -> this.complemento = valor);
    ValidationHelper.validaStringEmBranco(enderecoRecebido.getBairro())
        .ifPresent(valor -> this.bairro = valor);
    ValidationHelper.validaStringEmBranco(enderecoRecebido.getCidade())
        .ifPresent(valor -> this.cidade = valor);
    ValidationHelper.validaStringEmBranco(enderecoRecebido.getEstado())
        .ifPresent(valor -> this.estado = valor);
    ValidationHelper.validaStringEmBranco(enderecoRecebido.getCep())
        .ifPresent(valor -> this.cep = valor);
    this.principal = enderecoRecebido.isPrincipal();
  }
}
