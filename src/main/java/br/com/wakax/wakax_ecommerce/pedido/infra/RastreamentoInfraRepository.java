package br.com.wakax.wakax_ecommerce.pedido.infra;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pedido.application.repository.RastreamentoRepository;
import br.com.wakax.wakax_ecommerce.pedido.domain.Rastreamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Repository
@RequiredArgsConstructor
@Log4j2
public class RastreamentoInfraRepository implements RastreamentoRepository {
  private final RastreamentoJPARepository rastreamentoJPARepository;

  @Override
  public Rastreamento salva(Rastreamento rastreamento) {
    log.debug("[start] RastreamentoInfraRepository - salva");
    Rastreamento salvo = rastreamentoJPARepository.save(rastreamento);
    log.debug("[finish] RastreamentoInfraRepository - salva");
    return salvo;
  }

  @Override
  public Rastreamento buscaRastreamentoPorPedidoId(UUID idPedido) {
    log.debug("[start] RastreamentoInfraRepository - buscaRastreamentoPorPedidoId");
    Rastreamento rastreamento =
        rastreamentoJPARepository
            .findByPedidoId(idPedido)
            .orElseThrow(
                () ->
                    new APIException(
                        HttpStatus.NOT_FOUND, ErrorCode.RASTREAMENTO_NAO_ENCONTRADO, idPedido));
    log.debug("[finish] RastreamentoInfraRepository - buscaRastreamentoPorPedidoId");
    return rastreamento;
  }

  @Override
  public Optional<Rastreamento> buscaRastreamentoPorPedidoIdOptional(UUID idPedido) {
    log.debug("[start] RastreamentoInfraRepository - buscaRastreamentoPorPedidoIdOptional");
    Optional<Rastreamento> rastreamento = rastreamentoJPARepository.findByPedidoId(idPedido);
    log.debug("[finish] RastreamentoInfraRepository - buscaRastreamentoPorPedidoIdOptional");
    return rastreamento;
  }
}
