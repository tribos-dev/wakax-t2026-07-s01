package br.com.wakax.wakax_ecommerce.fornecedor.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wakax.wakax_ecommerce.fornecedor.application.api.request.FornecedorRequest;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.response.FornecedorListResponse;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.response.FornecedorPageResponse;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.response.FornecedorResponse;
import br.com.wakax.wakax_ecommerce.fornecedor.application.repository.FornecedorRepository;
import br.com.wakax.wakax_ecommerce.fornecedor.domain.Fornecedor;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class FornecedorApplicationService implements FornecedorService {

  private final FornecedorRepository fornecedorRepository;

  @Override
  public FornecedorResponse cadastraFornecedor(FornecedorRequest novoFornecedor) {
    log.debug("[start] FornecedorApplicationService - cadastraFornecedor");
    Fornecedor fornecedor = new Fornecedor(novoFornecedor);
    fornecedorRepository.salva(fornecedor);
    log.debug("[finish] FornecedorApplicationService - cadastraFornecedor");
    return new FornecedorResponse(fornecedor);
  }

  @Override
  public FornecedorListResponse buscaFornecedorPorId(UUID idFornecedor) {
    log.debug("[start] FornecedorApplicationService - buscaFornecedorPorId");
    Fornecedor fornecedor = fornecedorRepository.buscaFornecedorPorId(idFornecedor);
    log.debug("[finish] FornecedorApplicationService - buscaFornecedorPorId");
    return new FornecedorListResponse(fornecedor);
  }

  @Override
  public FornecedorPageResponse listarFornecedores(StatusPessoa status, Pageable pageable) {
    log.debug("[start] FornecedorApplicationService - listarFornecedores");
    Page<Fornecedor> fornecedoresPaginados =
        fornecedorRepository.buscaFornecedoresPaginados(status, pageable);
    log.debug("[finish] FornecedorApplicationService - listarFornecedores");
    return new FornecedorPageResponse(fornecedoresPaginados);
  }

  @Override
  @Transactional
  public void inativarFornecedor(UUID idFornecedor) {
    log.debug("[start] FornecedorApplicationService - inativarFornecedor");
    fornecedorRepository.inativar(idFornecedor);
    log.debug("[finish] FornecedorApplicationService - inativarFornecedor");
  }
}
