package br.com.wakax.wakax_ecommerce.cliente.application.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.wakax.wakax_ecommerce.auth.credencial.application.service.CredencialService;
import br.com.wakax.wakax_ecommerce.auth.security.service.AutenticacaoSecurityService;
import br.com.wakax.wakax_ecommerce.auth.security.service.TokenService;
import br.com.wakax.wakax_ecommerce.cliente.application.api.request.AtualizaClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.application.api.request.ClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.ClienteResponse;
import br.com.wakax.wakax_ecommerce.cliente.application.service.ClienteDataHelper;
import br.com.wakax.wakax_ecommerce.cliente.application.service.ClienteService;
import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.handler.MessageUtil;

@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(MessageUtil.class)
class ClienteControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private ClienteService clienteService;
    @MockBean private TokenService tokenService;
    @MockBean private CredencialService credencialService;
    @MockBean private AutenticacaoSecurityService autenticacaoSecurityService;

    @Test
    void deveCadastrarClienteComSucesso() throws Exception {
        ClienteRequest request = ClienteDataHelper.criaClienteRequestValido();
        Cliente cliente = ClienteDataHelper.criaClienteValido();
        ClienteResponse response = new ClienteResponse(cliente);

        when(clienteService.criaCliente(any())).thenReturn(response);

        mockMvc
                .perform(
                        post("/cliente")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(response.getNome()));
    }

    @Test
    void deveAtualizarClienteComSucesso() throws Exception {
        UUID idCliente = UUID.randomUUID();
        AtualizaClienteRequest request = ClienteDataHelper.criaAtualizaClienteRequestValido();

        // instância independente: usada só para montar a resposta simulada, sem mutar o objeto
        // que será serializado como corpo da requisição (Endereco/Pessoa não são serializáveis
        // com segurança após vinculados, pois carregam referência circular)
        Cliente cliente = ClienteDataHelper.criaClienteValido();
        cliente.atualizar(ClienteDataHelper.criaAtualizaClienteRequestValido());
        ClienteResponse response = new ClienteResponse(cliente);

        when(clienteService.atualizaCliente(eq(idCliente), any())).thenReturn(response);

        mockMvc
                .perform(
                        put("/cliente/{idCliente}", idCliente)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(response.getNome()))
                .andExpect(jsonPath("$.telefones[0]").value(response.getTelefones().get(0)))
                .andExpect(jsonPath("$.enderecos[0].cep").value(response.getEnderecos().get(0).getCep()));

        verify(clienteService).atualizaCliente(eq(idCliente), any(AtualizaClienteRequest.class));
    }

    @Test
    void deveRetornarBadRequestQuandoEmailInvalidoNaAtualizacao() throws Exception {
        UUID idCliente = UUID.randomUUID();
        AtualizaClienteRequest request =
                new AtualizaClienteRequest(
                        "Cliente Teste", List.of("email-invalido"), List.of("11999999999"), List.of());

        mockMvc
                .perform(
                        put("/cliente/{idCliente}", idCliente)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(clienteService, never()).atualizaCliente(any(), any());
    }

    @Test
    void deveRetornarNotFoundQuandoClienteNaoExisteNaAtualizacao() throws Exception {
        UUID idCliente = UUID.randomUUID();
        AtualizaClienteRequest request = ClienteDataHelper.criaAtualizaClienteRequestValido();

        when(clienteService.atualizaCliente(eq(idCliente), any()))
                .thenThrow(new APIException(HttpStatus.NOT_FOUND, ErrorCode.CLIENTE_NAO_ENCONTRADO));

        mockMvc
                .perform(
                        put("/cliente/{idCliente}", idCliente)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(clienteService).atualizaCliente(eq(idCliente), any(AtualizaClienteRequest.class));
    }

    @Test
    void deveBuscarClienteEspecificoComSucesso() throws Exception {
        UUID idCliente = UUID.randomUUID();
        Cliente cliente = ClienteDataHelper.criaClienteValido();
        ClienteResponse response = new ClienteResponse(cliente);

        when(clienteService.buscaClienteEspecifico(idCliente)).thenReturn(response);

        mockMvc
                .perform(get("/cliente/{idCliente}", idCliente))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(response.getNome()));

        verify(clienteService).buscaClienteEspecifico(idCliente);
    }
}
