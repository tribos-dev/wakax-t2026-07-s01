package br.com.wakax.wakax_ecommerce.produto.api.response;

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Getter
public class ProdutoAtualizaResponse{
    private UUID idProduto;
    private String descricao;
    private StatusProduto status;
    private BigDecimal pesoLiquido;
    private BigDecimal pesoBruto;
    private String descricaoComplementar;
    private List<PrecoResponse> precos;
    private String grupo;
    private String unidade;
    private Integer estoqueMinimo;
    private Integer estoqueMaximo;
    private LocalDateTime dataDeCriacao;
    private LocalDateTime dataDeAtualizacao;

    public ProdutoAtualizaResponse(Produto produto){
        this.idProduto = produto.getId();
        this.descricao = produto.getDescricao();
        this.status = produto.getStatus();
        this.pesoLiquido = produto.getPesoLiquido();
        this.pesoBruto = produto.getPesoBruto();
        this.descricaoComplementar = produto.getDescricaoComplementar();
        this.precos = produto.getPrecos() == null
                ? List.of()
                : produto.getPrecos()
                .stream()
                .map(PrecoResponse:: new )
                .collect(Collectors.toList());
        this.grupo = produto.getGrupo();
        this.unidade = produto.getUnidade();
        this.estoqueMinimo = produto.getEstoqueMinimo();
        this.estoqueMaximo = produto.getEstoqueMaximo();
        this.dataDeCriacao = produto.getDataCriacao();
        this.dataDeAtualizacao = produto.getDataAtualizacao();
    }


}
