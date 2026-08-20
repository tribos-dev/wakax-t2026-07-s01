package br.com.wakax.wakax_ecommerce.pagamento.application.api;

import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;

import br.com.wakax.wakax_ecommerce.pagamento.application.api.request.PagamentoRequest;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class PagamentoController implements PagamentoAPI {

  private final PagamentoService pagamentoService;

  @Override
  public PagamentoResponse processaPagamento(PagamentoRequest novoPagamento) {
    log.debug("[start] PagamentoController - criaPagamento");
    PagamentoResponse response = pagamentoService.processaPagamento(novoPagamento);
    log.debug("[finish] PagamentoController - criaPagamento");
    return response;
  }

  @Override
  public PagamentoResponse buscaPagamentoPorId(UUID idPagamento) {
    log.debug("[start] PagamentoController - buscaPagamentoPorId");
    PagamentoResponse response = pagamentoService.buscaPagamentoPorId(idPagamento);
    log.debug("[finish] PagamentoController - buscaPagamentoPorId");
    return response;
  }

  @Override
  public PagamentoResponse confirmaPagamento(UUID idPagamento) {
    log.debug("[start] PagamentoController - confirmaPagamento");
    PagamentoResponse response = pagamentoService.confirmaPagamento(idPagamento);
    log.debug("[finish] PagamentoController - confirmaPagamento");
    return response;
  }
}
