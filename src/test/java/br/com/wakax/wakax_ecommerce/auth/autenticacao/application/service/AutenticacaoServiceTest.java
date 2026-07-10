package br.com.wakax.wakax_ecommerce.auth.autenticacao.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import br.com.wakax.wakax_ecommerce.auth.autenticacao.domain.Token;
import br.com.wakax.wakax_ecommerce.auth.credencial.application.service.CredencialService;
import br.com.wakax.wakax_ecommerce.auth.credencial.domain.Credencial;
import br.com.wakax.wakax_ecommerce.auth.security.service.TokenService;
import br.com.wakax.wakax_ecommerce.auth.usuario.domain.Usuario;
import br.com.wakax.wakax_ecommerce.handler.APIException;

@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {

  @Mock private AuthenticationManager authenticationManager;

  @Mock private TokenService tokenService;

  @Mock private CredencialService credencialService;

  @Mock private Authentication authentication;

  @Mock private Credencial credencial;

  @Mock private Usuario usuario;

  @InjectMocks private AutenticacaoService autenticacaoService;

  private UsernamePasswordAuthenticationToken userCredentials;
  private UUID idUsuario;
  private String tokenGerado;
  private String tokenExpirado;

  @BeforeEach
  void setUp() {
    idUsuario = UUID.randomUUID();
    tokenGerado = "token.gerado.jwt";
    tokenExpirado = "token.expirado.jwt";
    userCredentials = new UsernamePasswordAuthenticationToken("usuario@teste.com", "senha123");
  }

  @Test
  void autentica_DeveRetornarTokenValido_QuandoCredenciaisSaoValidas() {
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(credencial);
    when(credencial.getUser()).thenReturn(usuario);
    when(usuario.getIdUsuario()).thenReturn(idUsuario);
    when(tokenService.gerarToken(authentication)).thenReturn(tokenGerado);

    Token resultado = autenticacaoService.autentica(userCredentials);

    assertNotNull(resultado);
    assertEquals("Bearer", resultado.getTipo());
    assertEquals(tokenGerado, resultado.getToken());
    assertEquals(idUsuario, resultado.getIdUsuario());

    verify(authenticationManager).authenticate(userCredentials);
    verify(tokenService).gerarToken(authentication);
  }

  @Test
  void autentica_DeveLancarExcecao_QuandoAutenticacaoFalha() {
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new RuntimeException("Credenciais inválidas"));

    assertThrows(
        RuntimeException.class,
        () -> {
          autenticacaoService.autentica(userCredentials);
        });

    verify(authenticationManager).authenticate(userCredentials);
    verify(tokenService, never()).gerarToken((Authentication) any());
  }

  @Test
  void reativaToken_DeveRetornarNovoToken_QuandoTokenExpiradoEValido() {
    String usuarioExtraido = "usuario@teste.com";
    when(tokenService.getUsuario(tokenExpirado)).thenReturn(java.util.Optional.of(usuarioExtraido));
    when(credencialService.buscaCredencialPorUsuario(usuarioExtraido)).thenReturn(credencial);
    when(tokenService.gerarToken(credencial)).thenReturn(tokenGerado);

    Token resultado = autenticacaoService.reativaToken(tokenExpirado);

    assertNotNull(resultado);
    assertEquals("Bearer", resultado.getTipo());
    assertEquals(tokenGerado, resultado.getToken());

    verify(tokenService).getUsuario(tokenExpirado);
    verify(credencialService).buscaCredencialPorUsuario(usuarioExtraido);
    verify(tokenService).gerarToken(credencial);
  }

  @Test
  void reativaToken_DeveLancarAPIException_QuandoTokenEInvalido() {
    when(tokenService.getUsuario(tokenExpirado)).thenReturn(java.util.Optional.empty());

    APIException exception =
        assertThrows(
            APIException.class,
            () -> {
              autenticacaoService.reativaToken(tokenExpirado);
            });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusException());
    assertEquals("Token Invalido!", exception.getMessage());

    verify(tokenService).getUsuario(tokenExpirado);
    verify(credencialService, never()).buscaCredencialPorUsuario(any());
    verify(tokenService, never()).gerarToken((Authentication) any());
  }

  @Test
  void reativaToken_DeveLancarAPIException_QuandoTokenENull() {
    APIException exception =
        assertThrows(
            APIException.class,
            () -> {
              autenticacaoService.reativaToken(null);
            });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusException());
    assertEquals("Token Invalido!", exception.getMessage());
  }

  @Test
  void reativaToken_DeveLancarAPIException_QuandoTokenEVazio() {
    APIException exception =
        assertThrows(
            APIException.class,
            () -> {
              autenticacaoService.reativaToken("");
            });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusException());
    assertEquals("Token Invalido!", exception.getMessage());
  }
}
