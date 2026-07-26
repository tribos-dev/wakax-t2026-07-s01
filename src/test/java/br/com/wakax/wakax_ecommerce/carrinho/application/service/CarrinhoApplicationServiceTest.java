package br.com.wakax.wakax_ecommerce.carrinho.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import br.com.wakax.wakax_ecommerce.carrinho.api.request.CarrinhoPaginacaoRequest;
import br.com.wakax.wakax_ecommerce.carrinho.api.request.ItemCarrinhoRequest;
import br.com.wakax.wakax_ecommerce.carrinho.api.response.CarrinhoListPageResponse;
import br.com.wakax.wakax_ecommerce.carrinho.api.response.CarrinhoResponse;
import br.com.wakax.wakax_ecommerce.carrinho.application.factory.ProcessadorEstoqueFactory;
import br.com.wakax.wakax_ecommerce.carrinho.application.repository.CarrinhoRepository;
import br.com.wakax.wakax_ecommerce.carrinho.application.strategy.ProcessadorEstoque;
import br.com.wakax.wakax_ecommerce.carrinho.domain.Carrinho;
import br.com.wakax.wakax_ecommerce.carrinho.domain.StatusCarrinho;
import br.com.wakax.wakax_ecommerce.cliente.application.repository.ClienteRepository;
import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import br.com.wakax.wakax_ecommerce.produto.application.repository.ProdutoRepository;
import br.com.wakax.wakax_ecommerce.produto.domain.Produto;

@ExtendWith(MockitoExtension.class)
class CarrinhoApplicationServiceTest {

  @InjectMocks CarrinhoApplicationService applicationService;

  @Mock CarrinhoRepository carrinhoRepository;

  @Mock ProdutoRepository produtoRepository;

  @Mock ClienteRepository clienteRepository;

  @Mock ProcessadorEstoqueFactory processadorEstoqueFactory;

  @Mock ProcessadorEstoque processadorEstoque;

  @Test
  void deveAdicionarItemAUmCarrinhoAtivo() {
    Cliente cliente = CarrinhoDataHelper.criaCliente();
    Carrinho carrinho = CarrinhoDataHelper.criaCarrinhoAtivoVazio(cliente);
    Produto produto = CarrinhoDataHelper.criaProduto();
    ItemCarrinhoRequest itemCarrinhoRequest =
        CarrinhoDataHelper.criaItemCarrinhoRequest(produto.getId());

    when(clienteRepository.buscaClientePorId(cliente.getId())).thenReturn(cliente);
    when(carrinhoRepository.buscaCarrinhoAtivoDoCliente(cliente.getId()))
        .thenReturn(Optional.of(carrinho));
    when(produtoRepository.buscaProdutoPorId(produto.getId())).thenReturn(produto);
    when(carrinhoRepository.salva(carrinho)).thenReturn(carrinho);
    when(processadorEstoqueFactory.obterProcessador()).thenReturn(processadorEstoque);
    doNothing().when(processadorEstoque).aoAdicionarItem(any(Produto.class), anyInt());

    CarrinhoResponse carrinhoComItem =
        applicationService.adicionaItemNoCarrinho(cliente.getId(), itemCarrinhoRequest);

    assertEquals(carrinho.getId(), carrinhoComItem.getIdCarrinho());
    assertEquals(1, carrinhoComItem.getItensCarrinho().size());
    assertEquals(2, carrinhoComItem.getItensCarrinho().get(0).getQuantidade());

    verify(carrinhoRepository, times(1)).buscaCarrinhoAtivoDoCliente(cliente.getId());
    verify(produtoRepository, times(1)).buscaProdutoPorId(produto.getId());
    verify(carrinhoRepository, times(1)).salva(carrinho);
  }

  @Test
  void deveCriarNovoCarrinhoSeNaoExistirCarrinhoAtivo() {
    Cliente cliente = CarrinhoDataHelper.criaCliente();
    Carrinho carrinho = CarrinhoDataHelper.criaCarrinhoAtivoVazio(cliente);
    Produto produto = CarrinhoDataHelper.criaProduto();
    ItemCarrinhoRequest itemCarrinhoRequest =
        CarrinhoDataHelper.criaItemCarrinhoRequest(produto.getId());

    when(clienteRepository.buscaClientePorId(cliente.getId())).thenReturn(cliente);
    when(carrinhoRepository.buscaCarrinhoAtivoDoCliente(cliente.getId()))
        .thenReturn(Optional.empty());
    when(produtoRepository.buscaProdutoPorId(produto.getId())).thenReturn(produto);
    when(carrinhoRepository.salva(any(Carrinho.class))).thenReturn(carrinho);
    when(processadorEstoqueFactory.obterProcessador()).thenReturn(processadorEstoque);
    doNothing().when(processadorEstoque).aoAdicionarItem(any(Produto.class), anyInt());

    applicationService.adicionaItemNoCarrinho(cliente.getId(), itemCarrinhoRequest);

    verify(carrinhoRepository, times(1)).buscaCarrinhoAtivoDoCliente(cliente.getId());
    verify(produtoRepository, times(1)).buscaProdutoPorId(produto.getId());
    verify(carrinhoRepository, times(1)).salva(any(Carrinho.class));
  }

  @Test
  void deveRetornarCarrinhoPorId() {
    Cliente cliente = CarrinhoDataHelper.criaCliente();
    Carrinho carrinho = CarrinhoDataHelper.criaCarrinhoAtivoComUmItem(cliente);

    when(clienteRepository.buscaClientePorId(cliente.getId())).thenReturn(cliente);
    when(carrinhoRepository.buscaCarrinhoPorId(carrinho.getId())).thenReturn(carrinho);

    CarrinhoResponse carrinhoBuscado =
        applicationService.buscaCarrinhoPorId(cliente.getId(), carrinho.getId());

    assertEquals(carrinho.getId(), carrinhoBuscado.getIdCarrinho());
    verify(clienteRepository, times(1)).buscaClientePorId(cliente.getId());
    verify(carrinhoRepository, times(1)).buscaCarrinhoPorId(carrinho.getId());
  }

  @Test
  void deveRetornarPaginaVaziaQuandoClienteNaoTemCarrinhos() {
    Cliente cliente = CarrinhoDataHelper.criaCliente();
    CarrinhoPaginacaoRequest paginacaoRequest = new CarrinhoPaginacaoRequest();
    Pageable pageableEsperado = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "dataCriacao"));
    Page<Carrinho> paginaVazia = new PageImpl<>(Collections.emptyList(), pageableEsperado, 0);

    when(clienteRepository.buscaClientePorId(cliente.getId())).thenReturn(cliente);
    when(carrinhoRepository.buscaTodosCarrinhosDoCliente(cliente.getId(), pageableEsperado))
        .thenReturn(paginaVazia);

    CarrinhoListPageResponse resposta =
        applicationService.listaCarrinhosDoCliente(cliente.getId(), paginacaoRequest);

    assertEquals(0, resposta.getCarrinhos().size());
    assertEquals(0, resposta.getTotalElementos());
  }

  @Test
  void deveRetornarCarrinhosComDiferentesStatus() {
    Cliente cliente = CarrinhoDataHelper.criaCliente();
    Carrinho ativo =
        CarrinhoDataHelper.criaCarrinhoModular(cliente, LocalDateTime.now(), StatusCarrinho.ATIVO);
    Carrinho finalizado =
        CarrinhoDataHelper.criaCarrinhoModular(
            cliente, LocalDateTime.now().minusHours(3), StatusCarrinho.FINALIZADO);
    CarrinhoPaginacaoRequest paginacaoRequest = new CarrinhoPaginacaoRequest();
    Pageable pageableEsperado = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "dataCriacao"));
    Page<Carrinho> pagina = new PageImpl<>(List.of(ativo, finalizado), pageableEsperado, 2);

    when(clienteRepository.buscaClientePorId(cliente.getId())).thenReturn(cliente);
    when(carrinhoRepository.buscaTodosCarrinhosDoCliente(cliente.getId(), pageableEsperado))
        .thenReturn(pagina);

    CarrinhoListPageResponse resposta =
        applicationService.listaCarrinhosDoCliente(cliente.getId(), paginacaoRequest);

    assertEquals(StatusCarrinho.ATIVO, resposta.getCarrinhos().get(0).getStatusCarrinho());
    assertEquals(StatusCarrinho.FINALIZADO, resposta.getCarrinhos().get(1).getStatusCarrinho());
  }

  @Test
  void deveManterOrdemDeChegadaDoRepositorio() {
    Cliente cliente = CarrinhoDataHelper.criaCliente();
    Carrinho maisRecente =
        CarrinhoDataHelper.criaCarrinhoModular(cliente, LocalDateTime.now(), StatusCarrinho.ATIVO);
    Carrinho maisAntigo =
        CarrinhoDataHelper.criaCarrinhoModular(
            cliente, LocalDateTime.now().minusDays(5), StatusCarrinho.FINALIZADO);
    CarrinhoPaginacaoRequest paginacaoRequest = new CarrinhoPaginacaoRequest();
    Pageable pageableEsperado = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "dataCriacao"));
    Page<Carrinho> pagina = new PageImpl<>(List.of(maisRecente, maisAntigo), pageableEsperado, 2);

    when(clienteRepository.buscaClientePorId(cliente.getId())).thenReturn(cliente);
    when(carrinhoRepository.buscaTodosCarrinhosDoCliente(cliente.getId(), pageableEsperado))
        .thenReturn(pagina);

    CarrinhoListPageResponse resposta =
        applicationService.listaCarrinhosDoCliente(cliente.getId(), paginacaoRequest);

    assertEquals(maisRecente.getId(), resposta.getCarrinhos().get(0).getId());
    assertEquals(maisAntigo.getId(), resposta.getCarrinhos().get(1).getId());
  }
}
