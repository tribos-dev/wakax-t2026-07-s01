package br.com.wakax.wakax_ecommerce.fornecedor.application.repository;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.fornecedor.domain.Fornecedor;

public interface FornecedorRepository {

  Fornecedor salva(Fornecedor fornecedor);

  Fornecedor buscaFornecedorPorId(UUID id);
}
