package br.com.wakax.wakax_ecommerce.cliente.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.ClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.ClienteResponse;
import br.com.wakax.wakax_ecommerce.cliente.application.repository.ClienteRepository;
import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ClienteApplicationService implements ClienteService {
  private final ClienteRepository clienteRepository;

  @Override
  public ClienteResponse criaCliente(ClienteRequest clienteRequest) {
    log.info("[start] ClienteApplicationService - criaCliente");
    Cliente clienteCriado = clienteRepository.salva(new Cliente(clienteRequest));
    log.debug("[finish] ClienteApplicationService - criaCliente");
    return new ClienteResponse(clienteCriado);
  }

  @Override
  public ClienteResponse buscaClienteEspecifico(UUID idCliente) {
    log.info("[start] ClienteApplicationService - buscaClienteEspecifico");
    Cliente cliente = clienteRepository.buscaClientePorId(idCliente);
    log.debug("[finish] ClienteApplicationService - buscaClienteEspecifico");
    return new ClienteResponse(cliente);
  }
}
