package br.com.wakax.wakax_ecommerce.produto.application.service;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.produto.api.request.PrecoUpdateRequest;
import br.com.wakax.wakax_ecommerce.produto.api.request.ProdutoRequest;
import br.com.wakax.wakax_ecommerce.produto.api.request.ProdutoUpdateRequest;
import br.com.wakax.wakax_ecommerce.produto.api.response.*;

public interface ProdutoService {
  ProdutoResponse cadastraProduto(ProdutoRequest novoProduto);

  ProdutoListResponse buscaProdutoPorId(UUID idProduto);

  ProdutoPaginadoResponse listaProduto(int pagina, int tamanho);

  ProdutoAtivoPaginadoResponse listarProdutosAtivos(int pagina, int tamanho);

  PrecoResponse atualizaPreco(UUID idProduto, PrecoUpdateRequest precoUpdateRequest);

  ProdutoAtualizaResponse atualizarProduto(UUID idProduto, ProdutoUpdateRequest produtoUpdateRequest);
}
