package br.com.wakax.wakax_ecommerce.produto.api.response;

import br.com.wakax.wakax_ecommerce.produto.domain.Preco;
import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
public class ProdutoResumoResponse {
    private final String descricao;
    private final StatusProduto status;
    private final BigDecimal preco;
    //private final Date dataCadastro;

    public ProdutoResumoResponse(Produto produto) {
        this.descricao = produto.getDescricao();
        this.status = produto.getStatus();
        this.preco = produto.getPrecoAtual();
        //this.dataCadastro = dataCadastro;
    }
}
