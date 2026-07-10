package br.com.wakax.wakax_ecommerce.estoque.api;

import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;

import br.com.wakax.wakax_ecommerce.estoque.api.request.EstoqueRequest;
import br.com.wakax.wakax_ecommerce.estoque.api.response.EstoqueResponse;
import br.com.wakax.wakax_ecommerce.estoque.application.service.EstoqueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
public class EstoqueController implements EstoqueAPI {

  private final EstoqueService estoqueService;

  @Override
  public EstoqueResponse criaEstoque(UUID idProduto, EstoqueRequest request) {
    log.info("[start] EstoqueController - criaEstoque");
    EstoqueResponse response = estoqueService.criaEstoque(idProduto, request);
    log.debug("[finish] EstoqueController - criaEstoque");
    return response;
  }

  @Override
  public EstoqueResponse buscaEstoquePorIdProduto(UUID idProduto) {
    log.info("[start] EstoqueController - buscaEstoquePorIdProduto");
    EstoqueResponse response = estoqueService.buscaEstoquePorIdProduto(idProduto);
    log.debug("[finish] EstoqueController - buscaEstoquePorIdProduto");
    return response;
  }
}
