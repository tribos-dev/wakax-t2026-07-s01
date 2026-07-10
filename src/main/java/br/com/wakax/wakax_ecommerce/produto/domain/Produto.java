package br.com.wakax.wakax_ecommerce.produto.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.*;

import br.com.wakax.wakax_ecommerce.produto.api.request.ProdutoRequest;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
  @Id @GeneratedValue private UUID id;

  @Column(length = 150, nullable = false)
  @NotNull
  @Size(max = 150)
  private String descricao;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @NotNull
  private StatusProduto status;

  @Column(nullable = false)
  @NotNull
  private BigDecimal pesoLiquido;

  @Column(nullable = false)
  @NotNull
  private BigDecimal pesoBruto;

  @Column(length = 500)
  @Size(max = 500)
  private String descricaoComplementar;

  @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Preco> precos;

  @Column(length = 50)
  @Size(max = 50)
  private String grupo;

  @Column(length = 10)
  @Size(max = 10)
  private String unidade;

  private Integer estoqueMinimo;
  private Integer estoqueMaximo;

  public Produto(ProdutoRequest request) {
    this.descricao = request.getDescricao();
    this.status = StatusProduto.ATIVO;
    this.pesoLiquido = request.getPesoLiquido();
    this.pesoBruto = request.getPesoBruto();
    this.descricaoComplementar = request.getDescricaoComplementar();
    this.grupo = request.getGrupo();
    this.unidade = request.getUnidade();
    this.estoqueMinimo = request.getEstoqueMinimo();
    this.estoqueMaximo = request.getEstoqueMaximo();
    if (request.getPrecos() != null) {
      this.precos =
          request.getPrecos().stream()
              .map(precoReq -> new Preco(precoReq.getTipo(), precoReq.getValor(), this))
              .collect(Collectors.toList());
    }
  }

  public BigDecimal getPrecoPadrao() {
    if (this.precos.isEmpty()) {
      return BigDecimal.ZERO;
    }
    return this.precos.get(0).getValor();
  }
}
