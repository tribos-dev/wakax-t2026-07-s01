package br.com.wakax.wakax_ecommerce.pagamento.infra;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wakax.wakax_ecommerce.pagamento.domain.TentativaPagamento;

public interface TentativaPagamentoJPARepository extends JpaRepository<TentativaPagamento, UUID> {

  long countByPagamentoId(UUID pagamentoId);
}
