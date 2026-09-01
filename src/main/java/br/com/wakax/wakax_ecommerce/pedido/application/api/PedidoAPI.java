package br.com.wakax.wakax_ecommerce.pedido.application.api;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.com.wakax.wakax_ecommerce.pedido.application.api.request.AtualizarStatusPedidoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoResponse;

@RestController
@RequestMapping("/pedido")
public interface PedidoAPI {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  PedidoResponse cadastraPedido(@Valid @RequestBody PedidoRequest pedidoRequest);

  @GetMapping("/{idPedido}")
  PedidoResponse buscaPedidoPorId(@PathVariable UUID idPedido);

  @PatchMapping("/{idPedido}/status")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void atualizarStatus(
      @PathVariable UUID idPedido, @Valid @RequestBody AtualizarStatusPedidoRequest request);
}
