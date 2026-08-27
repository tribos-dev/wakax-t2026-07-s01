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
import br.com.wakax.wakax_ecommerce.pagamento.application.api.request.PagamentoRequest;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoPaginadoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResumoProjection;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResumoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.factory.ProcessadorPagamentoFactory;
import br.com.wakax.wakax_ecommerce.pagamento.application.repository.PagamentoRepository;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;
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
  private final ProcessadorPagamentoFactory processadorFactory;
  private final ReprocessamentoPagamentoService reprocessamentoPagamentoService;

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
  public PagamentoResponse confirmaPagamento(UUID idPagamento) {
    log.debug("[start] PagamentoApplicationService - confirmaPagamento");
    Pagamento pagamento = pagamentoRepository.buscaPagamentoPorId(idPagamento);
    if (pagamento.getStatusPagamento() != StatusPagamento.AGUARDANDO) {
      throw new APIException(
          HttpStatus.NOT_FOUND, ErrorCode.PAGAMENTO_JA_CONFIRMADO, pagamento.getStatusPagamento());
    }
    pagamento.confirmarPagamento();
    Pedido pedido = pagamento.getPedido();
    pedido.marcarComoPago();

    pagamentoRepository.salva(pagamento);
    pedidoRepository.salva(pedido);
    log.debug("[finish] PagamentoApplicationService - confirmaPagamento");
    return new PagamentoResponse(pagamento);
  }

  @Override
  public PagamentoResponse reprocessaPagamento(UUID idPagamento) {
    return reprocessamentoPagamentoService.reprocessaPagamento(idPagamento);
  }
}
