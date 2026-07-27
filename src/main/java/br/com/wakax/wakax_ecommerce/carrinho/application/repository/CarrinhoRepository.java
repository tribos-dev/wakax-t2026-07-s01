package br.com.wakax.wakax_ecommerce.carrinho.application.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.wakax.wakax_ecommerce.carrinho.domain.Carrinho;

public interface CarrinhoRepository {
  Optional<Carrinho> buscaCarrinhoAtivoDoCliente(UUID idCliente);

  Carrinho salva(Carrinho carrinho);

  Carrinho buscaCarrinhoPorId(UUID idCarrinho);

  Page<Carrinho> buscaTodosCarrinhosDoCliente(UUID idCliente, Pageable pageable);
}
