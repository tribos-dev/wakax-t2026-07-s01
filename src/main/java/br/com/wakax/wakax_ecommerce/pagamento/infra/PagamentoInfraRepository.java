package br.com.wakax.wakax_ecommerce.pagamento.infra;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResumoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.repository.PagamentoRepository;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Repository
@RequiredArgsConstructor
@Log4j2
public class PagamentoInfraRepository implements PagamentoRepository {

  private final PagamentoJPARepository pagamentoJPARepository;

  @Override
  public Pagamento salva(Pagamento pagamento) {
    log.debug("[start] PagamentoInfraRepository - salva");
    pagamentoJPARepository.save(pagamento);
    log.debug("[finish] PagamentoInfraRepository - salva");
    return pagamento;
  }

  @Override
  public Pagamento buscaPagamentoPorId(UUID idPagamento) {
    log.debug("[start] PagamentoInfraRepository - buscaPagamentoPorId");
    return pagamentoJPARepository
        .findByIdComPedido(idPagamento)
        .orElseThrow(
            () ->
                new APIException(
                    HttpStatus.NOT_FOUND, ErrorCode.PAGAMENTO_NAO_ENCONTRADO, idPagamento));
  }

  @Override
  public Optional<Pagamento> buscaPagamentoPorPedidoId(UUID pedidoId) {
    log.debug("[start] PagamentoInfraRepository - buscaPagamentoPorPedidoId");
    Optional<Pagamento> pagamento = pagamentoJPARepository.findByPedidoId(pedidoId);
    log.debug("[finish] PagamentoInfraRepository - buscaPagamentoPorPedidoId");
    return pagamento;
  }

  @Override
  public Page<PagamentoResumoResponse> buscaPagamentos(StatusPagamento status, Pageable pageable) {
    log.debug("[start] " + getClass().getSimpleName() + " - buscaPagamentos");
    return pagamentoJPARepository.buscaPagamentos(status, pageable);
  }

  @Override
  public BigDecimal somaValores(StatusPagamento status) {
    return pagamentoJPARepository.somaValores(status);
  }
}
