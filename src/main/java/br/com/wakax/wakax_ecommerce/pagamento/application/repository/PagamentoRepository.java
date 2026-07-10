package br.com.wakax.wakax_ecommerce.pagamento.application.repository;

import java.util.Optional;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;

public interface PagamentoRepository {

  Pagamento salva(Pagamento pagamento);

  Pagamento buscaPagamentoPorId(UUID idPagamento);

  Optional<Pagamento> buscaPagamentoPorPedidoId(UUID pedidoId);
}
