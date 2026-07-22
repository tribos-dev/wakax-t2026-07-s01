package br.com.wakax.wakax_ecommerce.produto.infra;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
  public List<ProdutoDisponivel> listaProdutosAtivosComEstoque() {
    log.debug("[start] ProdutoInfraRepository - listaProdutosAtivosComEstoque");

    List<ProdutoDisponivel> produtos =
        produtoJPARepository.listaProdutosPorStatusComEstoque(StatusProduto.ATIVO).stream()
            .map(resultado -> new ProdutoDisponivel((Produto) resultado[0], (Integer) resultado[1]))
            .collect(Collectors.toList());

    log.debug("[finish] ProdutoInfraRepository - listaProdutosAtivosComEstoque");
    return produtos;
  }

  @Override
  public Produto buscaProdutoPorId(UUID idProduto) {
    log.debug("[start] ProdutoInfraRepository - buscaProdutoPorId");

    Produto produto =
        produtoJPARepository
            .findByIdComPrecos(idProduto)
            .orElseThrow(
                () ->
                    new APIException(
                        HttpStatus.NOT_FOUND, ErrorCode.PRODUTO_NAO_ENCONTRADO, idProduto));

    log.debug("[finish] ProdutoInfraRepository - buscaProdutoPorId");
    return produto;
  }
}
