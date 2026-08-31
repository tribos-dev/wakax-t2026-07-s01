package br.com.wakax.wakax_ecommerce.pagamento.infra;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.wakax.wakax_ecommerce.pagamento.application.repository.TentativaPagamentoRepository;
import br.com.wakax.wakax_ecommerce.pagamento.domain.TentativaPagamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Repository
@RequiredArgsConstructor
@Log4j2
public class TentativaPagamentoInfraRepository implements TentativaPagamentoRepository {

  private final TentativaPagamentoJPARepository tentativaPagamentoJPARepository;

  @Override
  public TentativaPagamento salva(TentativaPagamento tentativaPagamento) {
    log.debug("[start] TentativaPagamentoInfraRepository - salva");
    TentativaPagamento tentativaSalva = tentativaPagamentoJPARepository.save(tentativaPagamento);
    log.debug("[finish] TentativaPagamentoInfraRepository - salva");
    return tentativaSalva;
  }

  @Override
  public long contaTentativasDoPagamento(UUID idPagamento) {
    log.debug("[start] TentativaPagamentoInfraRepository - contaTentativasDoPagamento");
    long totalTentativas = tentativaPagamentoJPARepository.countByPagamentoId(idPagamento);
    log.debug("[finish] TentativaPagamentoInfraRepository - contaTentativasDoPagamento");
    return totalTentativas;
  }
}
