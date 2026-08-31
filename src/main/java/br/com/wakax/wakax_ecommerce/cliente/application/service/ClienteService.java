package br.com.wakax.wakax_ecommerce.cliente.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.AtualizaClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.application.api.request.ClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.ClienteResponse;
import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;

public interface ClienteService {
  ClienteResponse criaCliente(ClienteRequest clienteRequest);

  ClienteResponse buscaClienteEspecifico(UUID idCliente);

  Page<Cliente> buscarTodosClientes(Pageable pageable);

  ClienteResponse atualizaCliente(UUID idCliente, AtualizaClienteRequest clienteRequest);
}
