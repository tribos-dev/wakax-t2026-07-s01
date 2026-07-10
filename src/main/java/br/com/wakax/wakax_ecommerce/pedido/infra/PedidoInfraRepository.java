package br.com.wakax.wakax_ecommerce.pedido.infra;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pedido.application.repository.PedidoRepository;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Repository
@RequiredArgsConstructor
@Log4j2
public class PedidoInfraRepository implements PedidoRepository {
  private final PedidoJPARepository pedidoJPARepository;

  @Override
  public Pedido salva(Pedido pedido) {
    log.debug("[start] PedidoInfraRepository - salva");
    var salvo = pedidoJPARepository.save(pedido);
    log.debug("[finish] PedidoInfraRepository - salva");
    return salvo;
  }

  @Override
  public Pedido buscaPedidoPorId(UUID idPedido) {
    log.debug("[start] PedidoInfraRepository - buscaPedidoPorId");
    Pedido pedido =
        pedidoJPARepository
            .findById(idPedido)
            .orElseThrow(
                () ->
                    new APIException(
                        HttpStatus.NOT_FOUND, ErrorCode.PEDIDO_NAO_ENCONTRADO, idPedido));
    log.debug("[finish] PedidoInfraRepository - buscaPedidoPorId");
    return pedido;
  }
}
