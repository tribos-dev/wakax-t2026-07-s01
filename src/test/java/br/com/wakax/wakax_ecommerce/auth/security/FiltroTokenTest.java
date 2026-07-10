package br.com.wakax.wakax_ecommerce.auth.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.wakax.wakax_ecommerce.auth.credencial.application.service.CredencialService;
import br.com.wakax.wakax_ecommerce.auth.credencial.domain.Credencial;
import br.com.wakax.wakax_ecommerce.auth.security.service.TokenService;
import br.com.wakax.wakax_ecommerce.handler.APIException;

class FiltroTokenTest {
  @Mock private TokenService tokenService;
  @Mock private CredencialService credencialService;
  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  @Mock private FilterChain filterChain;
  @Mock private Credencial credencial;

  @InjectMocks private FiltroToken filtroToken;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    filtroToken = new FiltroToken(tokenService, credencialService);
    // Mock padrão para evitar NullPointerException
    when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.emptyList()));
  }

  @Test
  @DisplayName("Deve autenticar usuário com token válido")
  void doFilterInternal_TokenValido() throws ServletException, IOException {
    when(request.getHeader("Authorization")).thenReturn("Bearer tokenValido");
    when(tokenService.getUsuario("tokenValido")).thenReturn(Optional.of("usuario@teste.com"));
    when(credencialService.buscaCredencialPorUsuario("usuario@teste.com")).thenReturn(credencial);

    filtroToken.doFilterInternal(request, response, filterChain);

    verify(tokenService).getUsuario("tokenValido");
    verify(credencialService).buscaCredencialPorUsuario("usuario@teste.com");
    verify(filterChain).doFilter(request, response);
  }

  @Test
  @DisplayName("Deve lançar exceção quando token é inválido")
  void doFilterInternal_TokenInvalido() {
    when(request.getHeader("Authorization")).thenReturn("Bearer tokenInvalido");
    when(tokenService.getUsuario("tokenInvalido")).thenReturn(Optional.empty());

    APIException ex =
        assertThrows(
            APIException.class,
            () -> {
              filtroToken.doFilterInternal(request, response, filterChain);
            });
    assertEquals("O Token enviado está inválido. Tente novamente.", ex.getMessage());
  }

  @Test
  @DisplayName("Deve lançar exceção quando não há token no header")
  void doFilterInternal_SemToken() {
    when(request.getHeader("Authorization")).thenReturn(null);
    APIException ex =
        assertThrows(
            APIException.class,
            () -> {
              filtroToken.doFilterInternal(request, response, filterChain);
            });
    assertEquals("Token não está presente na requisição!", ex.getMessage());
  }

  @Test
  @DisplayName("Não deve filtrar rotas públicas")
  void shouldNotFilter_RotaPublica() throws ServletException {
    when(request.getRequestURI()).thenReturn("/public/autenticacao");
    assertTrue(filtroToken.shouldNotFilter(request));
  }

  @Test
  @DisplayName("Deve filtrar rotas protegidas")
  void shouldNotFilter_RotaProtegida() throws ServletException {
    when(request.getRequestURI()).thenReturn("/api/protegida");
    assertFalse(filtroToken.shouldNotFilter(request));
  }
}
