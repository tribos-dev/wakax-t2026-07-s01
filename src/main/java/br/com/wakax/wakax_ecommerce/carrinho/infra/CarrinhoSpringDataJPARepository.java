package br.com.wakax.wakax_ecommerce.carrinho.infra;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wakax.wakax_ecommerce.carrinho.domain.Carrinho;
import br.com.wakax.wakax_ecommerce.carrinho.domain.StatusCarrinho;

public interface CarrinhoSpringDataJPARepository extends JpaRepository<Carrinho, UUID> {

  Carrinho findByClienteIdAndStatusCarrinho(UUID idCliente, StatusCarrinho status);

  Page<Carrinho> findByClienteId(UUID idCliente, Pageable pageable);
}
