package br.com.wakax.wakax_ecommerce.produto.api;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.produto.api.request.StatusProdutoRequest;
import br.com.wakax.wakax_ecommerce.produto.api.response.*;
import org.springframework.web.bind.annotation.RestController;

import br.com.wakax.wakax_ecommerce.produto.api.request.PrecoUpdateRequest;
import br.com.wakax.wakax_ecommerce.produto.api.request.ProdutoRequest;
import br.com.wakax.wakax_ecommerce.produto.application.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ProdutoController implements ProdutoAPI {
  private final ProdutoService produtoService;

  @Override
  public ProdutoResponse cadastraProduto(ProdutoRequest novoProduto) {
    log.debug("[start] ProdutoController - cadastraProduto");
    ProdutoResponse response = produtoService.cadastraProduto(novoProduto);
    log.debug("[finish] ProdutoController - cadastraProduto");
    return response;
  }

  @Override
  public ProdutoListResponse buscaProdutoPorId(UUID idProduto) {
    log.debug("[start] ProdutoController - buscaProdutoPorId");
    ProdutoListResponse response = produtoService.buscaProdutoPorId(idProduto);
    log.debug("[finish] ProdutoController - buscaProdutoPorId");
    return response;
  }

  @Override
  public ProdutoPaginadoResponse listaProduto(int pagina, int tamanho) {
    log.debug("[start] ProdutoController - listaProduto");
    ProdutoPaginadoResponse response = produtoService.listaProduto(pagina, tamanho);
    log.debug("[finish] ProdutoController - listaProduto");
    return response;
  }

  @Override
  public PrecoResponse atualizaPreco(UUID idProduto, PrecoUpdateRequest precoUpdateRequest) {
    log.debug("[start] ProdutoController - atualizaPreco");
    PrecoResponse response = produtoService.atualizaPreco(idProduto, precoUpdateRequest);
    log.debug("[finish] ProdutoController - atualizaPreco");
    return response;
  }

  @Override
  public StatusResponse atualizaStatus(UUID idProduto, StatusProdutoRequest statusProdutoRequest) {
    log.debug("[start] ProdutoController - atualizaStatus");
    StatusResponse  response = produtoService.atualizaStatus(idProduto, statusProdutoRequest);
    log.debug("[finish] ProdutoController - atualizaStatus");
    return response;
  }

  @Override
  public ProdutoAtivoPaginadoResponse listarProdutosAtivos(int pagina, int tamanho) {
    log.debug("[start] ProdutoController - listarProdutosAtivos");
    ProdutoAtivoPaginadoResponse response = produtoService.listarProdutosAtivos(pagina, tamanho);
    log.debug("[finish] ProdutoController - listarProdutosAtivos");
    return response;
  }
}
