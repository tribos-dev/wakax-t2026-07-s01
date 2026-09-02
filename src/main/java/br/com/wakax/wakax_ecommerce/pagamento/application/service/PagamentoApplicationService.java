package br.com.wakax.wakax_ecommerce.pagamento.application.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.request.CancelaPagamentoRequest;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.request.PagamentoRequest;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.*;
import br.com.wakax.wakax_ecommerce.pagamento.application.factory.ProcessadorPagamentoFactory;
import br.com.wakax.wakax_ecommerce.pagamento.application.repository.PagamentoRepository;
import br.com.wakax.wakax_ecommerce.pagamento.application.repository.TentativaPagamentoRepository;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.TentativaPagamento;
import br.com.wakax.wakax_ecommerce.pedido.application.repository.PedidoRepository;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class PagamentoApplicationService implements PagamentoService {

  private final PagamentoRepository pagamentoRepository;
  private final PedidoRepository pedidoRepository;
  private final TentativaPagamentoRepository tentativaPagamentoRepository;
  private final ProcessadorPagamentoFactory processadorFactory;

  @Override
  @Transactional
  public PagamentoResponse processaPagamento(PagamentoRequest novoPagamento) {
    log.debug("[start] PagamentoApplicationService - criaPagamento");

    verificarSeExistePagamento(novoPagamento.getPedidoId());

    Pedido pedido = pedidoRepository.buscaPedidoPorId(novoPagamento.getPedidoId());
    Pagamento pagamento = new Pagamento(pedido);

    var processador = processadorFactory.obterProcessador(pedido.getFormaPagamento());
    processador.processar(pagamento, pedido);

    pedidoRepository.salva(pedido);
    pagamentoRepository.salva(pagamento);
    log.debug("[finish] PagamentoApplicationService - criaPagamento");
    return new PagamentoResponse(pagamento);
  }

  private void verificarSeExistePagamento(UUID pedidoId) {
    pagamentoRepository
        .buscaPagamentoPorPedidoId(pedidoId)
        .ifPresent(
            pagamentoExistente -> {
              throw new APIException(
                  HttpStatus.CONFLICT,
                  ErrorCode.PEDIDO_JA_POSSUI_PAGAMENTO,
                  pagamentoExistente.getStatusPagamento());
            });
  }

  @Override
  @Transactional(readOnly = true)
  public PagamentoResponse buscaPagamentoPorId(UUID idPagamento) {
    log.debug("[start] PagamentoApplicationService - buscaPagamentoPorId");
    Pagamento pagamento = pagamentoRepository.buscaPagamentoPorId(idPagamento);
    log.debug("[finish] PagamentoApplicationService - buscaPagamentoPorId");
    return new PagamentoResponse(pagamento);
  }

  @Override
  @Transactional(readOnly = true)
  public PagamentoResponse buscaPagamentoPorPedidoId(UUID idPedido) {
    log.debug("[start] PagamentoApplicationService - buscaPagamentoPorPedidoId");
    pedidoRepository.buscaPedidoPorId(idPedido);
    Pagamento pagamento =
        pagamentoRepository
            .buscaPagamentoPorPedidoId(idPedido)
            .orElseThrow(
                () ->
                    new APIException(
                        HttpStatus.NOT_FOUND,
                        ErrorCode.PAGAMENTO_DO_PEDIDO_NAO_ENCONTRADO,
                        idPedido));
    log.debug("[finish] PagamentoApplicationService - buscaPagamentoPorPedidoId");
    return new PagamentoResponse(pagamento);
  }

  @Override
  @Transactional(readOnly = true)
  public PagamentoPaginadoResponse buscaPagamentos(
      StatusPagamento status, int pagina, int tamanho) {
    log.debug("[start] PagamentoApplicationService - buscaPagamentos");

    Pageable pageable =
        PageRequest.of(pagina, tamanho, Sort.by(Sort.Direction.DESC, "dataPagamento"));
    Page<PagamentoResumoProjection> pagamentosProjection =
        pagamentoRepository.buscaPagamentos(status, pageable);

    Page<PagamentoResumoResponse> pagamentos =
        pagamentosProjection.map(
            p ->
                new PagamentoResumoResponse(
                    p.getId(),
                    p.getPedidoId(),
                    p.getStatusPagamento(),
                    p.getDataPagamento(),
                    p.getValor()));

    BigDecimal valorTotal = pagamentoRepository.somaValores(status);

    log.debug("[finish] PagamentoApplicationService - buscaPagamentos");
    return new PagamentoPaginadoResponse(pagamentos, valorTotal);
  }

  @Override
  @Transactional
  public PagamentoConfirmadoResponse confirmaPagamento(UUID idPagamento) {
    log.debug("[start] PagamentoApplicationService - confirmaPagamento");
    Pagamento pagamento = pagamentoRepository.buscaPagamentoPorId(idPagamento);
    if (pagamento.getStatusPagamento() != StatusPagamento.AGUARDANDO) {
      throw new APIException(
          HttpStatus.CONFLICT, ErrorCode.PAGAMENTO_JA_CONFIRMADO, pagamento.getStatusPagamento());
    }
    pagamento.confirmarPagamento();
    Pedido pedido = pagamento.getPedido();
    pedido.marcarComoPago();

    pagamentoRepository.salva(pagamento);
    pedidoRepository.salva(pedido);
    log.debug("[finish] PagamentoApplicationService - confirmaPagamento");
    return new PagamentoConfirmadoResponse(pagamento);
  }

  @Override
  @Transactional
  public ReprocessarPagamentoResponse reprocessaPagamento(UUID idPagamento) {
    log.debug("[start] PagamentoApplicationService - reprocessaPagamento");

    Pagamento pagamento = pagamentoRepository.buscaPagamentoPorId(idPagamento);
    Pedido pedido = pagamento.getPedido();

    validaReprocessamento(pagamento);
    int numeroTentativa = proximoNumeroTentativa(pagamento);

    pagamento.aguardarPagamento();
    tentativaPagamentoRepository.salva(TentativaPagamento.nova(pagamento, numeroTentativa));

    var processador = processadorFactory.obterProcessador(pedido.getFormaPagamento());
    processador.processar(pagamento, pedido);

    pedidoRepository.salva(pedido);
    pagamentoRepository.salva(pagamento);
    log.debug("[finish] PagamentoApplicationService - reprocessaPagamento");
    return new ReprocessarPagamentoResponse(pagamento, numeroTentativa);
  }

  private void validaReprocessamento(Pagamento pagamento) {
    if (pagamento.getStatusPagamento() == StatusPagamento.PAGO) {
      throw new APIException(HttpStatus.CONFLICT, ErrorCode.PAGAMENTO_JA_PROCESSADO_COM_SUCESSO);
    }
    if (pagamento.getStatusPagamento() != StatusPagamento.FALHOU) {
      throw new APIException(
          HttpStatus.CONFLICT,
          ErrorCode.PAGAMENTO_NAO_PODE_SER_REPROCESSADO,
          pagamento.getStatusPagamento());
    }
  }

  private int proximoNumeroTentativa(Pagamento pagamento) {
    long tentativasRealizadas =
        tentativaPagamentoRepository.contaTentativasDoPagamento(pagamento.getId());
    if (tentativasRealizadas >= TentativaPagamento.LIMITE_TENTATIVAS) {
      throw new APIException(
          HttpStatus.CONFLICT, ErrorCode.LIMITE_TENTATIVAS_PAGAMENTO_EXCEDIDO, pagamento.getId());
    }
    return (int) tentativasRealizadas + 1;
  }

  @Override
  @Transactional
  public PagamentoResponse cancelaPagamento(UUID idPagamento, CancelaPagamentoRequest request) {
    log.debug("[start] PagamentoApplicationService - cancelaPagamento");
    Pagamento pagamento = pagamentoRepository.buscaPagamentoPorId(idPagamento);
    pagamento.cancelarPagamento(request.getMotivo());
    Pedido pedido = pagamento.getPedido();
    pagamentoRepository.salva(pagamento);
    pedidoRepository.salva(pedido);
    log.debug("[finish] PagamentoApplicationService - cancelaPagamento");
    return new PagamentoResponse(pagamento);
  }
}
