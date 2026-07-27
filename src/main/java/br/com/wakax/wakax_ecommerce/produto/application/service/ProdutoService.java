package br.com.wakax.wakax_ecommerce.produto.application.service;

import java.util.List;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.produto.api.request.ProdutoRequest;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoListResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoResumoResponse;

public interface ProdutoService {
  ProdutoResponse cadastraProduto(ProdutoRequest novoProduto);

  ProdutoListResponse buscaProdutoPorId(UUID idProduto);

  List<ProdutoResumoResponse> listaProduto();
}
