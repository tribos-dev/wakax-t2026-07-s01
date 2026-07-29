package br.com.wakax.wakax_ecommerce.fornecedor.application.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.wakax.wakax_ecommerce.fornecedor.domain.Fornecedor;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;

public interface FornecedorRepository {

  Fornecedor salva(Fornecedor fornecedor);

  Fornecedor buscaFornecedorPorId(UUID id);

  Page<Fornecedor> buscaFornecedoresPaginados(StatusPessoa status, Pageable pageable);
}
