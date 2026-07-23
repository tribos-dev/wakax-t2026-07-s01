package br.com.wakax.wakax_ecommerce.cliente.application.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;

public interface ClienteRepository {
  Cliente salva(Cliente cliente);

  Cliente buscaClientePorId(UUID idCliente);

  Page<Cliente> buscarTodos(Pageable pageable);
}
