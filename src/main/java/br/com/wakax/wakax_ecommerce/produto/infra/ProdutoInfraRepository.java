package br.com.wakax.wakax_ecommerce.produto.infra;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.produto.application.repository.ProdutoRepository;
import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
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
}
