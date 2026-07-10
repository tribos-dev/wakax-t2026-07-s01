package br.com.wakax.wakax_ecommerce.cliente.application.api;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.ClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.ClienteResponse;

@RestController
@RequestMapping("/cliente")
public interface ClienteApi {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  ClienteResponse cadastrarCliente(@RequestBody @Valid ClienteRequest clienteRequest);

  @GetMapping("/{idCliente}")
  ClienteResponse buscaClienteEspecifico(@PathVariable UUID idCliente);
}
