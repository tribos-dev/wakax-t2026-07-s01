package br.com.wakax.wakax_ecommerce.fornecedor.application.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import br.com.wakax.wakax_ecommerce.fornecedor.application.api.request.FornecedorRequest;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.response.FornecedorListResponse;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.response.FornecedorPageResponse;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.response.FornecedorResponse;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;

public interface FornecedorService {

  FornecedorResponse cadastraFornecedor(FornecedorRequest novoFornecedor);

  FornecedorListResponse buscaFornecedorPorId(UUID idFornecedor);

  FornecedorPageResponse listarFornecedores(StatusPessoa status, Pageable pageable);

  void inativarFornecedor(UUID idFornecedor);
}
