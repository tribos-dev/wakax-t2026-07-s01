package br.com.wakax.wakax_ecommerce.pagamento.application.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.request.PagamentoRequest;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.factory.ProcessadorPagamentoFactory;
import br.com.wakax.wakax_ecommerce.pagamento.application.repository.PagamentoRepository;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
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
  public PagamentoResponse buscaPagamentoPorId(UUID idPagamento) {
    log.debug("[start] PagamentoApplicationService - buscaPagamentoPorId");
    Pagamento pagamento = pagamentoRepository.buscaPagamentoPorId(idPagamento);
    log.debug("[finish] PagamentoApplicationService - buscaPagamentoPorId");
    return new PagamentoResponse(pagamento);
  }
}
