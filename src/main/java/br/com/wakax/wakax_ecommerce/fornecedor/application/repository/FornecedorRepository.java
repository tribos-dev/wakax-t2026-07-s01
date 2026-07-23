package br.com.wakax.wakax_ecommerce.fornecedor.application.repository;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.fornecedor.domain.Fornecedor;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FornecedorRepository {

  Fornecedor salva(Fornecedor fornecedor);

  Fornecedor buscaFornecedorPorId(UUID id);

  Page<Fornecedor> buscaFornecedoresPaginados(StatusPessoa status, Pageable pageable);
}
