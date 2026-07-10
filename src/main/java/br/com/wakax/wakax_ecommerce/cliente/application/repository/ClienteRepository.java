package br.com.wakax.wakax_ecommerce.cliente.application.repository;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;

public interface ClienteRepository {
  Cliente salva(Cliente cliente);

  Cliente buscaClientePorId(UUID idCliente);
}
