package br.com.wakax.wakax_ecommerce.carrinho.application.service;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.carrinho.api.request.PaginacaoRequest;
import br.com.wakax.wakax_ecommerce.carrinho.api.response.CarrinhoListPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wakax.wakax_ecommerce.carrinho.api.request.ItemCarrinhoRequest;
import br.com.wakax.wakax_ecommerce.carrinho.api.response.CarrinhoResponse;
import br.com.wakax.wakax_ecommerce.carrinho.application.factory.ProcessadorEstoqueFactory;
import br.com.wakax.wakax_ecommerce.carrinho.application.repository.CarrinhoRepository;
import br.com.wakax.wakax_ecommerce.carrinho.application.strategy.ProcessadorEstoque;
import br.com.wakax.wakax_ecommerce.carrinho.domain.Carrinho;
import br.com.wakax.wakax_ecommerce.cliente.application.repository.ClienteRepository;
import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import br.com.wakax.wakax_ecommerce.produto.application.repository.ProdutoRepository;
import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class CarrinhoApplicationService implements CarrinhoService {

  private final CarrinhoRepository carrinhoRepository;
  private final ProdutoRepository produtoRepository;
  private final ClienteRepository clienteRepository;
  private final ProcessadorEstoqueFactory processadorEstoqueFactory;

  @Override
  @Transactional
  public CarrinhoResponse adicionaItemNoCarrinho(
      UUID idCliente, ItemCarrinhoRequest itemCarrinhoRequest) {
    log.info("[start] CarrinhoApplicationService - adicionaItemNoCarrinho");
    Cliente cliente = clienteRepository.buscaClientePorId(idCliente);
    Carrinho carrinho = buscaCarrinhoAtivoDoClienteOuCria(cliente);
    Produto produto = produtoRepository.buscaProdutoPorId(itemCarrinhoRequest.getIdProduto());

    ProcessadorEstoque processadorEstoque = processadorEstoqueFactory.obterProcessador();
    processadorEstoque.aoAdicionarItem(produto, itemCarrinhoRequest.getQuantidade());

    carrinho.adicionaItemAoCarrinho(itemCarrinhoRequest, produto);
    carrinhoRepository.salva(carrinho);
    log.debug("[finish] CarrinhoApplicationService - adicionaItemNoCarrinho");
    return new CarrinhoResponse(carrinho);
  }

  private Carrinho buscaCarrinhoAtivoDoClienteOuCria(Cliente cliente) {
    return carrinhoRepository
        .buscaCarrinhoAtivoDoCliente(cliente.getId())
        .orElseGet(() -> new Carrinho(cliente));
  }

  @Override
  @Transactional(readOnly = true)
  public CarrinhoResponse buscaCarrinhoPorId(UUID idCliente, UUID idCarrinho) {
    log.info("[start] CarrinhoApplicationService - buscaCarrinhoPorId");
    clienteRepository.buscaClientePorId(idCliente);
    Carrinho carrinho = carrinhoRepository.buscaCarrinhoPorId(idCarrinho);
    log.debug("[finish] CarrinhoApplicationService - buscaCarrinhoPorId");
    return new CarrinhoResponse(carrinho);
  }

  @Override
  @Transactional(readOnly = true)
  public CarrinhoListPageResponse listaCarrinhosDoCliente(UUID idCliente, PaginacaoRequest paginacaoRequest) {
      log.info("[start] CarrinhoApplicationService - listaCarrinhosDoCliente");
      clienteRepository.buscaClientePorId(idCliente);
      Pageable pageable = paginacaoRequest.paraPageable();
      Page<Carrinho> carrinhos = carrinhoRepository.buscaTodosCarrinhosDoCliente(idCliente, pageable);
      log.debug("[finish] CarrinhoApplicationService - listaCarrinhosDoCliente");
      return new CarrinhoListPageResponse(carrinhos);
  }
}
