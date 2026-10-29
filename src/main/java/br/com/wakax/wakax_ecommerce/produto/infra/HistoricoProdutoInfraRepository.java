package br.com.wakax.wakax_ecommerce.produto.infra;

import org.springframework.stereotype.Repository;

import br.com.wakax.wakax_ecommerce.produto.application.repository.HistoricoProdutoRepository;
import br.com.wakax.wakax_ecommerce.produto.domain.HistoricoProduto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Repository
@RequiredArgsConstructor
@Log4j2
public class HistoricoProdutoInfraRepository implements HistoricoProdutoRepository {
  private final HistoricoProdutoJPARepository historicoProdutoJPARepository;

  @Override
  public void salva(HistoricoProduto historico) {
    log.debug("[start] HistoricoProdutoInfraRepository - salva");
    historicoProdutoJPARepository.save(historico);
    log.debug("[finish] HistoricoProdutoInfraRepository - salva");
  }
}
