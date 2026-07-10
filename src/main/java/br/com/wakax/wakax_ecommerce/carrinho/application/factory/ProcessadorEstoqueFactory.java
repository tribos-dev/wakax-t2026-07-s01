package br.com.wakax.wakax_ecommerce.carrinho.application.factory;

import org.springframework.stereotype.Service;

import br.com.wakax.wakax_ecommerce.carrinho.application.strategy.EstoqueReservaImediataStrategy;
import br.com.wakax.wakax_ecommerce.carrinho.application.strategy.ProcessadorEstoque;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessadorEstoqueFactory {

  private final EstoqueReservaImediataStrategy estoqueReservaImediata;

  public ProcessadorEstoque obterProcessador() {
    return estoqueReservaImediata;
  }
}
