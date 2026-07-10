package br.com.wakax.wakax_ecommerce.fornecedor.infra;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wakax.wakax_ecommerce.fornecedor.domain.Fornecedor;

public interface FornecedorJPARepository extends JpaRepository<Fornecedor, UUID> {

  boolean existsByDocumento(String documento);
}
