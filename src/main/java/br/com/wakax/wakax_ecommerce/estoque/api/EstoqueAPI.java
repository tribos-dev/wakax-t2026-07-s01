package br.com.wakax.wakax_ecommerce.estoque.api;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.wakax.wakax_ecommerce.estoque.api.request.EstoqueRequest;
import br.com.wakax.wakax_ecommerce.estoque.api.response.EstoqueListagemResponse;
import br.com.wakax.wakax_ecommerce.estoque.api.response.EstoqueResponse;

@RestController
@RequestMapping("/estoque")
public interface EstoqueAPI {

  @PostMapping("/produto/{idProduto}")
  @ResponseStatus(HttpStatus.CREATED)
  EstoqueResponse criaEstoque(
      @PathVariable UUID idProduto, @Valid @RequestBody EstoqueRequest request);

  @GetMapping("/produto/{idProduto}")
  EstoqueResponse buscaEstoquePorIdProduto(@PathVariable UUID idProduto);

  @GetMapping
  EstoqueListagemResponse buscaTodosEstoques(
      @RequestParam(defaultValue = "false") Boolean quantidadeMinima,
      @RequestParam(defaultValue = "false") Boolean emFalta,
      @RequestParam(defaultValue = "0") int pagina,
      @RequestParam(defaultValue = "20") int tamanho);
}
