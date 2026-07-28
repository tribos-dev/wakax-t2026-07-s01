package br.com.wakax.wakax_ecommerce.carrinho.api.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.wakax.wakax_ecommerce.carrinho.domain.Carrinho;
import br.com.wakax.wakax_ecommerce.carrinho.domain.StatusCarrinho;
import lombok.Getter;

@Getter
public class CarrinhoListResponse {

  private UUID id;
  private LocalDateTime dataCriacao;
  private StatusCarrinho statusCarrinho;

  public CarrinhoListResponse(Carrinho carrinho) {
    this.id = carrinho.getId();
    this.dataCriacao = carrinho.getDataCriacao();
    this.statusCarrinho = carrinho.getStatusCarrinho();
  }

  public static List<CarrinhoListResponse> converte(List<Carrinho> carrinhos) {
    return carrinhos.stream().map(CarrinhoListResponse::new).collect(Collectors.toList());
  }
}
