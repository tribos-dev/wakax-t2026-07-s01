package br.com.wakax.wakax_ecommerce.estoque.application.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wakax.wakax_ecommerce.estoque.api.request.EstoqueRequest;
import br.com.wakax.wakax_ecommerce.estoque.api.response.EstoqueListagemResponse;
import br.com.wakax.wakax_ecommerce.estoque.api.response.EstoqueResponse;
import br.com.wakax.wakax_ecommerce.estoque.application.repository.EstoqueRepository;
import br.com.wakax.wakax_ecommerce.estoque.domain.Estoque;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.produto.application.repository.ProdutoRepository;
import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class EstoqueApplicationService implements EstoqueService {

  private final EstoqueRepository estoqueRepository;
  private final ProdutoRepository produtoRepository;

  @Override
  @Transactional
  public EstoqueResponse criaEstoque(UUID idProduto, EstoqueRequest request) {
    log.info("[start] EstoqueApplicationService - criaEstoque");
    Produto produto = produtoRepository.buscaProdutoPorId(idProduto);
    validaSeJaExisteEstoque(produto.getId());
    Estoque estoque = new Estoque(request, produto);
    estoqueRepository.salva(estoque);
    log.info("[finish] EstoqueApplicationService - criaEstoque");
    return new EstoqueResponse(estoque);
  }

  @Override
  @Transactional(readOnly = true)
  public EstoqueResponse buscaEstoquePorIdProduto(UUID idProduto) {
    log.info("[start] EstoqueApplicationService - buscaEstoquePorIdProduto");
    Estoque estoque = buscaEstoqueExistente(idProduto);
    log.info("[finish] EstoqueApplicationService - buscaEstoquePorIdProduto");
    return new EstoqueResponse(estoque);
  }

  private Estoque buscaEstoqueExistente(UUID idProduto) {
    return estoqueRepository
        .buscaEstoquePorIdProduto(idProduto)
        .orElseThrow(
            () -> new APIException(HttpStatus.NOT_FOUND, ErrorCode.ESTOQUE_NAO_ENCONTRADO));
  }

  @Override
  @Transactional(readOnly = true)
  public boolean temQuantidadeDisponivel(UUID idProduto, Integer quantidade) {
    log.info("[start] EstoqueApplicationService - temQuantidadeDisponivel");
    Estoque estoque = buscaEstoqueExistente(idProduto);
    boolean disponivel = estoque.temQuantidadeDisponivel(quantidade);
    log.info("[finish] EstoqueApplicationService - temQuantidadeDisponivel");
    return disponivel;
  }

  @Override
  @Transactional
  public void reservaQuantidade(UUID idProduto, Integer quantidade) {
    log.info("[start] EstoqueApplicationService - reservaQuantidade");
    Estoque estoque = buscaEstoqueExistente(idProduto);
    estoque.reservaQuantidade(quantidade);
    estoqueRepository.salva(estoque);
    log.info("[finish] EstoqueApplicationService - reservaQuantidade");
  }

  @Override
  @Transactional
  public void liberaReserva(UUID idProduto, Integer quantidade) {
    log.info("[start] EstoqueApplicationService - liberaReserva");
    Estoque estoque = buscaEstoqueExistente(idProduto);
    estoque.liberaReserva(quantidade);
    estoqueRepository.salva(estoque);
    log.info("[finish] EstoqueApplicationService - liberaReserva");
  }

  private void validaSeJaExisteEstoque(UUID idProduto) {
    estoqueRepository
        .buscaEstoquePorIdProduto(idProduto)
        .ifPresent(
            estoque -> {
              throw new APIException(HttpStatus.CONFLICT, ErrorCode.ESTOQUE_JA_EXISTE);
            });
  }

  @Override
  @Transactional(readOnly = true)
  public EstoqueListagemResponse buscaTodosEstoques(
      Boolean quantidadeMinima, Boolean emFalta, int pagina, int tamanho) {
    log.info("[start] EstoqueApplicationService - buscaTodosEstoques");
    boolean filtroQuantidadeMinima = Boolean.TRUE.equals(quantidadeMinima);
    boolean filtroEmFalta = Boolean.TRUE.equals(emFalta);

    if (filtroQuantidadeMinima && filtroEmFalta) {
      throw new APIException(
          HttpStatus.BAD_REQUEST, ErrorCode.ESTOQUE_FILTROS_MUTUAMENTE_EXCLUSIVOS);
    }

    Pageable pageable = PageRequest.of(pagina, tamanho);
    Page<Estoque> estoques =
        estoqueRepository.buscaTodosEstoques(filtroQuantidadeMinima, filtroEmFalta, pageable);
    BigDecimal valorTotalFinanceiro =
        estoqueRepository.calculaValorTotalInventario(filtroQuantidadeMinima, filtroEmFalta);

    List<EstoqueResponse> estoquesResponse =
        estoques.getContent().stream().map(EstoqueResponse::new).collect(Collectors.toList());

    log.info("[finish] EstoqueApplicationService - buscaTodosEstoques");
    return EstoqueListagemResponse.builder()
        .estoques(estoquesResponse)
        .valorTotalInventario(valorTotalFinanceiro)
        .pagina(estoques.getNumber())
        .tamanho(estoques.getSize())
        .totalItens(estoques.getTotalElements())
        .build();
  }
}
