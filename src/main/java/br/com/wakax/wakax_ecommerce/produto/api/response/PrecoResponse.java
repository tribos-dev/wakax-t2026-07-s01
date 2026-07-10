package br.com.wakax.wakax_ecommerce.produto.api.response;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.produto.domain.Preco;
import br.com.wakax.wakax_ecommerce.produto.domain.TipoPreco;
import lombok.Getter;

@Getter
public class PrecoResponse {
  private final UUID id;
  private final TipoPreco tipo;
  private final BigDecimal valor;

  public PrecoResponse(Preco preco) {
    this.id = preco.getId();
    this.tipo = preco.getTipo();
    this.valor = preco.getValor();
  }
}
