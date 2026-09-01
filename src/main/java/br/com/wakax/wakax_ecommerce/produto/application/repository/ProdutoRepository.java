package br.com.wakax.wakax_ecommerce.produto.application.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.ProdutoDisponivel;

public interface ProdutoRepository {
  Produto salva(Produto produto);

  Produto buscaProdutoPorId(UUID idProduto);

  Page<Produto> listaTodos(Pageable pageable);

  Page<ProdutoDisponivel> listaProdutosAtivosComEstoque(Pageable pageable);
}
