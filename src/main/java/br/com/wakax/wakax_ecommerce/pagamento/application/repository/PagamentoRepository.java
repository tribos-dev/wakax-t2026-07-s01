package br.com.wakax.wakax_ecommerce.pagamento.application.repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResumoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;

public interface PagamentoRepository {

  Pagamento salva(Pagamento pagamento);

  Pagamento buscaPagamentoPorId(UUID idPagamento);

  Optional<Pagamento> buscaPagamentoPorPedidoId(UUID pedidoId);

  Page<PagamentoResumoResponse> buscaPagamentos(StatusPagamento status, Pageable pageable);

  BigDecimal somaValores(StatusPagamento status);
}
