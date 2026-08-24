package br.com.wakax.wakax_ecommerce.cliente.application.api;

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.ClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.ClienteResponse;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.ClienteResumoResponse;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.PageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

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
}
