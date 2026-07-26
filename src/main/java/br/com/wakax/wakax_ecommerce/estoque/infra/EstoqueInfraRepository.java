package br.com.wakax.wakax_ecommerce.estoque.infra;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import br.com.wakax.wakax_ecommerce.estoque.application.repository.EstoqueRepository;
import br.com.wakax.wakax_ecommerce.estoque.domain.Estoque;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Repository
@RequiredArgsConstructor
public class EstoqueInfraRepository implements EstoqueRepository {

  private final EstoqueJPARepository estoqueJPARepository;

  @Override
  public Estoque salva(Estoque estoque) {
    log.info("[start] EstoqueInfraRepository - salva");
    Estoque estoqueSalvo = estoqueJPARepository.save(estoque);
    log.debug("[finish] EstoqueInfraRepository - salva");
    return estoqueSalvo;
  }

  @Override
  public Optional<Estoque> buscaEstoquePorIdProduto(UUID idProduto) {
    log.info("[start] EstoqueInfraRepository - buscaEstoquePorIdProduto");
    Optional<Estoque> estoque = estoqueJPARepository.findByProdutoId(idProduto);
    log.debug("[finish] EstoqueInfraRepository - buscaEstoquePorIdProduto");
    return estoque;
  }

  @Override
  public Estoque buscaEstoquePorId(UUID idEstoque) {
    log.info("[start] EstoqueInfraRepository - buscaEstoquePorId");
    Estoque estoque =
        estoqueJPARepository
            .findById(idEstoque)
            .orElseThrow(
                () -> new APIException(HttpStatus.NOT_FOUND, ErrorCode.ESTOQUE_NAO_ENCONTRADO));
    log.debug("[finish] EstoqueInfraRepository - buscaEstoquePorId");
    return estoque;
  }

  @Override
  public Page<Estoque> buscaTodosEstoques(
      Boolean quantidadeMinima, Boolean emFalta, Pageable pageable) {
    Page<UUID> paginaIds;

    if (Boolean.TRUE.equals(quantidadeMinima)) {
      paginaIds = estoqueJPARepository.buscaIdsEstoquesComQuantidadeMinima(pageable);
    } else if (Boolean.TRUE.equals(emFalta)) {
      paginaIds = estoqueJPARepository.buscaIdsEstoquesEmFalta(pageable);
    } else {
      paginaIds = estoqueJPARepository.buscaIdsTodosEstoques(pageable);
    }

    if (paginaIds.isEmpty()) {
      return new PageImpl<Estoque>(List.of(), pageable, paginaIds.getTotalElements());
    }

    List<UUID> ids = paginaIds.getContent();
    Map<UUID, Integer> ordemPorId = new HashMap<>();
    for (int i = 0; i < ids.size(); i++) {
      ordemPorId.put(ids.get(i), i);
    }

    List<Estoque> estoques = estoqueJPARepository.buscaEstoquesComProdutoEPrecos(ids);
    estoques.sort(Comparator.comparing(estoque -> ordemPorId.get(estoque.getId())));

    return new PageImpl<>(estoques, pageable, paginaIds.getTotalElements());
  }

  @Override
  public BigDecimal calculaValorTotalInventario(Boolean quantidadeMinima, Boolean emFalta) {
    BigDecimal valorTotal;

    if (Boolean.TRUE.equals(quantidadeMinima)) {
      valorTotal = estoqueJPARepository.calculaValorTotalInventarioQuantidadeMinima();
    } else if (Boolean.TRUE.equals(emFalta)) {
      valorTotal = estoqueJPARepository.calculaValorTotalInventarioEmFalta();
    } else {
      valorTotal = estoqueJPARepository.calculaValorTotalInventario();
    }

    if (valorTotal == null) {
      return BigDecimal.ZERO;
    }
    return valorTotal;
  }
}
