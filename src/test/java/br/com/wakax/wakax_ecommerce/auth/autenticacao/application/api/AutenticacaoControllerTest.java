package br.com.wakax.wakax_ecommerce.auth.autenticacao.application.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.wakax.wakax_ecommerce.auth.autenticacao.application.service.AutenticacaoService;
import br.com.wakax.wakax_ecommerce.auth.autenticacao.domain.Token;
import br.com.wakax.wakax_ecommerce.auth.credencial.application.service.CredencialService;
import br.com.wakax.wakax_ecommerce.auth.security.service.AutenticacaoSecurityService;
import br.com.wakax.wakax_ecommerce.auth.security.service.TokenService;
import br.com.wakax.wakax_ecommerce.handler.MessageUtil;

@WebMvcTest(AutenticacaoController.class)
@Import(MessageUtil.class)
class AutenticacaoControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private AutenticacaoService autenticacaoService;

  @MockBean private TokenService tokenService;

  @MockBean private CredencialService credencialService;

  @MockBean private AutenticacaoSecurityService autenticacaoSecurityService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @DisplayName("Deve autenticar e retornar token válido")
  void autentica_DeveRetornarTokenValido() throws Exception {
    Token token =
        Token.builder()
            .token("token.jwt.teste")
            .tipo("Bearer")
            .idUsuario(UUID.randomUUID())
            .build();
    Mockito.when(autenticacaoService.autentica(any())).thenReturn(token);

    AutenticacaoRequest request =
        AutenticacaoRequest.builder().usuario("teste@teste.com").senha("123456").build();

    mockMvc
        .perform(
            post("/public/autenticacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("token.jwt.teste"))
        .andExpect(jsonPath("$.tipo").value("Bearer"));
  }

  @Test
  @DisplayName("Deve revalidar token expirado e retornar novo token")
  void reativaAutenticacao_DeveRetornarNovoToken() throws Exception {
    Token token =
        Token.builder().token("novo.token.jwt").tipo("Bearer").idUsuario(UUID.randomUUID()).build();
    Mockito.when(autenticacaoService.reativaToken(eq("tokenExpirado"))).thenReturn(token);

    mockMvc
        .perform(
            post("/public/autenticacao/reativacao").header("Authorization", "Bearer tokenExpirado"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("novo.token.jwt"))
        .andExpect(jsonPath("$.tipo").value("Bearer"));
  }
}
