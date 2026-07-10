package br.com.wakax.wakax_ecommerce.fornecedor.application.service;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.fornecedor.application.api.request.FornecedorRequest;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.response.FornecedorListResponse;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.response.FornecedorResponse;

public interface FornecedorService {

  FornecedorResponse cadastraFornecedor(FornecedorRequest novoFornecedor);

  FornecedorListResponse buscaFornecedorPorId(UUID idFornecedor);
}
