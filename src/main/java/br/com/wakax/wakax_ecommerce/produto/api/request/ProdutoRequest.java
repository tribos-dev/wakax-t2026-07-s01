package br.com.wakax.wakax_ecommerce.produto.api.request;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ProdutoRequest {
  @NotBlank
  @Size(max = 150)
  private String descricao;

  @NotNull private BigDecimal pesoLiquido;

  @NotNull private BigDecimal pesoBruto;

  @Size(max = 500)
  private String descricaoComplementar;

  @NotNull private BigDecimal preco;

  @Size(max = 50)
  private String grupo;

  @Size(max = 10)
  private String unidade;

  private Integer estoqueMinimo;
  private Integer estoqueMaximo;

  private List<PrecoRequest> precos;
}
