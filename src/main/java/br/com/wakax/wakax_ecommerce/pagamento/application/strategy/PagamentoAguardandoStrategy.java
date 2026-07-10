package br.com.wakax.wakax_ecommerce.pagamento.application.strategy;

import org.springframework.stereotype.Component;

import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;

@Component
public class PagamentoAguardandoStrategy implements ProcessadorPagamento {

  @Override
  public void processar(Pagamento pagamento, Pedido pedido) {
    pedido.aguardarPagamento();
  }
}
