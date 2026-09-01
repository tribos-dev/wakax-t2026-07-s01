package br.com.wakax.wakax_ecommerce.pagamento.application.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.request.PagamentoRequest;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResumoProjection;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResumoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;
import br.com.wakax.wakax_ecommerce.pedido.domain.FormaPagamento;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Pessoa;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;

public final class PagamentoDataHelper {

  private PagamentoDataHelper() {}

  public static Pedido criaPedidoValido() {
    return Pedido.builder()
        .id(UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890"))
        .cliente(criaClienteValido())
        .enderecoEntrega(criaEnderecoValido())
        .formaPagamento(FormaPagamento.PIX)
        .status(StatusPedido.CRIADO)
        .valorTotal(new BigDecimal("299.99"))
        .dataPedido(LocalDateTime.now())
        .dataAtualizacao(LocalDateTime.now())
        .itensPedido(new ArrayList<>())
        .build();
  }

  public static Cliente criaClienteValido() {
    return Cliente.builder()
        .id(UUID.fromString("b2c3d4e5-1234-5678-9abc-def123456789"))
        .pessoa(criaPessoaValida())
        .dataCriacao(LocalDateTime.now())
        .dataEdicao(LocalDateTime.now())
        .build();
  }

  public static Pessoa criaPessoaValida() {
    return new Pessoa(
        UUID.fromString("c3d4e5f6-1234-5678-9abc-def123456789"),
        "João Silva",
        "123.456.789-00",
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        StatusPessoa.ATIVO);
  }

  public static Endereco criaEnderecoValido() {
    return Endereco.builder()
        .id(UUID.fromString("d4e5f6a7-1234-5678-9abc-def123456789"))
        .cep("01234-567")
        .logradouro("Rua das Flores")
        .numero("123")
        .complemento("Apto 45")
        .bairro("Centro")
        .cidade("São Paulo")
        .estado("SP")
        .build();
  }

  public static Pagamento criaPagamentoValido(Pedido pedido) {
    return Pagamento.builder()
        .id(UUID.fromString("e5f6a7b8-1234-5678-9abc-def123456789"))
        .pedido(pedido)
        .statusPagamento(StatusPagamento.AGUARDANDO)
        .dataPagamento(LocalDateTime.now())
        .valor(pedido.getValorTotal())
        .build();
  }

  public static PagamentoRequest criaPagamentoRequestValido(UUID pedidoId) {
    return PagamentoRequest.builder().pedidoId(pedidoId).build();
  }

  public static PagamentoResumoResponse criaPagamentoResumoResponse(
      StatusPagamento status, LocalDateTime dataPagamento, BigDecimal valor) {
    return new PagamentoResumoResponse(
        UUID.randomUUID(), UUID.randomUUID(), status, dataPagamento, valor);
  }

  public static PagamentoResumoProjection criaPagamentoResumoProjection(
      StatusPagamento statusPagamento, LocalDateTime dataPagamento, BigDecimal valor) {
    PagamentoResumoProjection projection = mock(PagamentoResumoProjection.class);
    when(projection.getId()).thenReturn(UUID.randomUUID());
    when(projection.getPedidoId()).thenReturn(UUID.randomUUID());
    when(projection.getStatusPagamento()).thenReturn(statusPagamento);
    when(projection.getDataPagamento()).thenReturn(dataPagamento);
    when(projection.getValor()).thenReturn(valor);
    return projection;
  }
}
