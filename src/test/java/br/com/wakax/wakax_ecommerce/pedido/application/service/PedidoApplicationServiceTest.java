package br.com.wakax.wakax_ecommerce.pedido.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.wakax.wakax_ecommerce.carrinho.application.repository.CarrinhoRepository;
import br.com.wakax.wakax_ecommerce.carrinho.domain.Carrinho;
import br.com.wakax.wakax_ecommerce.carrinho.domain.ItemCarrinho;
import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoResponse;
import br.com.wakax.wakax_ecommerce.pedido.application.repository.PedidoRepository;
import br.com.wakax.wakax_ecommerce.pedido.domain.FormaPagamento;
import br.com.wakax.wakax_ecommerce.pedido.domain.ItemPedido;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Pessoa;
import br.com.wakax.wakax_ecommerce.produto.domain.Preco;
import br.com.wakax.wakax_ecommerce.produto.domain.Produto;

@ExtendWith(MockitoExtension.class)
class PedidoApplicationServiceTest {

  @Mock private PedidoRepository pedidoRepository;

  @Mock private CarrinhoRepository carrinhoRepository;

  @InjectMocks private PedidoApplicationService applicationService;

  @Test
  void deveCadastrarPedidoComSucesso() {
    UUID idCarrinho = UUID.randomUUID();
    UUID idCliente = UUID.randomUUID();

    PedidoRequest request = mock(PedidoRequest.class);
    when(request.getIdCarrinho()).thenReturn(idCarrinho);
    when(request.getFormaPagamento()).thenReturn(FormaPagamento.CARTAO_CREDITO);

    Carrinho carrinho = mock(Carrinho.class);
    Cliente cliente = mock(Cliente.class);
    Pessoa pessoa = mock(Pessoa.class);
    Endereco endereco = mock(Endereco.class);

    Produto produto = mock(Produto.class);
    Preco preco = mock(Preco.class);
    ItemCarrinho itemCarrinho = mock(ItemCarrinho.class);

    when(carrinho.getCliente()).thenReturn(cliente);
    when(cliente.getId()).thenReturn(idCliente);
    when(cliente.getPessoa()).thenReturn(pessoa);
    when(pessoa.getNome()).thenReturn("Cliente Teste");
    when(pessoa.getEnderecos()).thenReturn(List.of(endereco));
    when(carrinho.getItensCarrinho()).thenReturn(List.of(itemCarrinho));

    when(itemCarrinho.getProduto()).thenReturn(produto);
    when(itemCarrinho.getQuantidade()).thenReturn(2);
    when(produto.getPrecos()).thenReturn(List.of(preco));
    when(preco.getValor()).thenReturn(new BigDecimal("50.00"));

    when(carrinhoRepository.buscaCarrinhoPorId(idCarrinho)).thenReturn(carrinho);

    when(pedidoRepository.salva(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

    PedidoResponse response = applicationService.cadastraPedido(request);

    assertNotNull(response);
    assertEquals(idCliente, response.getClienteId());
    assertEquals(new BigDecimal("100.00"), response.getValorTotal());

    verify(carrinhoRepository).buscaCarrinhoPorId(idCarrinho);
    verify(pedidoRepository).salva(any(Pedido.class));
    verifyNoMoreInteractions(carrinhoRepository, pedidoRepository);
  }

  @Test
  void deveLancarExcecaoAoCadastrarQuandoCarrinhoNaoExiste() {
    UUID idCarrinho = UUID.randomUUID();

    PedidoRequest request = mock(PedidoRequest.class);
    when(request.getIdCarrinho()).thenReturn(idCarrinho);

    when(carrinhoRepository.buscaCarrinhoPorId(idCarrinho))
        .thenThrow(new APIException(null, ErrorCode.CARRINHO_NAO_ENCONTRADO));

    APIException ex =
        assertThrows(APIException.class, () -> applicationService.cadastraPedido(request));
    assertEquals(ErrorCode.CARRINHO_NAO_ENCONTRADO, ex.getErrorCode());

    verify(carrinhoRepository, times(1)).buscaCarrinhoPorId(idCarrinho);
    verify(pedidoRepository, never()).salva(any());
  }

  @Test
  void deveBuscarPedidoPorIdComSucesso() {
    UUID idPedido = UUID.randomUUID();
    UUID idCliente = UUID.randomUUID();

    Cliente cliente = mock(Cliente.class);
    Pessoa pessoa = mock(Pessoa.class);
    Endereco endereco = mock(Endereco.class);
    when(cliente.getId()).thenReturn(idCliente);
    when(cliente.getPessoa()).thenReturn(pessoa);
    when(pessoa.getNome()).thenReturn("Cliente Teste");

    Pedido pedido =
        Pedido.builder()
            .id(idPedido)
            .cliente(cliente)
            .dataPedido(LocalDateTime.now())
            .itensPedido(List.of())
            .valorTotal(new BigDecimal("10.00"))
            .formaPagamento(FormaPagamento.CARTAO_DEBITO)
            .enderecoEntrega(endereco)
            .build();

    when(pedidoRepository.buscaPedidoPorId(idPedido)).thenReturn(pedido);

    PedidoResponse response = applicationService.buscaPedidoPorId(idPedido);

    assertNotNull(response);
    assertEquals(idPedido, response.getIdPedido());
    assertEquals(idCliente, response.getClienteId());
    assertEquals(new BigDecimal("10.00"), response.getValorTotal());

    verify(pedidoRepository, times(1)).buscaPedidoPorId(idPedido);
    verifyNoMoreInteractions(pedidoRepository);
    verifyNoInteractions(carrinhoRepository);
  }

  @Test
  void deveLancarExcecaoQuandoPedidoNaoExiste() {
    UUID idPedido = UUID.randomUUID();

    when(pedidoRepository.buscaPedidoPorId(idPedido))
        .thenThrow(new APIException(null, ErrorCode.PEDIDO_NAO_ENCONTRADO));

    APIException ex =
        assertThrows(APIException.class, () -> applicationService.buscaPedidoPorId(idPedido));
    assertEquals(ErrorCode.PEDIDO_NAO_ENCONTRADO, ex.getErrorCode());

    verify(pedidoRepository, times(1)).buscaPedidoPorId(idPedido);
    verifyNoMoreInteractions(pedidoRepository);
    verifyNoInteractions(carrinhoRepository);
  }

  @Test
  void cadastraPedido_deveRetornarResponseComIdEValoresCorretos() {
    UUID idCarrinho = UUID.randomUUID();
    UUID idPedido = UUID.randomUUID();
    UUID idCliente = UUID.randomUUID();

    PedidoRequest request = mock(PedidoRequest.class);
    when(request.getIdCarrinho()).thenReturn(idCarrinho);
    when(request.getFormaPagamento()).thenReturn(FormaPagamento.CARTAO_CREDITO);

    Carrinho carrinho = mock(Carrinho.class);
    Cliente cliente = mock(Cliente.class);
    Pessoa pessoa = mock(Pessoa.class);
    Endereco endereco = mock(Endereco.class);
    ItemCarrinho itemCarrinho = mock(ItemCarrinho.class);
    Produto produto = mock(Produto.class);
    Preco preco = mock(Preco.class);

    when(carrinho.getCliente()).thenReturn(cliente);
    when(cliente.getId()).thenReturn(idCliente);
    when(cliente.getPessoa()).thenReturn(pessoa);
    when(pessoa.getNome()).thenReturn("Cliente Teste");
    when(pessoa.getEnderecos()).thenReturn(List.of(endereco));
    when(carrinho.getItensCarrinho()).thenReturn(List.of(itemCarrinho));

    when(itemCarrinho.getProduto()).thenReturn(produto);
    when(itemCarrinho.getQuantidade()).thenReturn(2);
    when(produto.getPrecos()).thenReturn(List.of(preco));
    when(preco.getValor()).thenReturn(new BigDecimal("50.00"));

    when(carrinhoRepository.buscaCarrinhoPorId(idCarrinho)).thenReturn(carrinho);
    when(pedidoRepository.salva(any(Pedido.class)))
        .thenAnswer(
            inv -> {
              Pedido p = inv.getArgument(0);
              p.setId(idPedido);
              return p;
            });

    PedidoResponse resp = applicationService.cadastraPedido(request);

    assertNotNull(resp);
    assertEquals(idPedido, resp.getIdPedido());
    assertEquals(idCliente, resp.getClienteId());
    assertEquals(new BigDecimal("100.00"), resp.getValorTotal());
    assertEquals(FormaPagamento.CARTAO_CREDITO, resp.getFormaPagamento());
    assertNotNull(resp.getEnderecoEntrega());

    verify(carrinhoRepository, times(1)).buscaCarrinhoPorId(idCarrinho);
    verify(pedidoRepository, times(1)).salva(any(Pedido.class));
    verifyNoMoreInteractions(carrinhoRepository, pedidoRepository);
  }

  @Test
  void buscaPedidoPorId_deveRetornarResponseMapeada() {
    UUID idPedido = UUID.randomUUID();
    UUID idCliente = UUID.randomUUID();

    Produto produto = mock(Produto.class);
    when(produto.getId()).thenReturn(UUID.randomUUID());
    when(produto.getDescricao()).thenReturn("Produto X");

    Endereco endereco = new Endereco();
    Pessoa pessoa = new Pessoa();
    pessoa.setNome("Fulano");
    pessoa.setEnderecos(List.of(endereco));

    Cliente cliente =
        Cliente.builder()
            .id(idCliente)
            .pessoa(pessoa)
            .dataCriacao(LocalDateTime.now())
            .dataEdicao(LocalDateTime.now())
            .build();

    ItemPedido item =
        ItemPedido.builder()
            .produto(produto)
            .quantidade(3)
            .valorUnitario(new BigDecimal("10.00"))
            .build();

    Pedido pedido =
        Pedido.builder()
            .id(idPedido)
            .cliente(cliente)
            .dataPedido(LocalDateTime.now())
            .status(StatusPedido.CRIADO)
            .itensPedido(List.of(item))
            .valorTotal(new BigDecimal("30.00"))
            .formaPagamento(FormaPagamento.PIX)
            .enderecoEntrega(endereco)
            .build();
    item.setPedido(pedido);

    when(pedidoRepository.buscaPedidoPorId(idPedido)).thenReturn(pedido);

    PedidoResponse resp = applicationService.buscaPedidoPorId(idPedido);

    assertNotNull(resp);
    assertEquals(idPedido, resp.getIdPedido());
    assertEquals(idCliente, resp.getClienteId());
    assertEquals("Fulano", resp.getNomeCliente());
    assertEquals(StatusPedido.CRIADO, resp.getStatus());
    assertEquals(new BigDecimal("30.00"), resp.getValorTotal());
    assertEquals(FormaPagamento.PIX, resp.getFormaPagamento());
    assertEquals(1, resp.getItensPedido().size());
    assertEquals(3, resp.getItensPedido().get(0).getQuantidade());

    verify(pedidoRepository, times(1)).buscaPedidoPorId(idPedido);
    verifyNoMoreInteractions(pedidoRepository);
    verifyNoInteractions(carrinhoRepository);
  }

  @Test
  void buscaPedidoPorId_devePropagarAPIExceptionQuandoNaoEncontrado() {
    UUID idPedido = UUID.randomUUID();
    when(pedidoRepository.buscaPedidoPorId(idPedido))
        .thenThrow(
            new APIException(
                org.springframework.http.HttpStatus.NOT_FOUND,
                ErrorCode.PEDIDO_NAO_ENCONTRADO,
                idPedido));

    APIException ex =
        assertThrows(APIException.class, () -> applicationService.buscaPedidoPorId(idPedido));
    assertEquals(ErrorCode.PEDIDO_NAO_ENCONTRADO, ex.getErrorCode());

    verify(pedidoRepository, times(1)).buscaPedidoPorId(idPedido);
    verifyNoMoreInteractions(pedidoRepository);
    verifyNoInteractions(carrinhoRepository);
  }
}
