package br.com.wakax.wakax_ecommerce.produto.application.service;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.produto.api.request.ProdutoRequest;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoAtivoPaginadoResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoListResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoPaginadoResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoResponse;

public interface ProdutoService {
  ProdutoResponse cadastraProduto(ProdutoRequest novoProduto);

  ProdutoListResponse buscaProdutoPorId(UUID idProduto);

  ProdutoPaginadoResponse listaProduto(int pagina, int tamanho);

  ProdutoAtivoPaginadoResponse listarProdutosAtivos(int pagina, int tamanho);
}
