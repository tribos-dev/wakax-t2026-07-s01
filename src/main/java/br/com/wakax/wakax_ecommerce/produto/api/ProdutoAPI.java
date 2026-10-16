package br.com.wakax.wakax_ecommerce.produto.api;

import java.util.UUID;

import javax.validation.Valid;

import br.com.wakax.wakax_ecommerce.produto.api.request.StatusProdutoRequest;
import br.com.wakax.wakax_ecommerce.produto.api.response.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.com.wakax.wakax_ecommerce.produto.api.request.PrecoUpdateRequest;
import br.com.wakax.wakax_ecommerce.produto.api.request.ProdutoRequest;

@RestController
@RequestMapping("/produto")
public interface ProdutoAPI {
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  ProdutoResponse cadastraProduto(@RequestBody ProdutoRequest novoProduto);

  @GetMapping("/{idProduto}")
  ProdutoListResponse buscaProdutoPorId(@PathVariable UUID idProduto);

  @GetMapping
  ProdutoPaginadoResponse listaProduto(
      @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "10") int tamanho);

  @PatchMapping("/{idProduto}/preco")
  PrecoResponse atualizaPreco(
      @PathVariable UUID idProduto, @Valid @RequestBody PrecoUpdateRequest precoUpdateRequest);

  @GetMapping("/ativos")
  ProdutoAtivoPaginadoResponse listarProdutosAtivos(
      @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "10") int tamanho);

  @PatchMapping("/{idProduto}/status/{status}")
  StatusResponse atualizaStatus(@PathVariable UUID idProduto, @Valid @RequestBody StatusProdutoRequest statusProdutoRequest);
}
