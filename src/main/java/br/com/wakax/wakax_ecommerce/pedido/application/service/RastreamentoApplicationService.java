package br.com.wakax.wakax_ecommerce.pedido.application.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.RastreamentoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.RastreamentoResponse;
import br.com.wakax.wakax_ecommerce.pedido.application.repository.PedidoRepository;
import br.com.wakax.wakax_ecommerce.pedido.application.repository.RastreamentoRepository;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import br.com.wakax.wakax_ecommerce.pedido.domain.Rastreamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class RastreamentoApplicationService implements RastreamentoService {
  private final PedidoRepository pedidoRepository;
  private final RastreamentoRepository rastreamentoRepository;

  @Override
  @Transactional
  public RastreamentoResponse cadastraRastreamento(UUID idPedido, RastreamentoRequest request) {
    log.debug("[start] RastreamentoApplicationService - cadastraRastreamento");
    verificaSeJaExisteRastreamento(idPedido);
    Pedido pedido = pedidoRepository.buscaPedidoPorId(idPedido);
    Rastreamento rastreamento = new Rastreamento(request, pedido);
    rastreamentoRepository.salva(rastreamento);
    log.debug("[finish] RastreamentoApplicationService - cadastraRastreamento");
    return new RastreamentoResponse(rastreamento);
  }

  private void verificaSeJaExisteRastreamento(UUID idPedido) {
    rastreamentoRepository
        .buscaRastreamentoPorPedidoIdOptional(idPedido)
        .ifPresent(
            rastreamentoExistente -> {
              throw new APIException(
                  HttpStatus.CONFLICT, ErrorCode.RASTREAMENTO_JA_EXISTE, idPedido);
            });
  }

  @Override
  @Transactional(readOnly = true)
  public RastreamentoResponse consultaRastreamento(UUID idCliente, UUID idPedido) {
    log.debug("[start] RastreamentoApplicationService - consultaRastreamento");
    Rastreamento rastreamento = rastreamentoRepository.buscaRastreamentoPorPedidoId(idPedido);
    validaClienteDonoDoRastreio(idCliente, rastreamento);
    log.debug("[finish] RastreamentoApplicationService - consultaRastreamento");
    return new RastreamentoResponse(rastreamento);
  }

  private void validaClienteDonoDoRastreio(UUID idCliente, Rastreamento rastreamento) {
    UUID idClienteDoPedido = rastreamento.getPedido().getCliente().getId();
    if (!idClienteDoPedido.equals(idCliente)) {
      throw new APIException(
          HttpStatus.FORBIDDEN, ErrorCode.ACESSO_NEGADO, rastreamento.getPedido().getId());
    }
  }
}
