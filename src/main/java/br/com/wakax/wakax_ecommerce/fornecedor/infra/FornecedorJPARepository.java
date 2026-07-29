package br.com.wakax.wakax_ecommerce.fornecedor.infra;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wakax.wakax_ecommerce.fornecedor.domain.Fornecedor;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;

public interface FornecedorJPARepository extends JpaRepository<Fornecedor, UUID> {

  boolean existsByDocumento(String documento);

  @Query(
      """
    SELECT f FROM Fornecedor f
    WHERE (:status IS NULL OR f.pessoa.status = :status)
""")
  Page<Fornecedor> findAllByPessoaStatus(@Param("status") StatusPessoa status, Pageable pageable);
}
