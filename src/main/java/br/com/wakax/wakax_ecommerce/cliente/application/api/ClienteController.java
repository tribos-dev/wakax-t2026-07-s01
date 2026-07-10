package br.com.wakax.wakax_ecommerce.cliente.application.api;

import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.ClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.ClienteResponse;
import br.com.wakax.wakax_ecommerce.cliente.application.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ClienteController implements ClienteApi {

  private final ClienteService clienteService;

  @Override
  public ClienteResponse cadastrarCliente(ClienteRequest clienteRequest) {
    log.info("[start] ClienteController - cadastrarCliente");
    ClienteResponse clienteCriado = clienteService.criaCliente(clienteRequest);
    log.debug("[finish] ClienteController - cadastrarCliente");
    return clienteCriado;
  }

  @Override
  public ClienteResponse buscaClienteEspecifico(UUID idCliente) {
    log.info("[start] ClienteController - buscaClienteEspecifico");
    ClienteResponse cliente = clienteService.buscaClienteEspecifico(idCliente);
    log.debug("[finish] ClienteController - buscaClienteEspecifico");
    return cliente;
  }
}
