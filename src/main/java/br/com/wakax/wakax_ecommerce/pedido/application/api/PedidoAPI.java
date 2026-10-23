package br.com.wakax.wakax_ecommerce.pedido.application.api;

import java.util.UUID;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.com.wakax.wakax_ecommerce.pedido.application.api.request.AtualizarStatusPedidoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.EnderecoUpdateRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoFilterRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoListResponse;
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

  @PatchMapping("/{idPedido}/endereco")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void alteraEnderecoPedido(
      @PathVariable UUID idPedido, @Valid @RequestBody EnderecoUpdateRequest enderecoUpdateRequest);

  @GetMapping("/cliente/{idCliente}")
  @ResponseStatus(HttpStatus.OK)
  Page<PedidoListResponse> buscaPedidoPorClienteId(
      @ParameterObject @ModelAttribute PedidoFilterRequest loteFilterRequest,
      @ParameterObject
          @PageableDefault(
              page = 0,
              size = 10,
              sort = "dataPedido",
              direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable UUID idCliente);
}
