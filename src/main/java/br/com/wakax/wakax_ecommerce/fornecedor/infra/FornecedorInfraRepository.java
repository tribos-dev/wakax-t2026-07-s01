package br.com.wakax.wakax_ecommerce.fornecedor.infra;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import br.com.wakax.wakax_ecommerce.fornecedor.application.repository.FornecedorRepository;
import br.com.wakax.wakax_ecommerce.fornecedor.domain.Fornecedor;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Repository
@RequiredArgsConstructor
@Log4j2
public class FornecedorInfraRepository implements FornecedorRepository {

  private final FornecedorJPARepository fornecedorJPARepository;

  @Override
  public Fornecedor salva(Fornecedor fornecedor) {
    log.debug("[start] FornecedorInfraRepository - salva");
    boolean jaExiste = fornecedorJPARepository.existsByDocumento(fornecedor.getDocumento());
    if (jaExiste) {
      log.error("Fornecedor duplicado: {}", fornecedor.getDocumento());
      throw new APIException(
          HttpStatus.CONFLICT, ErrorCode.FORNECEDOR_DUPLICADO, fornecedor.getDocumento());
    }
    fornecedorJPARepository.save(fornecedor);
    log.debug("[finish] FornecedorInfraRepository - salva");
    return fornecedor;
  }

  @Override
  public Fornecedor buscaFornecedorPorId(UUID id) {
    log.debug("[start] FornecedorInfraRepository - buscaPorId");
    Fornecedor fornecedor =
        fornecedorJPARepository
            .findById(id)
            .orElseThrow(
                () ->
                    new APIException(
                        HttpStatus.NOT_FOUND, ErrorCode.FORNECEDOR_NAO_ENCONTRADO, id));
    log.debug("[finish] FornecedorInfraRepository - buscaPorId");
    return fornecedor;
  }

  @Override
  public Page<Fornecedor> buscaFornecedoresPaginados(StatusPessoa status, Pageable pageable) {
    log.debug("[start] FornecedorInfraRepository - buscaFornecedoresPaginados");
    Page<Fornecedor> fornecedores = fornecedorJPARepository.findAllByPessoaStatus(status, pageable);
    log.debug("[finish] FornecedorInfraRepository - buscaFornecedoresPaginados");
    return fornecedores;
  }

  @Override
  public void inativar(UUID id) {
    log.debug("[start] FornecedorInfraRepository - inativar");
    Fornecedor fornecedor = buscaFornecedorPorId(id);

    if (temRestricaoDominio(fornecedor)) {
      log.warn("Tentativa de inativar fornecedor com restrição de domínio: {}", id);
      throw new APIException(HttpStatus.CONFLICT, ErrorCode.FORNECEDOR_COM_RESTRICAO, id);
    }

    fornecedor.inativar();
    fornecedorJPARepository.save(fornecedor);
    log.debug("[finish] FornecedorInfraRepository - inativar");
  }

  private boolean temRestricaoDominio(Fornecedor fornecedor) {
    // Placeholder: sempre retorna false (permite inativação), apenas loga warning
    // Atualmente nada restringe a inativação
    return false;
  }
}
