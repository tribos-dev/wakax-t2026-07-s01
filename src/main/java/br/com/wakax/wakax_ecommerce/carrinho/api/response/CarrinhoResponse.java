package br.com.wakax.wakax_ecommerce.carrinho.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.wakax.wakax_ecommerce.carrinho.domain.Carrinho;
import br.com.wakax.wakax_ecommerce.carrinho.domain.ItemCarrinho;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.ClienteResponse;
import lombok.Getter;

@Getter
public class CarrinhoResponse {

  private UUID idCarrinho;
  private ClienteResponse cliente;
  private LocalDateTime dataCriacao;
  private BigDecimal valorTotal;
  private List<ItemCarrinhoResponse> itensCarrinho;

  public CarrinhoResponse(Carrinho carrinho) {
    this.idCarrinho = carrinho.getId();
    this.cliente = new ClienteResponse(carrinho.getCliente());
    this.dataCriacao = carrinho.getDataCriacao();
    this.valorTotal = carrinho.calculaValorTotal();
    this.itensCarrinho = converteListaDeItensParaResponse(carrinho.getItensCarrinho());
  }

  private List<ItemCarrinhoResponse> converteListaDeItensParaResponse(
      List<ItemCarrinho> itensCarrinho) {
    return itensCarrinho.stream().map(ItemCarrinhoResponse::new).collect(Collectors.toList());
  }
}
