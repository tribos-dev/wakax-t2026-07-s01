package br.com.wakax.wakax_ecommerce.pagamento.application.repository;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pagamento.domain.TentativaPagamento;

public interface TentativaPagamentoRepository {

  TentativaPagamento salva(TentativaPagamento tentativaPagamento);

  long contaTentativasDoPagamento(UUID idPagamento);
}
