package br.com.wakax.wakax_ecommerce.fornecedor.application.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.request.FornecedorAlteracaoRequest;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.response.FornecedorAlteracaoResponse;
import br.com.wakax.wakax_ecommerce.fornecedor.application.service.FornecedorDataHelper;
import br.com.wakax.wakax_ecommerce.fornecedor.application.service.FornecedorService;
import br.com.wakax.wakax_ecommerce.fornecedor.domain.Fornecedor;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.handler.MessageUtil;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;

@WebMvcTest(FornecedorController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(MessageUtil.class)
class FornecedorControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockBean private FornecedorService fornecedorService;
  @MockBean private TokenService tokenService;
  @MockBean private CredencialService credencialService;
  @MockBean private AutenticacaoSecurityService autenticacaoSecurityService;

  @Test
  void deveAlterarDadosFornecedorComSucesso() throws Exception {
    Fornecedor fornecedor = FornecedorDataHelper.criarFornecedorAtivo();
    UUID idFornecedor = fornecedor.getId();
    FornecedorAlteracaoRequest request =
        FornecedorDataHelper.criarRequestAlteracaoRazaoSocialENomeFantasia();

    fornecedor.alterarDadosFornecedor(
        FornecedorDataHelper.criarRequestAlteracaoRazaoSocialENomeFantasia());
    FornecedorAlteracaoResponse response = new FornecedorAlteracaoResponse(fornecedor);

    when(fornecedorService.alterarDadosFornecedor(eq(idFornecedor), any())).thenReturn(response);

    mockMvc
        .perform(
            patch("/fornecedor/{idFornecedor}", idFornecedor)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(idFornecedor.toString()))
        .andExpect(jsonPath("$.razaoSocial").value("Nova Razao Social"))
        .andExpect(jsonPath("$.nomeFantasia").value("Nova Fantasia"))
        .andExpect(jsonPath("$.documento").value(fornecedor.getDocumento()));

    verify(fornecedorService)
        .alterarDadosFornecedor(eq(idFornecedor), any(FornecedorAlteracaoRequest.class));
  }

  @Test
  void deveRetornarBadRequestQuandoEmailInvalidoNaAlteracao() throws Exception {
    UUID idFornecedor = UUID.randomUUID();
    FornecedorAlteracaoRequest request =
        FornecedorDataHelper.criarRequestAlteracao(
            null, List.of("email-invalido"), null, null, null, null, null);

    mockMvc
        .perform(
            patch("/fornecedor/{idFornecedor}", idFornecedor)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$['emails[0]']").value("E-mail inválido."));

    verify(fornecedorService, never()).alterarDadosFornecedor(any(), any());
  }

  @Test
  void deveRetornarBadRequestQuandoRazaoSocialExcedeTamanho() throws Exception {
    UUID idFornecedor = UUID.randomUUID();
    FornecedorAlteracaoRequest request =
        FornecedorDataHelper.criarRequestAlteracao(
            null, null, null, null, null, "R".repeat(101), null);

    mockMvc
        .perform(
            patch("/fornecedor/{idFornecedor}", idFornecedor)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.razaoSocial").value("Razão social deve ter no máximo 100 caracteres."));

    verify(fornecedorService, never()).alterarDadosFornecedor(any(), any());
  }

  @Test
  void deveAceitarCamposEmBrancoNaAlteracao() throws Exception {
    UUID idFornecedor = UUID.randomUUID();
    Endereco endereco = Endereco.builder().logradouro("   ").numero("").principal(false).build();
    FornecedorAlteracaoRequest request =
        FornecedorDataHelper.criarRequestAlteracao(
            null, null, null, List.of(endereco), null, "  ", null);
    Fornecedor fornecedor = FornecedorDataHelper.criarFornecedorAtivo();

    when(fornecedorService.alterarDadosFornecedor(eq(idFornecedor), any()))
        .thenReturn(new FornecedorAlteracaoResponse(fornecedor));

    mockMvc
        .perform(
            patch("/fornecedor/{idFornecedor}", idFornecedor)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(fornecedorService)
        .alterarDadosFornecedor(eq(idFornecedor), any(FornecedorAlteracaoRequest.class));
  }

  @Test
  void deveRetornarNotFoundQuandoFornecedorNaoExisteNaAlteracao() throws Exception {
    UUID idFornecedor = UUID.randomUUID();
    FornecedorAlteracaoRequest request =
        FornecedorDataHelper.criarRequestAlteracaoRazaoSocialENomeFantasia();

    when(fornecedorService.alterarDadosFornecedor(eq(idFornecedor), any()))
        .thenThrow(
            new APIException(
                HttpStatus.NOT_FOUND, ErrorCode.FORNECEDOR_NAO_ENCONTRADO, idFornecedor));

    mockMvc
        .perform(
            patch("/fornecedor/{idFornecedor}", idFornecedor)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(
            jsonPath("$.message")
                .value("Fornecedor com ID " + idFornecedor + " não foi encontrado."));

    verify(fornecedorService)
        .alterarDadosFornecedor(eq(idFornecedor), any(FornecedorAlteracaoRequest.class));
  }
}
