package br.com.wakax.wakax_ecommerce.fornecedor.infra;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wakax.wakax_ecommerce.fornecedor.domain.Fornecedor;

public interface FornecedorJPARepository extends JpaRepository<Fornecedor, UUID> {

  boolean existsByDocumento(String documento);

    Page<Fornecedor> findAllByPessoaStatus(StatusPessoa status, Pageable pageable);
}
