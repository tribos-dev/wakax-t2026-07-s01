package br.com.wakax.wakax_ecommerce.pedido.application.service;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.wakax.wakax_ecommerce.carrinho.application.repository.CarrinhoRepository;
import br.com.wakax.wakax_ecommerce.carrinho.domain.Carrinho;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.EnderecoUpdateRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoResponse;
import br.com.wakax.wakax_ecommerce.pedido.application.repository.PedidoRepository;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class PedidoApplicationService implements PedidoService {

  private final PedidoRepository pedidoRepository;
  private final CarrinhoRepository carrinhoRepository;

  @Override
  @Transactional
  public PedidoResponse cadastraPedido(PedidoRequest request) {
    log.info("[start] PedidoApplicationService - cadastraPedido");
    Carrinho carrinho = carrinhoRepository.buscaCarrinhoPorId(request.getIdCarrinho());
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
  public void alteraEnderecoEntrega(UUID idPedido, EnderecoUpdateRequest enderecoUpdateRequest) {
    log.debug("[start] PedidoApplicationService - alteraEnderecoEntrega");

    Pedido pedido = pedidoRepository.buscaPedidoPorId(idPedido);
    if (!pedido.podeAlterarEndereco()) {
      throw new APIException(
          HttpStatus.CONFLICT,
          ErrorCode.PEDIDO_NAO_PERMITE_ALTERACAO_ENDERECO,
          pedido.getStatus(),
          idPedido);
    }
    pedido.alteraEndereco(enderecoUpdateRequest);
    pedidoRepository.salva(pedido);

    log.debug("[finish] PedidoApplicationService - alteraEnderecoEntrega");
  }
}
