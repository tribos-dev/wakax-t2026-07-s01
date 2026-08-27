package br.com.wakax.wakax_ecommerce.pagamento.infra;

import org.springframework.stereotype.Repository;

import br.com.wakax.wakax_ecommerce.pagamento.application.repository.TentativaPagamentoRepository;
import br.com.wakax.wakax_ecommerce.pagamento.domain.TentativaPagamento;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TentativaPagamentoInfraRepository implements TentativaPagamentoRepository {

  private final TentativaPagamentoJPARepository tentativaPagamentoJPARepository;

  @Override
  public TentativaPagamento salva(TentativaPagamento tentativaPagamento) {
    return tentativaPagamentoJPARepository.save(tentativaPagamento);
  }

  @Override
  public TentativaPagamento buscaPorChaveIdempotencia(String chaveIdempotencia) {
    return tentativaPagamentoJPARepository
        .findByChaveIdempotencia(chaveIdempotencia)
        .orElseThrow(
            () ->
                new IllegalStateException(
                    "Tentativa de pagamento nao encontrada: " + chaveIdempotencia));
  }
}
