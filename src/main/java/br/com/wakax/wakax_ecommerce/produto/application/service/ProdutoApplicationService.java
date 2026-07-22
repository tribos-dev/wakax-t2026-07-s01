package br.com.wakax.wakax_ecommerce.produto.application.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wakax.wakax_ecommerce.produto.api.request.ProdutoRequest;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoAtivoResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoListResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoResponse;
import br.com.wakax.wakax_ecommerce.produto.application.repository.ProdutoRepository;
import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.ProdutoDisponivel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProdutoApplicationService implements ProdutoService {

  private final ProdutoRepository produtoRepository;

  @Override
  public ProdutoResponse cadastraProduto(ProdutoRequest novoProduto) {
    log.debug("[start] ProdutoApplicationService - cadastraProduto");

    Produto produto = new Produto(novoProduto);
    produtoRepository.salva(produto);

    log.debug("[finish] ProdutoApplicationService - cadastraProduto");
    return new ProdutoResponse(produto);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProdutoAtivoResponse> listaProdutosAtivos() {
    log.debug("[start] ProdutoApplicationService - listaProdutosAtivos");

    List<ProdutoDisponivel> produtosDisponiveis = produtoRepository.listaProdutosAtivosComEstoque();

    List<ProdutoAtivoResponse> response =
        produtosDisponiveis.stream()
            .map(
                produtoDisponivel ->
                    new ProdutoAtivoResponse(
                        produtoDisponivel.getProduto(),
                        produtoDisponivel.getQuantidadeDisponivel()))
            .collect(Collectors.toList());

    log.debug("[finish] ProdutoApplicationService - listaProdutosAtivos");
    return response;
  }

  @Override
  public ProdutoListResponse buscaProdutoPorId(UUID idProduto) {
    log.debug("[start] ProdutoApplicationService - buscaProdutoPorId");

    Produto produto = produtoRepository.buscaProdutoPorId(idProduto);

    log.debug("[finish] ProdutoApplicationService - buscaProdutoPorId");
    return new ProdutoListResponse(produto);
  }
}
