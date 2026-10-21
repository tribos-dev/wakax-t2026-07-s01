package br.com.wakax.wakax_ecommerce.produto.infra;

import br.com.wakax.wakax_ecommerce.produto.application.repository.HistoricoProdutoRepository;
import br.com.wakax.wakax_ecommerce.produto.domain.HistoricoProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface HistoricoProdutoJPARepository extends JpaRepository<HistoricoProduto, UUID> {
}
