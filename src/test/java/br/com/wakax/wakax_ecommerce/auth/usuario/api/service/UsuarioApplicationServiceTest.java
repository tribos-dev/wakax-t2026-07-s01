package br.com.wakax.wakax_ecommerce.auth.usuario.api.service;

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

import br.com.wakax.wakax_ecommerce.auth.credencial.application.service.CredencialService;
import br.com.wakax.wakax_ecommerce.auth.usuario.api.UsuarioCriadoResponse;
import br.com.wakax.wakax_ecommerce.auth.usuario.api.UsuarioNovoRequest;
import br.com.wakax.wakax_ecommerce.auth.usuario.api.repository.UsuarioRepository;
import br.com.wakax.wakax_ecommerce.auth.usuario.domain.Usuario;

@ExtendWith(MockitoExtension.class)
class UsuarioApplicationServiceTest {

  @Mock private CredencialService credencialService;

  @Mock private UsuarioRepository usuarioRepository;

  @Mock private Usuario usuario;

  @InjectMocks private UsuarioApplicationService usuarioApplicationService;

  private UsuarioNovoRequest usuarioNovoRequest;
  private UUID idUsuario;

  @BeforeEach
  void setUp() {
    idUsuario = UUID.randomUUID();
    usuarioNovoRequest = new UsuarioNovoRequest("usuario@teste.com", "senha123");
  }

  @Test
  void criaNovoUsuario_DeveCriarUsuarioComSucesso_QuandoDadosSaoValidos() {
    when(usuarioRepository.salva(any(Usuario.class))).thenReturn(usuario);
    doNothing()
        .when(credencialService)
        .criaNovaCredencial(any(Usuario.class), any(UsuarioNovoRequest.class));

    UsuarioCriadoResponse resultado = usuarioApplicationService.criaNovoUsuario(usuarioNovoRequest);

    assertNotNull(resultado);

    verify(usuarioRepository).salva(any(Usuario.class));
    verify(credencialService).criaNovaCredencial(any(Usuario.class), eq(usuarioNovoRequest));
  }

  @Test
  void criaNovoUsuario_DeveLancarExcecao_QuandoUsuarioNovoRequestENull() {
    assertThrows(
        NullPointerException.class,
        () -> {
          usuarioApplicationService.criaNovoUsuario(null);
        });

    verify(usuarioRepository, never()).salva(any());
    verify(credencialService, never()).criaNovaCredencial(any(), any());
  }

  @Test
  void criaNovoUsuario_DeveLancarExcecao_QuandoRepositoryFalha() {
    when(usuarioRepository.salva(any(Usuario.class)))
        .thenThrow(new RuntimeException("Erro ao salvar usuário"));

    assertThrows(
        RuntimeException.class,
        () -> {
          usuarioApplicationService.criaNovoUsuario(usuarioNovoRequest);
        });

    verify(usuarioRepository).salva(any(Usuario.class));
    verify(credencialService, never()).criaNovaCredencial(any(), any());
  }

  @Test
  void criaNovoUsuario_DeveLancarExcecao_QuandoCredencialServiceFalha() {
    when(usuarioRepository.salva(any(Usuario.class))).thenReturn(usuario);
    doThrow(new RuntimeException("Erro ao criar credencial"))
        .when(credencialService)
        .criaNovaCredencial(any(Usuario.class), any(UsuarioNovoRequest.class));

    assertThrows(
        RuntimeException.class,
        () -> {
          usuarioApplicationService.criaNovoUsuario(usuarioNovoRequest);
        });

    verify(usuarioRepository).salva(any(Usuario.class));
    verify(credencialService).criaNovaCredencial(any(Usuario.class), eq(usuarioNovoRequest));
  }

  @Test
  void buscaUsuarioPorId_DeveRetornarUsuario_QuandoUsuarioExiste() {
    when(usuarioRepository.buscaUsuarioPorId(idUsuario)).thenReturn(usuario);

    UsuarioCriadoResponse resultado = usuarioApplicationService.buscaUsuarioPorId(idUsuario);

    assertNotNull(resultado);

    verify(usuarioRepository).buscaUsuarioPorId(idUsuario);
  }

  @Test
  void buscaUsuarioPorId_DeveLancarExcecao_QuandoUsuarioNaoExiste() {
    when(usuarioRepository.buscaUsuarioPorId(idUsuario))
        .thenThrow(new RuntimeException("Usuário não encontrado"));

    assertThrows(
        RuntimeException.class,
        () -> {
          usuarioApplicationService.buscaUsuarioPorId(idUsuario);
        });

    verify(usuarioRepository).buscaUsuarioPorId(idUsuario);
  }

  @Test
  void criaNovoUsuario_DeveCriarUsuarioComDadosCorretos() {
    when(usuarioRepository.salva(any(Usuario.class)))
        .thenAnswer(
            invocation -> {
              Usuario usuarioSalvo = invocation.getArgument(0);
              assertEquals(usuarioNovoRequest.getEmail(), usuarioSalvo.getEmail());
              return usuarioSalvo;
            });
    doNothing()
        .when(credencialService)
        .criaNovaCredencial(any(Usuario.class), any(UsuarioNovoRequest.class));

    UsuarioCriadoResponse resultado = usuarioApplicationService.criaNovoUsuario(usuarioNovoRequest);

    assertNotNull(resultado);
    verify(usuarioRepository).salva(any(Usuario.class));
    verify(credencialService).criaNovaCredencial(any(Usuario.class), eq(usuarioNovoRequest));
  }

  @Test
  void buscaUsuarioPorId_DeveRetornarUsuarioCorreto_QuandoMultiplosUsuariosExistem() {
    UUID idUsuario1 = UUID.randomUUID();
    UUID idUsuario2 = UUID.randomUUID();

    Usuario usuario1 = mock(Usuario.class);
    Usuario usuario2 = mock(Usuario.class);

    when(usuarioRepository.buscaUsuarioPorId(idUsuario1)).thenReturn(usuario1);
    when(usuarioRepository.buscaUsuarioPorId(idUsuario2)).thenReturn(usuario2);

    UsuarioCriadoResponse resultado1 = usuarioApplicationService.buscaUsuarioPorId(idUsuario1);
    UsuarioCriadoResponse resultado2 = usuarioApplicationService.buscaUsuarioPorId(idUsuario2);

    assertNotNull(resultado1);
    assertNotNull(resultado2);

    verify(usuarioRepository).buscaUsuarioPorId(idUsuario1);
    verify(usuarioRepository).buscaUsuarioPorId(idUsuario2);
  }
}
