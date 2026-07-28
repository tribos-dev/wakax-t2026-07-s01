package br.com.wakax.wakax_ecommerce.pedido.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import br.com.wakax.wakax_ecommerce.pedido.domain.HistoricoRastreamento;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.HistoricoRastreamentoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.RastreamentoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.RastreamentoResponse;
import br.com.wakax.wakax_ecommerce.pedido.application.repository.PedidoRepository;
import br.com.wakax.wakax_ecommerce.pedido.application.repository.RastreamentoRepository;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import br.com.wakax.wakax_ecommerce.pedido.domain.Rastreamento;
import br.com.wakax.wakax_ecommerce.pedido.domain.StatusRastreamento;

@ExtendWith(MockitoExtension.class)
class RastreamentoApplicationServiceTest {

  @Mock private PedidoRepository pedidoRepository;
  @Mock private RastreamentoRepository rastreamentoRepository;

  @InjectMocks private RastreamentoApplicationService rastreamentoApplicationService;

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void deveCadastrarRastreamentoComEventos() {
    UUID idPedido = UUID.randomUUID();

    when(rastreamentoRepository.buscaRastreamentoPorPedidoIdOptional(idPedido))
        .thenReturn(java.util.Optional.empty());
    when(pedidoRepository.buscaPedidoPorId(idPedido))
        .thenReturn(Pedido.builder().id(idPedido).build());

    RastreamentoRequest request =
        new RastreamentoRequest(
            "BR123",
            "Correios",
            StatusRastreamento.EM_TRANSITO,
            LocalDate.now().plusDays(3),
            List.of(
                new HistoricoRastreamentoRequest(
                    LocalDateTime.now(), "SP", "Objeto postado", StatusRastreamento.CRIADO)));

    RastreamentoResponse response =
        rastreamentoApplicationService.cadastraRastreamento(idPedido, request);

    assertNotNull(response);
    assertEquals("BR123", response.getCodigo());
    assertEquals("Correios", response.getTransportadora());
    assertEquals(StatusRastreamento.EM_TRANSITO, response.getStatusAtual());
    assertEquals(1, response.getHistorico().size());

    verify(rastreamentoRepository, times(1)).salva(any());
  }

  @Test
  void deveLancarExcecaoQuandoRastreamentoJaExiste() {
    UUID idPedido = UUID.randomUUID();
    when(rastreamentoRepository.buscaRastreamentoPorPedidoIdOptional(idPedido))
        .thenReturn(java.util.Optional.of(mock(Rastreamento.class)));

    RastreamentoRequest request =
        new RastreamentoRequest("BR123", "Correios", StatusRastreamento.CRIADO, null, List.of());

    APIException ex =
        assertThrows(
            APIException.class,
            () -> rastreamentoApplicationService.cadastraRastreamento(idPedido, request));

    assertEquals(ErrorCode.RASTREAMENTO_JA_EXISTE, ex.getErrorCode());
    verifyNoInteractions(pedidoRepository);
  }

    @Test
    void deveConsultarRastreamentoComSucesso() {
        UUID idCliente = UUID.randomUUID();
        UUID idPedido = UUID.randomUUID();
        Cliente cliente = Cliente.builder().id(idCliente).build();
        Pedido pedido = Pedido.builder().id(idPedido).cliente(cliente).build();
        Rastreamento rastreamento =
                Rastreamento.builder()
                        .id(UUID.randomUUID())
                        .codigo("BR123")
                        .transportadora("Correios")
                        .statusAtual(StatusRastreamento.EM_TRANSITO)
                        .previsaoEntrega(LocalDate.now().plusDays(3))
                        .pedido(pedido)
                        .eventos(
                                List.of(
                                        HistoricoRastreamento.builder()
                                                .id(UUID.randomUUID())
                                                .dataEvento(LocalDateTime.now())
                                                .local("SP")
                                                .descricao("Objeto postado")
                                                .status(StatusRastreamento.CRIADO)
                                                .build()))
                        .build();

        when(rastreamentoRepository.buscaRastreamentoPorPedidoId(idPedido)).thenReturn(rastreamento);

        RastreamentoResponse response =
                rastreamentoApplicationService.consultaRastreamento(idCliente, idPedido);

        assertNotNull(response);
        assertEquals("BR123", response.getCodigo());
        assertEquals("Correios", response.getTransportadora());
        assertEquals(StatusRastreamento.EM_TRANSITO, response.getStatusAtual());
        assertEquals(1, response.getHistorico().size());

        verify(rastreamentoRepository, times(1)).buscaRastreamentoPorPedidoId(idPedido);
    }

    @Test
    void deveLancarExcecaoQuandoPedidoNaoTemRastreamento() {
        UUID idCliente = UUID.randomUUID();
        UUID idPedido = UUID.randomUUID();

        when(rastreamentoRepository.buscaRastreamentoPorPedidoId(idPedido))
                .thenThrow(
                        new APIException(HttpStatus.NOT_FOUND, ErrorCode.RASTREAMENTO_NAO_ENCONTRADO, idPedido));

        APIException ex =
                assertThrows(
                        APIException.class,
                        () -> rastreamentoApplicationService.consultaRastreamento(idCliente, idPedido));

        assertEquals(ErrorCode.RASTREAMENTO_NAO_ENCONTRADO, ex.getErrorCode());
    }

    @Test
    void deveLancarExcecaoQuandoPedidoNaoPertenceAoCliente() {
        UUID idClienteDoPedido = UUID.randomUUID();
        UUID idClienteDaConsulta = UUID.randomUUID();
        UUID idPedido = UUID.randomUUID();
        Cliente clienteDono = Cliente.builder().id(idClienteDoPedido).build();
        Pedido pedido = Pedido.builder().id(idPedido).cliente(clienteDono).build();
        Rastreamento rastreamento =
                Rastreamento.builder()
                        .id(UUID.randomUUID())
                        .codigo("BR123")
                        .transportadora("Correios")
                        .statusAtual(StatusRastreamento.EM_TRANSITO)
                        .pedido(pedido)
                        .eventos(List.of())
                        .build();

        when(rastreamentoRepository.buscaRastreamentoPorPedidoId(idPedido)).thenReturn(rastreamento);

        APIException ex =
                assertThrows(
                        APIException.class,
                        () ->
                                rastreamentoApplicationService.consultaRastreamento(
                                        idClienteDaConsulta, idPedido));

        assertEquals(ErrorCode.ACESSO_NEGADO, ex.getErrorCode());
    }
}
