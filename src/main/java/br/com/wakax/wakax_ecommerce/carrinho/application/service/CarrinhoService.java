package br.com.wakax.wakax_ecommerce.carrinho.application.service;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.carrinho.api.request.ItemCarrinhoRequest;
import br.com.wakax.wakax_ecommerce.carrinho.api.request.CarrinhoPaginacaoRequest;
import br.com.wakax.wakax_ecommerce.carrinho.api.response.CarrinhoListPageResponse;
import br.com.wakax.wakax_ecommerce.carrinho.api.response.CarrinhoResponse;

public interface CarrinhoService {
  CarrinhoResponse adicionaItemNoCarrinho(UUID idCliente, ItemCarrinhoRequest itemCarrinho);

  CarrinhoResponse buscaCarrinhoPorId(UUID idCliente, UUID idCarrinho);

  CarrinhoListPageResponse listaCarrinhosDoCliente(UUID idCliente, CarrinhoPaginacaoRequest carrinhoPaginacaoRequest);
}
