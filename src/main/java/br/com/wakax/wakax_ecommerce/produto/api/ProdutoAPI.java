package br.com.wakax.wakax_ecommerce.produto.api;

import java.util.List;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoResumoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.com.wakax.wakax_ecommerce.produto.api.request.ProdutoRequest;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoListResponse;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoResponse;

@RestController
@RequestMapping("/produto")
public interface ProdutoAPI {
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  ProdutoResponse cadastraProduto(@RequestBody ProdutoRequest novoProduto);

  @GetMapping("/{idProduto}")
  ProdutoListResponse buscaProdutoPorId(@PathVariable UUID idProduto);

  @GetMapping
  List<ProdutoResumoResponse> listaProduto();
}
