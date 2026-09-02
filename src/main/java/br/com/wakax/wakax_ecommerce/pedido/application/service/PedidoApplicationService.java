package br.com.wakax.wakax_ecommerce.pedido.application.service;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wakax.wakax_ecommerce.carrinho.application.repository.CarrinhoRepository;
import br.com.wakax.wakax_ecommerce.carrinho.domain.Carrinho;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoPaginadoResponse;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoResponse;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoResumoProjection;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoResumoResponse;
import br.com.wakax.wakax_ecommerce.pedido.application.repository.PedidoRepository;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class PedidoApplicationService implements PedidoService {

  private final PedidoRepository pedidoRepository;
  private final CarrinhoRepository carrinhoRepository;
  private final EstoqueService estoqueService;
  private final ClienteRepository clienteRepository;

  @Override
  @Transactional
  public PedidoResponse cadastraPedido(PedidoRequest request) {
    log.info("[start] PedidoApplicationService - cadastraPedido");
    Carrinho carrinho = carrinhoRepository.buscaCarrinhoPorId(request.getIdCarrinho());

    if (carrinho.getCliente().getPessoa().getStatus() != StatusPessoa.ATIVO) {
      throw APIException.build(HttpStatus.CONFLICT, "Cliente inativo não pode realizar pedidos.");
    }

    Pedido pedido = new Pedido(request, carrinho);
    pedidoRepository.salva(pedido);
    log.debug("[finish] PedidoApplicationService - cadastraPedido");
    return new PedidoResponse(pedido);
  }

  @Override
  public PedidoResponse buscaPedidoPorId(UUID idPedido) {
    log.debug("[start] PedidoApplicationService - buscaPedidoPorId");
    var pedido = pedidoRepository.buscaPedidoPorId(idPedido);
    log.debug("[finish] PedidoApplicationService - buscaPedidoPorId");
    return new PedidoResponse(pedido);
  }

  @Override
  @Transactional
  public void atualizarStatus(UUID idPedido, StatusPedido novoStatus) {
    log.debug("[start] PedidoApplicationService - atualizarStatus");
    Pedido pedido = pedidoRepository.buscaPedidoPorId(idPedido);
    pedido.atualizarStatus(novoStatus);

    if (novoStatus == StatusPedido.CANCELADO) {
      liberarEstoqueReservado(pedido);
    }

    pedidoRepository.salva(pedido);
    log.debug("[finish] PedidoApplicationService - atualizarStatus");
  }

  private void liberarEstoqueReservado(Pedido pedido) {
    pedido
        .getItensPedido()
        .forEach(
            item -> estoqueService.liberaReserva(item.getProduto().getId(), item.getQuantidade()));
  }

  @Override
  @Transactional(readOnly = true)
  public PedidoPaginadoResponse buscaPedidosDoCliente(
      UUID idCliente, StatusPedido status, int pagina, int tamanho) {
    log.debug("[start] PedidoApplicationService - buscaPedidosDoCliente");
    clienteRepository.buscaClientePorId(idCliente);
    Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(Sort.Direction.DESC, "dataPedido"));
    Page<PedidoResumoProjection> pedidosProjection =
        pedidoRepository.buscaPedidosDoCliente(idCliente, status, pageable);
    Page<PedidoResumoResponse> pedidos =
        pedidosProjection.map(
            p ->
                new PedidoResumoResponse(
                    p.getId(), p.getDataPedido(), p.getStatus(), p.getValorTotal()));
    log.debug("[finish] PedidoApplicationService - buscaPedidosDoCliente");
    return new PedidoPaginadoResponse(pedidos);
  }
}
