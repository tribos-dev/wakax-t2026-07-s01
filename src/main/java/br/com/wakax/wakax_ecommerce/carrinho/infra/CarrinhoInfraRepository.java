package br.com.wakax.wakax_ecommerce.carrinho.infra;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import br.com.wakax.wakax_ecommerce.carrinho.application.repository.CarrinhoRepository;
import br.com.wakax.wakax_ecommerce.carrinho.domain.Carrinho;
import br.com.wakax.wakax_ecommerce.carrinho.domain.StatusCarrinho;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Repository
@RequiredArgsConstructor
public class CarrinhoInfraRepository implements CarrinhoRepository {

  private final CarrinhoSpringDataJPARepository carrinhoSpringDataJPARepository;

  @Override
  public Carrinho salva(Carrinho carrinho) {
    log.debug("[start] CarrinhoInfraRepository - save");
    Carrinho carrinhoSalvo = carrinhoSpringDataJPARepository.save(carrinho);
    log.debug("[finish] CarrinhoInfraRepository - save");
    return carrinhoSalvo;
  }

  @Override
  public Optional<Carrinho> buscaCarrinhoAtivoDoCliente(UUID idCliente) {
    log.debug("[start] CarrinhoInfraRepository - buscaCarrinhoAtivoDoCliente");
    Carrinho carrinho =
        carrinhoSpringDataJPARepository.findByClienteIdAndStatusCarrinho(
            idCliente, StatusCarrinho.ATIVO);
    log.debug("[finish] CarrinhoInfraRepository - buscaCarrinhoAtivoDoCliente");
    return Optional.ofNullable(carrinho);
  }

  @Override
  public Carrinho buscaCarrinhoPorId(UUID idCarrinho) {
    log.info("[start] CarrinhoInfraRepository - buscaCarrinhoPorId");
    Carrinho carrinho =
        carrinhoSpringDataJPARepository
            .findById(idCarrinho)
            .orElseThrow(
                () -> new APIException(HttpStatus.NOT_FOUND, ErrorCode.CARRINHO_NAO_ENCONTRADO));
    log.debug("[finish] CarrinhoInfraRepository - buscaCarrinhoPorId");
    return carrinho;
  }

    @Override
    public Page<Carrinho> buscaTodosCarrinhosDoCliente(UUID idCliente, Pageable pageable) {
        log.debug("[start] CarrinhoInfraRepository - buscaTodosCarrinhosDoCliente");
        Page<Carrinho> carrinhos = carrinhoSpringDataJPARepository.findByClienteId(idCliente, pageable);
        log.debug("[finish] CarrinhoInfraRepository - buscaTodosCarrinhosDoCliente");
        return carrinhos;
    }
}
