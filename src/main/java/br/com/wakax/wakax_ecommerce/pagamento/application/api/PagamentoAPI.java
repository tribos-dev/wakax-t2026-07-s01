package br.com.wakax.wakax_ecommerce.pagamento.application.api;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.com.wakax.wakax_ecommerce.pagamento.application.api.request.PagamentoRequest;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoPaginadoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;

@RestController
@RequestMapping("/pagamento")
public interface PagamentoAPI {
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  PagamentoResponse processaPagamento(@Valid @RequestBody PagamentoRequest novoPagamento);

  @GetMapping("/{idPagamento}")
  PagamentoResponse buscaPagamentoPorId(@PathVariable UUID idPagamento);

  @GetMapping
  PagamentoPaginadoResponse buscaPagamentos(
      @RequestParam(required = false) StatusPagamento status,
      @RequestParam(defaultValue = "0") int pagina,
      @RequestParam(defaultValue = "10") int tamanho);

  @PostMapping("/{idPagamento}/confirma")
  @ResponseStatus(HttpStatus.CREATED)
  PagamentoResponse confirmaPagamento(@PathVariable UUID idPagamento);
}
