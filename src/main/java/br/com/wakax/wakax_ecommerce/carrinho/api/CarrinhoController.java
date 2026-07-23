package br.com.wakax.wakax_ecommerce.carrinho.api;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.carrinho.api.request.CarrinhoPaginacaoRequest;
import br.com.wakax.wakax_ecommerce.carrinho.api.response.CarrinhoListPageResponse;
import org.springframework.web.bind.annotation.RestController;

import br.com.wakax.wakax_ecommerce.carrinho.api.request.ItemCarrinhoRequest;
import br.com.wakax.wakax_ecommerce.carrinho.api.response.CarrinhoResponse;
import br.com.wakax.wakax_ecommerce.carrinho.application.service.CarrinhoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
public class CarrinhoController implements CarrinhoAPI {

  private final CarrinhoService carrinhoService;

  @Override
  public CarrinhoResponse adicionaItemNoCarrinho(UUID idCliente, ItemCarrinhoRequest itemCarrinho) {
    log.info("[start] CarrinhoController - adicionaItemNoCarrinho");
    CarrinhoResponse carrinho = carrinhoService.adicionaItemNoCarrinho(idCliente, itemCarrinho);
    log.debug("[finish] CarrinhoController - adicionaItemNoCarrinho");
    return carrinho;
  }

  @Override
  public CarrinhoResponse buscaCarrinhoPorId(UUID idCliente, UUID idCarrinho) {
    log.info("[start] CarrinhoController - buscaCarrinhoPorId");
    CarrinhoResponse carrinho = carrinhoService.buscaCarrinhoPorId(idCliente, idCarrinho);
    log.debug("[finish] CarrinhoController - buscaCarrinhoPorId");
    return carrinho;
  }

  @Override
  public CarrinhoListPageResponse listaCarrinhosDoCliente(UUID idCliente, CarrinhoPaginacaoRequest carrinhoPaginacaoRequest) {
      log.info("[start] CarrinhoController - listaCarrinhosDoCliente");
      CarrinhoListPageResponse carrinhos = carrinhoService.listaCarrinhosDoCliente(idCliente, carrinhoPaginacaoRequest);
      log.debug("[finish] CarrinhoController - listaCarrinhosDoCliente");
      return carrinhos;
  }
}
