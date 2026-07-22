package br.com.wakax.wakax_ecommerce.produto.api;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;

import br.com.wakax.wakax_ecommerce.produto.api.request.ProdutoRequest;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoAtivoResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoListResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoResponse;
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
  public List<ProdutoAtivoResponse> listaProdutosAtivos() {
    log.debug("[start] ProdutoController - listaProdutosAtivos");
    List<ProdutoAtivoResponse> response = produtoService.listaProdutosAtivos();
    log.debug("[finish] ProdutoController - listaProdutosAtivos");
    return response;
  }

  @Override
  public ProdutoListResponse buscaProdutoPorId(UUID idProduto) {
    log.debug("[start] ProdutoController - buscaProdutoPorId");
    ProdutoListResponse response = produtoService.buscaProdutoPorId(idProduto);
    log.debug("[finish] ProdutoController - buscaProdutoPorId");
    return response;
  }
}
