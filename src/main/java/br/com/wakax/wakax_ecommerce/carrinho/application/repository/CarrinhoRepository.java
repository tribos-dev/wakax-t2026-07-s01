package br.com.wakax.wakax_ecommerce.carrinho.application.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.carrinho.domain.Carrinho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarrinhoRepository {
  Optional<Carrinho> buscaCarrinhoAtivoDoCliente(UUID idCliente);

  Carrinho salva(Carrinho carrinho);

  Carrinho buscaCarrinhoPorId(UUID idCarrinho);

  Page<Carrinho> buscaTodosCarrinhosDoCliente(UUID idCliente, Pageable pageable);
}
