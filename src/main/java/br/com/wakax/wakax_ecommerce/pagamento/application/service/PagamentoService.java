package br.com.wakax.wakax_ecommerce.pagamento.application.service;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pagamento.application.api.request.PagamentoRequest;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoPaginadoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;

public interface PagamentoService {

  PagamentoResponse processaPagamento(PagamentoRequest novoPagamento);

  PagamentoResponse buscaPagamentoPorId(UUID idPagamento);

  PagamentoResponse buscaPagamentoPorPedidoId(UUID idPedido);

  PagamentoPaginadoResponse buscaPagamentos(StatusPagamento status, int pagina, int tamanho);

  PagamentoResponse confirmaPagamento(UUID idPagamento);

  PagamentoResponse reprocessaPagamento(UUID idPagamento);
}
