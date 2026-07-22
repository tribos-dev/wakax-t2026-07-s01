package br.com.wakax.wakax_ecommerce.produto.application.repository;

import java.util.List;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.ProdutoDisponivel;

public interface ProdutoRepository {

  Produto salva(Produto produto);

  List<ProdutoDisponivel> listaProdutosAtivosComEstoque();

  Produto buscaProdutoPorId(UUID idProduto);
}
