package br.com.wakax.wakax_ecommerce.fornecedor.application.api;

import java.util.UUID;

import javax.validation.Valid;

import br.com.wakax.wakax_ecommerce.fornecedor.application.api.request.FornecedorFiltroRequest;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.response.FornecedorPageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.com.wakax.wakax_ecommerce.fornecedor.application.api.request.FornecedorRequest;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.response.FornecedorListResponse;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.response.FornecedorResponse;

@RestController
@RequestMapping("/fornecedor")
public interface FornecedorAPI {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  FornecedorResponse cadastraFornecedor(@Valid @RequestBody FornecedorRequest novoFornecedor);

  @GetMapping("/{idFornecedor}")
  FornecedorListResponse buscaFornecedorPorId(@PathVariable UUID idFornecedor);

  @GetMapping
  FornecedorPageResponse listarFornecedores(@ModelAttribute FornecedorFiltroRequest filtro);

}
