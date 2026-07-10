package br.com.wakax.wakax_ecommerce.pagamento.application.factory;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pagamento.application.strategy.PagamentoAguardandoStrategy;
import br.com.wakax.wakax_ecommerce.pagamento.application.strategy.PagamentoImediatoStrategy;
import br.com.wakax.wakax_ecommerce.pagamento.application.strategy.ProcessadorPagamento;
import br.com.wakax.wakax_ecommerce.pedido.domain.FormaPagamento;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessadorPagamentoFactory {

  private final PagamentoImediatoStrategy pagamentoImediato;
  private final PagamentoAguardandoStrategy pagamentoAguardando;

  public ProcessadorPagamento obterProcessador(FormaPagamento formaPagamento) {
    switch (formaPagamento) {
      case CARTAO_CREDITO:
      case CARTAO_DEBITO:
      case DINHEIRO:
        return pagamentoImediato;
      case PIX:
      case BOLETO:
        return pagamentoAguardando;
      default:
        throw new APIException(
            HttpStatus.BAD_REQUEST, ErrorCode.FORMA_PAGAMENTO_NAO_SUPORTADA, formaPagamento);
    }
  }
}
