package br.com.wakax.wakax_ecommerce.auth.security.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.wakax.wakax_ecommerce.auth.credencial.domain.Credencial;
import br.com.wakax.wakax_ecommerce.auth.usuario.domain.Usuario;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

  @Mock private Authentication authentication;

  @Mock private Credencial credencial;

  @Mock private Usuario usuario;

  @InjectMocks private TokenService tokenService;

  private String usuarioEmail;

  @BeforeEach
  void setUp() {
    UUID idUsuario = UUID.randomUUID();
    usuarioEmail = "usuario@teste.com";

    ReflectionTestUtils.setField(tokenService, "expiracao", "60");
    ReflectionTestUtils.setField(
        tokenService, "chave", "chave-secreta-para-testes-jwt-token-service");
  }

  @Test
  void gerarToken_DeveGerarTokenValido_QuandoRecebeAuthentication() {
    when(authentication.getPrincipal()).thenReturn(credencial);
    when(credencial.getUsuario()).thenReturn(usuarioEmail);

    String token = tokenService.gerarToken(authentication);

    assertNotNull(token);
    assertFalse(token.isEmpty());
    assertTrue(token.contains("."));

    verify(authentication).getPrincipal();
  }

  @Test
  void gerarToken_DeveGerarTokenValido_QuandoRecebeCredencial() {
    when(credencial.getUsuario()).thenReturn(usuarioEmail);

    String token = tokenService.gerarToken(credencial);

    assertNotNull(token);
    assertFalse(token.isEmpty());
    assertTrue(token.contains("."));
  }

  @Test
  void getUsuario_DeveRetornarUsuario_QuandoTokenEValido() {
    when(credencial.getUsuario()).thenReturn(usuarioEmail);
    String token = tokenService.gerarToken(credencial);

    Optional<String> resultado = tokenService.getUsuario(token);

    assertTrue(resultado.isPresent());
    assertEquals(usuarioEmail, resultado.get());
  }

  @Test
  void getUsuario_DeveRetornarUsuario_QuandoTokenExpirado() {
    when(credencial.getUsuario()).thenReturn(usuarioEmail);
    String token = tokenService.gerarToken(credencial);

    Optional<String> resultado = tokenService.getUsuario(token);

    assertTrue(resultado.isPresent());
    assertEquals(usuarioEmail, resultado.get());
  }

  @Test
  void getUsuario_DeveRetornarEmpty_QuandoTokenEInvalido() {
    assertThrows(
        Exception.class,
        () -> {
          tokenService.getUsuario("token.invalido.jwt");
        });
  }

  @Test
  void getUsuario_DeveRetornarEmpty_QuandoTokenENull() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          tokenService.getUsuario(null);
        });
  }

  @Test
  void getUsuario_DeveRetornarEmpty_QuandoTokenEVazio() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          tokenService.getUsuario("");
        });
  }

  @Test
  void getUsuarioByBearerToken_DeveRetornarUsuario_QuandoBearerTokenEValido() {
    when(credencial.getUsuario()).thenReturn(usuarioEmail);
    String token = tokenService.gerarToken(credencial);
    String bearerToken = "Bearer " + token;

    Optional<String> resultado = tokenService.getUsuarioByBearerToken(bearerToken);

    assertTrue(resultado.isPresent());
    assertEquals(usuarioEmail, resultado.get());
  }

  @Test
  void getUsuarioByBearerToken_DeveRetornarEmpty_QuandoBearerTokenEInvalido() {
    assertThrows(
        Exception.class,
        () -> {
          tokenService.getUsuarioByBearerToken("Bearer token.invalido.jwt");
        });
  }

  @Test
  void getUsuarioByBearerToken_DeveRetornarEmpty_QuandoBearerTokenENull() {
    assertThrows(
        NullPointerException.class,
        () -> {
          tokenService.getUsuarioByBearerToken(null);
        });
  }

  @Test
  void getUsuarioByBearerToken_DeveRetornarEmpty_QuandoBearerTokenEVazio() {
    assertThrows(
        StringIndexOutOfBoundsException.class,
        () -> {
          tokenService.getUsuarioByBearerToken("");
        });
  }

  @Test
  void getUsuarioByBearerToken_DeveRetornarEmpty_QuandoBearerTokenNaoTemBearer() {
    assertThrows(
        Exception.class,
        () -> {
          tokenService.getUsuarioByBearerToken("Token token.invalido.jwt");
        });
  }

  @Test
  void getUsuarioByBearerToken_DeveRetornarEmpty_QuandoBearerTokenTemMenosDe7Caracteres() {
    assertThrows(
        StringIndexOutOfBoundsException.class,
        () -> {
          tokenService.getUsuarioByBearerToken("Bea");
        });
  }
}
