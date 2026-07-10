package br.com.wakax.wakax_ecommerce.carrinho.application.strategy;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import br.com.wakax.wakax_ecommerce.estoque.application.service.EstoqueService;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EstoqueReservaImediataStrategy implements ProcessadorEstoque {

  private final EstoqueService estoqueService;

  @Override
  public void aoAdicionarItem(Produto produto, Integer quantidade) {
    if (!estoqueService.temQuantidadeDisponivel(produto.getId(), quantidade)) {
      throw new APIException(HttpStatus.BAD_REQUEST, ErrorCode.QUANTIDADE_INSUFICIENTE_ESTOQUE);
    }
    estoqueService.reservaQuantidade(produto.getId(), quantidade);
  }

  @Override
  public void aoAlterarQuantidade(
      Produto produto, Integer quantidadeAtual, Integer novaQuantidade) {
    Integer diferenca = novaQuantidade - quantidadeAtual;

    if (diferenca > 0) {
      if (!estoqueService.temQuantidadeDisponivel(produto.getId(), diferenca)) {
        throw new APIException(HttpStatus.BAD_REQUEST, ErrorCode.QUANTIDADE_INSUFICIENTE_ESTOQUE);
      }
      estoqueService.reservaQuantidade(produto.getId(), diferenca);
    } else if (diferenca < 0) {
      estoqueService.liberaReserva(produto.getId(), Math.abs(diferenca));
    }
  }

  @Override
  public void aoRemoverItem(Produto produto, Integer quantidade) {
    estoqueService.liberaReserva(produto.getId(), quantidade);
  }
}
