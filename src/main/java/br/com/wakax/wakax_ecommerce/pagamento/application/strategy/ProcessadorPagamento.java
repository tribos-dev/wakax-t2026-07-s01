package br.com.wakax.wakax_ecommerce.pagamento.application.strategy;

import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;

public interface ProcessadorPagamento {
  void processar(Pagamento pagamento, Pedido pedido);
}
