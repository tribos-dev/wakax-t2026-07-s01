package br.com.wakax.wakax_ecommerce.produto.infra;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wakax.wakax_ecommerce.produto.domain.HistoricoProduto;

public interface HistoricoProdutoJPARepository extends JpaRepository<HistoricoProduto, UUID> {}
