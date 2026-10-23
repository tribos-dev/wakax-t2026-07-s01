package br.com.wakax.wakax_ecommerce.pedido.application.api;

import br.com.wakax.wakax_ecommerce.auth.credencial.application.service.CredencialService;
import br.com.wakax.wakax_ecommerce.auth.security.service.AutenticacaoSecurityService;
import br.com.wakax.wakax_ecommerce.auth.security.service.TokenService;
import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import br.com.wakax.wakax_ecommerce.handler.MessageUtil;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoFilterRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.PedidoListResponse;
import br.com.wakax.wakax_ecommerce.pedido.application.service.PedidoApplicationService;
import br.com.wakax.wakax_ecommerce.pedido.domain.FormaPagamento;
import br.com.wakax.wakax_ecommerce.pedido.domain.Pedido;
import br.com.wakax.wakax_ecommerce.pedido.domain.StatusPedido;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Pessoa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(MessageUtil.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private TokenService tokenService;

    @MockBean private CredencialService credencialService;
    @MockBean private AutenticacaoSecurityService autenticacaoSecurityService;

    @MockBean private PedidoApplicationService pedidoService;

    @Test
    @DisplayName("Valida se o filtro está sendo preenchido pela configuração do endpoint")
    void FiltroDeveEstaSendoPreenchidoAoBuscarPedidoPorCliente() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dataPedido"));
        UUID idCliente = UUID.randomUUID();

        Page<PedidoListResponse> pagina = Page.empty();

        when(pedidoService.buscarTodosPedidosComFiltro(any(), any(), any())).thenReturn(pagina);

        mockMvc
                .perform(
                        get("/pedido/cliente/{idCliente}", idCliente)
                                .param("status", "PAGO")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sort", "dataPedido,desc")
                )
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        ArgumentCaptor<PedidoFilterRequest> filterCaptor = ArgumentCaptor.forClass(PedidoFilterRequest.class);

        verify(pedidoService).buscarTodosPedidosComFiltro(
                filterCaptor.capture(),
                pageableCaptor.capture(),
                eq(idCliente)
        );

        assertEquals(StatusPedido.PAGO, filterCaptor.getValue().getStatus());
        assertEquals(pageableCaptor.getValue(), pageable);
    }
}
