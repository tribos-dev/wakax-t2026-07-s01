package br.com.wakax.wakax_ecommerce.pagamento.application.repository;

import br.com.wakax.wakax_ecommerce.pagamento.domain.TentativaPagamento;

public interface TentativaPagamentoRepository {

  TentativaPagamento salva(TentativaPagamento tentativaPagamento);

  TentativaPagamento buscaPorChaveIdempotencia(String chaveIdempotencia);
}
