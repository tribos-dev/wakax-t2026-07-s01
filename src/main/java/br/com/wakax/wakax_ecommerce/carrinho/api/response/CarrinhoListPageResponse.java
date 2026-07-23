package br.com.wakax.wakax_ecommerce.carrinho.api.response;

import java.util.List;

import org.springframework.data.domain.Page;

import br.com.wakax.wakax_ecommerce.carrinho.domain.Carrinho;
import lombok.Getter;

@Getter
public class CarrinhoListPageResponse {
    private final List<CarrinhoListResponse> carrinhos;
    private final int paginaAtual;
    private final int totalPaginas;
    private final long totalElementos;
    private final boolean ultimaPagina;

    public CarrinhoListPageResponse(Page<Carrinho> pageCarrinhos) {
        this.carrinhos = CarrinhoListResponse.converte(pageCarrinhos.getContent());
        this.paginaAtual = pageCarrinhos.getNumber();
        this.totalPaginas = pageCarrinhos.getTotalPages();
        this.totalElementos = pageCarrinhos.getTotalElements();
        this.ultimaPagina = pageCarrinhos.isLast();
    }
}