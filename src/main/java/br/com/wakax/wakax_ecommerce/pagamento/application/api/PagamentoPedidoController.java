package br.com.wakax.wakax_ecommerce.pagamento.application.api;

import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;

import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class PagamentoPedidoController implements PagamentoPedidoAPI {

  private final PagamentoService pagamentoService;

  @Override
  public PagamentoResponse buscaPagamentoPorPedidoId(UUID idPedido) {
    log.debug("[start] PagamentoPedidoController - buscaPagamentoPorPedidoId");
    PagamentoResponse response = pagamentoService.buscaPagamentoPorPedidoId(idPedido);
    log.debug("[finish] PagamentoPedidoController - buscaPagamentoPorPedidoId");
    return response;
  }
}
