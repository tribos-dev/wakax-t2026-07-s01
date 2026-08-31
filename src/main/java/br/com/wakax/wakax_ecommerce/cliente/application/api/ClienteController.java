package br.com.wakax.wakax_ecommerce.cliente.application.api;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.AtualizaClienteRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RestController;

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.ClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.ClienteResponse;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.ClienteResumoResponse;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.PageResponse;
import br.com.wakax.wakax_ecommerce.cliente.application.service.ClienteService;
import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ClienteController implements ClienteApi {

  private final ClienteService clienteService;

  @Override
  public ClienteResponse cadastrarCliente(ClienteRequest clienteRequest) {
    log.debug("[start] ClienteController - cadastrarCliente");
    ClienteResponse clienteCriado = clienteService.criaCliente(clienteRequest);
    log.debug("[finish] ClienteController - cadastrarCliente");
    return clienteCriado;
  }

  @Override
  public ClienteResponse buscaClienteEspecifico(UUID idCliente) {
    log.debug("[start] ClienteController - buscaClienteEspecifico");
    ClienteResponse cliente = clienteService.buscaClienteEspecifico(idCliente);
    log.debug("[finish] ClienteController - buscaClienteEspecifico");
    return cliente;
  }

  @Override
  public PageResponse<ClienteResumoResponse> buscarTodosClientes(int page, int size) {
    log.debug("[start] ClienteController - buscarTodosClientes");
    Page<Cliente> clientes = clienteService.buscarTodosClientes(PageRequest.of(page, size));
    Page<ClienteResumoResponse> response = clientes.map(ClienteResumoResponse::new);
    log.debug("[finish] ClienteController - buscarTodosClientes");
    return PageResponse.from(response);
  }

  @Override
  public ClienteResponse atualizarCliente(UUID idCliente, AtualizaClienteRequest clienteRequest) {
    log.debug("[start] ClienteController - atualizarCliente");
    ClienteResponse clienteAtualizado = clienteService.atualizaCliente(idCliente, clienteRequest);
    log.debug("[finish] ClienteController - atualizarCliente");
    return clienteAtualizado;
  }
}
