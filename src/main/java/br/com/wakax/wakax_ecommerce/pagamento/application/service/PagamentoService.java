package br.com.wakax.wakax_ecommerce.pagamento.application.service;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pagamento.application.api.request.PagamentoRequest;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResponse;

public interface PagamentoService {

  PagamentoResponse processaPagamento(PagamentoRequest novoPagamento);

  PagamentoResponse buscaPagamentoPorId(UUID idPagamento);

  PagamentoResponse buscaPagamentoPorPedidoId(UUID idPedido);

  PagamentoResponse confirmaPagamento(UUID idPagamento);
}
