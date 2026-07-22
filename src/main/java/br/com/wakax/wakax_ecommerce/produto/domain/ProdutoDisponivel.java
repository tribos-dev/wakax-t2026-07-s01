package br.com.wakax.wakax_ecommerce.produto.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProdutoDisponivel {

  private final Produto produto;
  private final Integer quantidadeDisponivel;
}
