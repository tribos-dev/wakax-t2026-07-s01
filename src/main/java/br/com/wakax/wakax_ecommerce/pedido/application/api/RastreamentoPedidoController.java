package br.com.wakax.wakax_ecommerce.pedido.application.api;

import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;

import br.com.wakax.wakax_ecommerce.pedido.application.api.request.RastreamentoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.RastreamentoResponse;
import br.com.wakax.wakax_ecommerce.pedido.application.service.RastreamentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class RastreamentoPedidoController implements RastreamentoPedidoAPI {
  private final RastreamentoService rastreamentoService;

  @Override
  public RastreamentoResponse cadastraRastreamento(UUID idPedido, RastreamentoRequest request) {
    log.debug("[start] RastreamentoPedidoController - cadastraRastreamento");
    RastreamentoResponse response = rastreamentoService.cadastraRastreamento(idPedido, request);
    log.debug("[finish] RastreamentoPedidoController - cadastraRastreamento");
    return response;
  }

    @Override
    public RastreamentoResponse consultaRastreamento(UUID idCliente, UUID idPedido) {
        return null;
    }
}
