package br.com.wakax.wakax_ecommerce.carrinho.api;

import java.util.UUID;

import javax.validation.Valid;

import br.com.wakax.wakax_ecommerce.carrinho.api.request.CarrinhoPaginacaoRequest;
import br.com.wakax.wakax_ecommerce.carrinho.api.response.CarrinhoListPageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.com.wakax.wakax_ecommerce.carrinho.api.request.ItemCarrinhoRequest;
import br.com.wakax.wakax_ecommerce.carrinho.api.response.CarrinhoResponse;

@RestController
@RequestMapping("/carrinho")
public interface CarrinhoAPI {

  @PostMapping("/{idCliente}")
  @ResponseStatus(code = HttpStatus.CREATED)
  CarrinhoResponse adicionaItemNoCarrinho(
      @PathVariable("idCliente") UUID idCliente,
      @Valid @RequestBody ItemCarrinhoRequest itemCarrinho);

  @GetMapping("{idCliente}/busca-carrinho/{idCarrinho}")
  CarrinhoResponse buscaCarrinhoPorId(
      @PathVariable("idCliente") UUID idCliente, @PathVariable("idCarrinho") UUID idCarrinho);

    @GetMapping("/{idCliente}/busca-todos")
    CarrinhoListPageResponse listaCarrinhosDoCliente(
            @PathVariable("idCliente") UUID idCliente, @ModelAttribute CarrinhoPaginacaoRequest carrinhoPaginacaoRequest);
}
