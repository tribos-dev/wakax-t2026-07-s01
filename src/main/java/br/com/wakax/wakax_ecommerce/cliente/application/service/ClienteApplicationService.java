package br.com.wakax.wakax_ecommerce.cliente.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.AtualizaClienteRequest;
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
  @Transactional
  public ClienteResponse criaCliente(ClienteRequest clienteRequest) {
    log.debug("[start] ClienteApplicationService - criaCliente");
    Cliente clienteCriado = clienteRepository.salva(new Cliente(clienteRequest));
    log.debug("[finish] ClienteApplicationService - criaCliente");
    return new ClienteResponse(clienteCriado);
  }

  @Override
  @Transactional(readOnly = true)
  public ClienteResponse buscaClienteEspecifico(UUID idCliente) {
    log.debug("[start] ClienteApplicationService - buscaClienteEspecifico");
    Cliente cliente = clienteRepository.buscaClientePorId(idCliente);
    log.debug("[finish] ClienteApplicationService - buscaClienteEspecifico");
    return new ClienteResponse(cliente);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Cliente> buscarTodosClientes(Pageable pageable) {
    log.debug("[start] ClienteApplicationService - buscarTodosClientes");
    Page<Cliente> clientes = clienteRepository.buscarTodos(pageable);
    log.debug("[finish] ClienteApplicationService - buscarTodosClientes");
    return clientes;
  }

  @Override
  public ClienteResponse ativarCliente(UUID idCliente) {
    log.debug("[start] ClienteApplicationService - ativarCliente");
    Cliente cliente = clienteRepository.buscaClientePorId(idCliente);
    cliente.ativar();
    clienteRepository.salva(cliente);
    log.debug("[finish] ClienteApplicationService - ativarCliente");
    return new ClienteResponse(cliente);
  }

  @Override
  @Transactional
  public ClienteResponse atualizaCliente(UUID idCliente, AtualizaClienteRequest clienteRequest) {
    log.debug("[start] ClienteApplicationService - atualizaCliente");
    Cliente cliente = clienteRepository.buscaClientePorId(idCliente);
    cliente.atualizar(clienteRequest);
    Cliente clienteAtualizado = clienteRepository.salva(cliente);
    log.debug("[finish] ClienteApplicationService - atualizaCliente");
    return new ClienteResponse(clienteAtualizado);
  }
}
