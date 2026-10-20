package br.com.wakax.wakax_ecommerce.pedido.application.api;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import br.com.wakax.wakax_ecommerce.pedido.application.api.request.AtualizarStatusPedidoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.EnderecoUpdateRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoFilterRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoListResponse;
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

  @Override
  public void alteraEnderecoPedido(UUID idPedido, EnderecoUpdateRequest enderecoUpdateRequest) {
    log.debug("[start] PedidoController - alteraEnderecoPedido");
    pedidoService.alteraEnderecoEntrega(idPedido, enderecoUpdateRequest);
    log.debug("[finish] PedidoController - alteraEnderecoPedido");
  }

  @Override
  public Page<PedidoListResponse> buscaPedidoPorClienteId(
      PedidoFilterRequest loteFilterRequest, Pageable pageable, UUID idCliente) {
    log.info("[start] PedidoController - buscaClientePorId");
    log.info("[info] idCliente: {}", idCliente);
    log.info("[info] filtro: {}", loteFilterRequest.getStatus());
    Page<PedidoListResponse> pedidoListResponses =
        pedidoService.buscarTodosPedidosComFiltro(loteFilterRequest, pageable, idCliente);
    log.info("[finish] PedidoController - buscaClientePorId");
    return pedidoListResponses;
  }
}
