package br.com.wakax.wakax_ecommerce.produto.api.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;
import lombok.Getter;

@Getter
public class ProdutoListResponse {
  private final UUID idProduto;
  private final String descricao;
  private final StatusProduto status;
  private final BigDecimal pesoLiquido;
  private final BigDecimal pesoBruto;
  private final String descricaoComplementar;
  private final List<PrecoResponse> precos;
  private final String grupo;
  private final String unidade;
  private final Integer estoqueMinimo;
  private final Integer estoqueMaximo;

  public ProdutoListResponse(Produto produto) {
    this.idProduto = produto.getId();
    this.descricao = produto.getDescricao();
    this.status = produto.getStatus();
    this.pesoLiquido = produto.getPesoLiquido();
    this.pesoBruto = produto.getPesoBruto();
    this.descricaoComplementar = produto.getDescricaoComplementar();
    this.precos = produto.getPrecos().stream().map(PrecoResponse::new).collect(Collectors.toList());
    this.grupo = produto.getGrupo();
    this.unidade = produto.getUnidade();
    this.estoqueMinimo = produto.getEstoqueMinimo();
    this.estoqueMaximo = produto.getEstoqueMaximo();
  }
}
