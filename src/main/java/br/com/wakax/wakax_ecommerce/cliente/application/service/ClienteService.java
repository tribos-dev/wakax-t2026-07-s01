package br.com.wakax.wakax_ecommerce.cliente.application.service;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.ClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.ClienteResponse;

public interface ClienteService {
  ClienteResponse criaCliente(ClienteRequest clienteRequest);

  ClienteResponse buscaClienteEspecifico(UUID idCliente);


}
