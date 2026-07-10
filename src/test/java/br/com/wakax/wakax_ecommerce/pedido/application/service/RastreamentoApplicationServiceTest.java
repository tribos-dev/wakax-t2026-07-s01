package br.com.wakax.wakax_ecommerce.pedido.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
}
