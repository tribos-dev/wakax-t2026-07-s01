package br.com.wakax.wakax_ecommerce.produto.application.service;

import br.com.wakax.wakax_ecommerce.produto.api.request.ProdutoRequest;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoListResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoResumoResponse;

import java.util.List;
import java.util.UUID;

public interface ProdutoService {
    ProdutoResponse cadastraProduto(ProdutoRequest novoProduto);

    ProdutoListResponse buscaProdutoPorId(UUID idProduto);

    List<ProdutoResumoResponse> listaProduto();
}
