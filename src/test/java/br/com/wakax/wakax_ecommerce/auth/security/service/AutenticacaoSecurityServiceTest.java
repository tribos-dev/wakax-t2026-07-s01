package br.com.wakax.wakax_ecommerce.auth.security.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import br.com.wakax.wakax_ecommerce.auth.credencial.application.repository.CredencialRepository;
import br.com.wakax.wakax_ecommerce.auth.credencial.domain.Credencial;
import br.com.wakax.wakax_ecommerce.handler.APIException;

@ExtendWith(MockitoExtension.class)
class AutenticacaoSecurityServiceTest {

  @Mock private CredencialRepository credencialRepository;

  @Mock private Credencial credencial;

  @InjectMocks private AutenticacaoSecurityService autenticacaoSecurityService;

  private String usuarioEmail;

  @BeforeEach
  void setUp() {
    usuarioEmail = "usuario@teste.com";
  }

  @Test
  void loadUserByUsername_DeveRetornarUserDetails_QuandoUsuarioExiste() {
    when(credencialRepository.buscaCredencialPorUsuario(usuarioEmail)).thenReturn(credencial);

    UserDetails resultado = autenticacaoSecurityService.loadUserByUsername(usuarioEmail);

    assertNotNull(resultado);
    assertEquals(credencial, resultado);

    verify(credencialRepository).buscaCredencialPorUsuario(usuarioEmail);
  }

  @Test
  void loadUserByUsername_DeveLancarAPIException_QuandoUsuarioNaoExiste() {
    when(credencialRepository.buscaCredencialPorUsuario(usuarioEmail)).thenReturn(null);

    APIException exception =
        assertThrows(
            APIException.class,
            () -> {
              autenticacaoSecurityService.loadUserByUsername(usuarioEmail);
            });

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
    assertEquals("Não existe credencial para o Usuario informado!", exception.getMessage());

    verify(credencialRepository).buscaCredencialPorUsuario(usuarioEmail);
  }

  @Test
  void loadUserByUsername_DeveLancarAPIException_QuandoRepositoryLancaExcecao() {
    when(credencialRepository.buscaCredencialPorUsuario(usuarioEmail))
        .thenThrow(new RuntimeException("Erro no banco de dados"));

    assertThrows(
        RuntimeException.class,
        () -> {
          autenticacaoSecurityService.loadUserByUsername(usuarioEmail);
        });

    verify(credencialRepository).buscaCredencialPorUsuario(usuarioEmail);
  }

  @Test
  void loadUserByUsername_DeveLancarAPIException_QuandoUsuarioENull() {
    APIException exception =
        assertThrows(
            APIException.class,
            () -> {
              autenticacaoSecurityService.loadUserByUsername(null);
            });

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
    assertEquals("Não existe credencial para o Usuario informado!", exception.getMessage());
  }

  @Test
  void loadUserByUsername_DeveLancarAPIException_QuandoUsuarioEVazio() {
    APIException exception =
        assertThrows(
            APIException.class,
            () -> {
              autenticacaoSecurityService.loadUserByUsername("");
            });

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
    assertEquals("Não existe credencial para o Usuario informado!", exception.getMessage());
  }

  @Test
  void loadUserByUsername_DeveRetornarCredencialCorreta_QuandoMultiplosUsuariosExistem() {
    String usuarioEmail1 = "usuario1@teste.com";
    String usuarioEmail2 = "usuario2@teste.com";

    Credencial credencial1 = mock(Credencial.class);
    Credencial credencial2 = mock(Credencial.class);

    when(credencialRepository.buscaCredencialPorUsuario(usuarioEmail1)).thenReturn(credencial1);
    when(credencialRepository.buscaCredencialPorUsuario(usuarioEmail2)).thenReturn(credencial2);

    UserDetails resultado1 = autenticacaoSecurityService.loadUserByUsername(usuarioEmail1);
    UserDetails resultado2 = autenticacaoSecurityService.loadUserByUsername(usuarioEmail2);

    assertEquals(credencial1, resultado1);
    assertEquals(credencial2, resultado2);

    verify(credencialRepository).buscaCredencialPorUsuario(usuarioEmail1);
    verify(credencialRepository).buscaCredencialPorUsuario(usuarioEmail2);
  }

  @Test
  void loadUserByUsername_DeveImplementarUserDetailsService() {
    assertInstanceOf(UserDetailsService.class, autenticacaoSecurityService);
  }
}
