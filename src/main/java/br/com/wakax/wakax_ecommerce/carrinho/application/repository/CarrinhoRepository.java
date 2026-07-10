package br.com.wakax.wakax_ecommerce.carrinho.application.repository;

import java.util.Optional;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.carrinho.domain.Carrinho;

public interface CarrinhoRepository {
  Optional<Carrinho> buscaCarrinhoAtivoDoCliente(UUID idCliente);

  Carrinho salva(Carrinho carrinho);

  Carrinho buscaCarrinhoPorId(UUID idCarrinho);
}
