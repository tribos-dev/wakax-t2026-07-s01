package br.com.wakax.wakax_ecommerce.produto.infra;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.produto.application.repository.ProdutoRepository;
import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.ProdutoDisponivel;
import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ProdutoInfraRepository implements ProdutoRepository {

  private final ProdutoJPARepository produtoJPARepository;

  @Override
  public Produto salva(Produto produto) {
    log.debug("[start] ProdutoInfraRepository - salva");
    Produto produtoSalvo = produtoJPARepository.save(produto);
    log.debug("[finish] ProdutoInfraRepository - salva");
    return produtoSalvo;
  }

  @Override
  public Produto buscaProdutoPorId(UUID idProduto) {
    log.debug("[start] ProdutoInfraRepository - buscaProdutoPorId");
    return produtoJPARepository
        .findByIdComPrecos(idProduto)
        .orElseThrow(
            () ->
                new APIException(
                    HttpStatus.NOT_FOUND, ErrorCode.PRODUTO_NAO_ENCONTRADO, idProduto));
  }

  @Override
  public Page<Produto> listaTodos(Pageable pageable) {
    log.debug("[start] " + getClass().getSimpleName() + " - listaTodos");

    Page<UUID> paginaDeIds = produtoJPARepository.paginaIds(pageable);
    if (paginaDeIds.isEmpty()) {
      log.debug("[finish] " + getClass().getSimpleName() + " - listaTodos (pagina vazia)");
      return new PageImpl<>(List.of(), pageable, paginaDeIds.getTotalElements());
    }

    List<Produto> produtos = produtoJPARepository.buscaComPrecosPorIds(paginaDeIds.getContent());
    log.debug("[finish] " + getClass().getSimpleName() + " - listaTodos");

    return new PageImpl<>(produtos, pageable, paginaDeIds.getTotalElements());
  }

  @Override
  public Page<ProdutoDisponivel> listaProdutosAtivosComEstoque(Pageable pageable) {
    log.debug("[start] " + getClass().getSimpleName() + " - listaProdutosAtivosComEstoque");

    Page<UUID> paginaDeIds =
        produtoJPARepository.paginaIdsProdutosComEstoquePorStatus(StatusProduto.ATIVO, pageable);
    if (paginaDeIds.isEmpty()) {
      log.debug(
          "[finish] "
              + getClass().getSimpleName()
              + " - listaProdutosAtivosComEstoque (pagina vazia)");
      return new PageImpl<>(List.of(), pageable, paginaDeIds.getTotalElements());
    }

    List<Object[]> resultados =
        produtoJPARepository.buscaProdutosComPrecosEQuantidadePorIds(paginaDeIds.getContent());
    Map<UUID, ProdutoDisponivel> produtosPorId = new HashMap<>();
    for (Object[] resultado : resultados) {
      Produto produto = (Produto) resultado[0];
      Integer quantidadeDisponivel = (Integer) resultado[1];
      produtosPorId.put(produto.getId(), new ProdutoDisponivel(produto, quantidadeDisponivel));
    }

    List<ProdutoDisponivel> produtosOrdenados =
        paginaDeIds.getContent().stream().map(produtosPorId::get).toList();

    log.debug("[finish] " + getClass().getSimpleName() + " - listaProdutosAtivosComEstoque");
    return new PageImpl<>(produtosOrdenados, pageable, paginaDeIds.getTotalElements());
  }
}
