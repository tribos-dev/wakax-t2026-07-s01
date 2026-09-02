package br.com.wakax.wakax_ecommerce.cliente.application.api;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.AtualizaClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.application.api.request.ClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.ClienteResponse;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.ClienteResumoResponse;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.PageResponse;

@RestController
@RequestMapping("/cliente")
public interface ClienteApi {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  ClienteResponse cadastrarCliente(@RequestBody @Valid ClienteRequest clienteRequest);

  @GetMapping("/{idCliente}")
  ClienteResponse buscaClienteEspecifico(@PathVariable UUID idCliente);

  @GetMapping("/clientes")
  PageResponse<ClienteResumoResponse> buscarTodosClientes(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size);

  @PatchMapping("/{idCliente}/ativar")
  @ResponseStatus(HttpStatus.OK)
  ClienteResponse ativarCliente(@PathVariable UUID idCliente);

  @PutMapping("/{idCliente}")
  ClienteResponse atualizarCliente(
      @PathVariable UUID idCliente, @RequestBody @Valid AtualizaClienteRequest clienteRequest);

  @PatchMapping("/{idCliente}/desativar")
  @ResponseStatus(HttpStatus.OK)
  ClienteResponse desativarCliente(@PathVariable UUID idCliente);
}
