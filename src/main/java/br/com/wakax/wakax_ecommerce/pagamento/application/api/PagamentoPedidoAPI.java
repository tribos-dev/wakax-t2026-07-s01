package br.com.wakax.wakax_ecommerce.pagamento.application.api;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResponse;

@RestController
@RequestMapping("/pedidos")
public interface PagamentoPedidoAPI {

  @GetMapping("/{idPedido}/pagamento")
  PagamentoResponse buscaPagamentoPorPedidoId(@PathVariable UUID idPedido);
}
