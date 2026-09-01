package br.com.wakax.wakax_ecommerce.pedido.application.api;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RestController;

import br.com.wakax.wakax_ecommerce.pedido.application.api.request.AtualizarStatusPedidoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoResponse;
import br.com.wakax.wakax_ecommerce.pedido.application.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class PedidoController implements PedidoAPI {
  private final PedidoService pedidoService;

  @Override
  public PedidoResponse cadastraPedido(@Valid PedidoRequest pedidoRequest) {
    log.debug("[start] PedidoController - cadastraPedido");
    PedidoResponse response = pedidoService.cadastraPedido(pedidoRequest);
    log.debug("[finish] PedidoController - cadastraPedido");
    return response;
  }

  @Override
  public PedidoResponse buscaPedidoPorId(UUID idPedido) {
    log.debug("[start] PedidoController - buscaPedidoPorId");
    PedidoResponse response = pedidoService.buscaPedidoPorId(idPedido);
    log.debug("[finish] PedidoController - buscaPedidoPorId");
    return response;
  }

  @Override
  public void atualizarStatus(UUID idPedido, AtualizarStatusPedidoRequest request) {
    log.debug("[start] PedidoController - atualizarStatus");
    pedidoService.atualizarStatus(idPedido, request.getStatus());
    log.debug("[finish] PedidoController - atualizarStatus");
  }
}
