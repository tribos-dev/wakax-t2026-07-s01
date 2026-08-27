package br.com.wakax.wakax_ecommerce.pagamento.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import br.com.wakax.wakax_ecommerce.auth.credencial.application.service.CredencialService;
import br.com.wakax.wakax_ecommerce.auth.credencial.domain.Credencial;
import br.com.wakax.wakax_ecommerce.auth.security.service.AutenticacaoSecurityService;
import br.com.wakax.wakax_ecommerce.auth.security.service.TokenService;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.handler.MessageUtil;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.PagamentoController;
import br.com.wakax.wakax_ecommerce.pagamento.application.api.response.PagamentoResponse;
import br.com.wakax.wakax_ecommerce.pagamento.application.service.PagamentoDataHelper;
import br.com.wakax.wakax_ecommerce.pagamento.application.service.PagamentoService;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;

@WebMvcTest(PagamentoController.class)
@AutoConfigureMockMvc
@Import(MessageUtil.class)
class PagamentoControllerWebTest {

  private static final String TOKEN = "token-teste";
  private static final String USUARIO = "usuario@teste.com";

  @Autowired private MockMvc mockMvc;
  @MockBean private PagamentoService pagamentoService;
  @MockBean private TokenService tokenService;
  @MockBean private CredencialService credencialService;
  @MockBean private AutenticacaoSecurityService autenticacaoSecurityService;

  private UUID idPagamento;
  private PagamentoResponse pagamentoResponse;

  @BeforeEach
  void setUp() {
    Credencial credencial = mock(Credencial.class);
    when(tokenService.getUsuario(TOKEN)).thenReturn(Optional.of(USUARIO));
    when(credencialService.buscaCredencialPorUsuario(USUARIO)).thenReturn(credencial);
    when(credencial.getAuthorities()).thenReturn(Collections.emptyList());

    Pagamento pagamento =
        PagamentoDataHelper.criaPagamentoValido(PagamentoDataHelper.criaPedidoValido());
    pagamento.setStatusPagamento(StatusPagamento.AGUARDANDO);
    pagamento.setNumeroTentativas(2);
    idPagamento = pagamento.getId();
    pagamentoResponse = new PagamentoResponse(pagamento);
  }

  @Test
  void deveExporReprocessamentoPorPut() throws Exception {
    when(pagamentoService.reprocessaPagamento(idPagamento)).thenReturn(pagamentoResponse);

    mockMvc
        .perform(
            put("/pagamento/{idPagamento}/reprocessar", idPagamento)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.idPagamento").value(idPagamento.toString()))
        .andExpect(jsonPath("$.statusPagamento").value("AGUARDANDO"))
        .andExpect(jsonPath("$.numeroTentativas").value(2));

    verify(pagamentoService).reprocessaPagamento(idPagamento);
  }

  @Test
  void deveInformarPagamentoJaProcessadoComSucesso() throws Exception {
    when(pagamentoService.reprocessaPagamento(idPagamento))
        .thenThrow(
            new APIException(HttpStatus.CONFLICT, ErrorCode.PAGAMENTO_JA_PROCESSADO_COM_SUCESSO));

    mockMvc
        .perform(
            put("/pagamento/{idPagamento}/reprocessar", idPagamento)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("Pagamento já processado com sucesso."));
  }

  @Test
  void deveInformarLimiteDeTentativasExcedido() throws Exception {
    when(pagamentoService.reprocessaPagamento(idPagamento))
        .thenThrow(
            new APIException(
                HttpStatus.CONFLICT, ErrorCode.LIMITE_TENTATIVAS_PAGAMENTO_EXCEDIDO, idPagamento));

    mockMvc
        .perform(
            put("/pagamento/{idPagamento}/reprocessar", idPagamento)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN))
        .andExpect(status().isConflict())
        .andExpect(
            jsonPath("$.message")
                .value("Limite de tentativas excedido para o pagamento " + idPagamento + "."));
  }

  @Test
  void deveExigirAutenticacaoParaReprocessarPagamento() throws Exception {
    APIException exception =
        assertThrows(
            APIException.class,
            () -> mockMvc.perform(put("/pagamento/{idPagamento}/reprocessar", idPagamento)));

    assertEquals(HttpStatus.FORBIDDEN, exception.getStatusException());
    verify(pagamentoService, never()).reprocessaPagamento(idPagamento);
  }
}
