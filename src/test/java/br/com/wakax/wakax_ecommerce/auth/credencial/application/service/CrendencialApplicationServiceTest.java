package br.com.wakax.wakax_ecommerce.auth.credencial.application.service;

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

import br.com.wakax.wakax_ecommerce.auth.credencial.application.repository.CredencialRepository;
import br.com.wakax.wakax_ecommerce.auth.credencial.domain.Credencial;
import br.com.wakax.wakax_ecommerce.auth.usuario.api.UsuarioNovoRequest;
import br.com.wakax.wakax_ecommerce.auth.usuario.domain.Usuario;

@ExtendWith(MockitoExtension.class)
class CrendencialApplicationServiceTest {

  @Mock private CredencialRepository credencialRepository;

  @Mock private Usuario usuario;

  @Mock private Credencial credencial;

  @InjectMocks private CrendencialApplicationService credencialApplicationService;

  private UsuarioNovoRequest usuarioNovoRequest;
  private UUID idUsuario;

  @BeforeEach
  void setUp() {
    idUsuario = UUID.randomUUID();
    usuarioNovoRequest = new UsuarioNovoRequest("usuario@teste.com", "senha123");
  }

  @Test
  void criaNovaCredencial_DeveCriarCredencialComSucesso_QuandoDadosSaoValidos() {
    when(credencialRepository.salva(any(Credencial.class))).thenReturn(credencial);

    assertDoesNotThrow(
        () -> {
          credencialApplicationService.criaNovaCredencial(usuario, usuarioNovoRequest);
        });

    verify(credencialRepository).salva(any(Credencial.class));
  }

  @Test
  void criaNovaCredencial_DeveLancarExcecao_QuandoUsuarioNovoRequestENull() {
    assertThrows(
        NullPointerException.class,
        () -> {
          credencialApplicationService.criaNovaCredencial(usuario, null);
        });

    verify(credencialRepository, never()).salva(any());
  }

  @Test
  void criaNovaCredencial_DeveLancarExcecao_QuandoRepositoryFalha() {
    when(credencialRepository.salva(any(Credencial.class)))
        .thenThrow(new RuntimeException("Erro ao salvar credencial"));

    assertThrows(
        RuntimeException.class,
        () -> {
          credencialApplicationService.criaNovaCredencial(usuario, usuarioNovoRequest);
        });

    verify(credencialRepository).salva(any(Credencial.class));
  }

  @Test
  void buscaCredencialPorUsuario_DeveRetornarCredencial_QuandoUsuarioExiste() {
    String usuarioEmail = "usuario@teste.com";
    when(credencialRepository.buscaCredencialPorUsuario(usuarioEmail)).thenReturn(credencial);

    Credencial resultado = credencialApplicationService.buscaCredencialPorUsuario(usuarioEmail);

    assertNotNull(resultado);
    assertEquals(credencial, resultado);

    verify(credencialRepository).buscaCredencialPorUsuario(usuarioEmail);
  }

  @Test
  void buscaCredencialPorUsuario_DeveLancarExcecao_QuandoUsuarioNaoExiste() {
    String usuarioEmail = "usuario.inexistente@teste.com";
    when(credencialRepository.buscaCredencialPorUsuario(usuarioEmail))
        .thenThrow(new RuntimeException("Usuário não encontrado"));

    assertThrows(
        RuntimeException.class,
        () -> {
          credencialApplicationService.buscaCredencialPorUsuario(usuarioEmail);
        });

    verify(credencialRepository).buscaCredencialPorUsuario(usuarioEmail);
  }

  @Test
  void buscaCredencialPorUsuario_DeveRetornarCredencialCorreta_QuandoMultiplosUsuariosExistem() {
    String usuarioEmail1 = "usuario1@teste.com";
    String usuarioEmail2 = "usuario2@teste.com";

    Credencial credencial1 = mock(Credencial.class);
    Credencial credencial2 = mock(Credencial.class);

    when(credencialRepository.buscaCredencialPorUsuario(usuarioEmail1)).thenReturn(credencial1);
    when(credencialRepository.buscaCredencialPorUsuario(usuarioEmail2)).thenReturn(credencial2);

    Credencial resultado1 = credencialApplicationService.buscaCredencialPorUsuario(usuarioEmail1);
    Credencial resultado2 = credencialApplicationService.buscaCredencialPorUsuario(usuarioEmail2);

    assertEquals(credencial1, resultado1);
    assertEquals(credencial2, resultado2);

    verify(credencialRepository).buscaCredencialPorUsuario(usuarioEmail1);
    verify(credencialRepository).buscaCredencialPorUsuario(usuarioEmail2);
  }
}
