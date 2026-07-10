package br.com.wakax.wakax_ecommerce.auth.usuario.infra;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import br.com.wakax.wakax_ecommerce.auth.usuario.api.UsuarioNovoRequest;
import br.com.wakax.wakax_ecommerce.auth.usuario.domain.Usuario;
import br.com.wakax.wakax_ecommerce.handler.APIException;

@ExtendWith(MockitoExtension.class)
class UsuaiorInfraRepositoryTest {

  @Mock private UsuarioJPARepository usuarioJPARepository;

  @Mock private Usuario usuario;

  @InjectMocks private UsuaiorInfraRepository usuaiorInfraRepository;

  private UUID idUsuario;

  @BeforeEach
  void setUp() {
    idUsuario = UUID.randomUUID();
  }

  @Test
  void salva_DeveSalvarUsuarioComSucesso_QuandoUsuarioEValido() {
    when(usuarioJPARepository.save(any(Usuario.class))).thenReturn(usuario);

    Usuario resultado = usuaiorInfraRepository.salva(usuario);

    assertNotNull(resultado);
    assertEquals(usuario, resultado);

    verify(usuarioJPARepository).save(usuario);
  }

  @Test
  void salva_DeveLancarExcecao_QuandoRepositoryFalha() {
    when(usuarioJPARepository.save(any(Usuario.class)))
        .thenThrow(new RuntimeException("Erro ao salvar usuário"));

    assertThrows(
        RuntimeException.class,
        () -> {
          usuaiorInfraRepository.salva(usuario);
        });

    verify(usuarioJPARepository).save(usuario);
  }

  @Test
  void buscaUsuarioPorId_DeveRetornarUsuario_QuandoUsuarioExiste() {
    when(usuarioJPARepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuario));

    Usuario resultado = usuaiorInfraRepository.buscaUsuarioPorId(idUsuario);

    assertNotNull(resultado);
    assertEquals(usuario, resultado);

    verify(usuarioJPARepository).findByIdUsuario(idUsuario);
  }

  @Test
  void buscaUsuarioPorId_DeveLancarAPIException_QuandoUsuarioNaoExiste() {
    when(usuarioJPARepository.findByIdUsuario(idUsuario)).thenReturn(Optional.empty());

    APIException exception =
        assertThrows(
            APIException.class,
            () -> {
              usuaiorInfraRepository.buscaUsuarioPorId(idUsuario);
            });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusException());
    assertEquals("Usuario não encontrado!", exception.getMessage());

    verify(usuarioJPARepository).findByIdUsuario(idUsuario);
  }

  @Test
  void buscaUsuarioPorId_DeveLancarAPIException_QuandoIdUsuarioENull() {
    APIException exception =
        assertThrows(
            APIException.class,
            () -> {
              usuaiorInfraRepository.buscaUsuarioPorId(null);
            });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusException());
    assertEquals("Usuario não encontrado!", exception.getMessage());
  }

  @Test
  void buscaUsuarioPorId_DeveLancarExcecao_QuandoRepositoryFalha() {
    when(usuarioJPARepository.findByIdUsuario(idUsuario))
        .thenThrow(new RuntimeException("Erro no banco de dados"));

    assertThrows(
        RuntimeException.class,
        () -> {
          usuaiorInfraRepository.buscaUsuarioPorId(idUsuario);
        });

    verify(usuarioJPARepository).findByIdUsuario(idUsuario);
  }

  @Test
  void buscaUsuarioPorId_DeveRetornarUsuarioCorreto_QuandoMultiplosUsuariosExistem() {
    UUID idUsuario1 = UUID.randomUUID();
    UUID idUsuario2 = UUID.randomUUID();

    Usuario usuario1 = mock(Usuario.class);
    Usuario usuario2 = mock(Usuario.class);

    when(usuarioJPARepository.findByIdUsuario(idUsuario1)).thenReturn(Optional.of(usuario1));
    when(usuarioJPARepository.findByIdUsuario(idUsuario2)).thenReturn(Optional.of(usuario2));

    Usuario resultado1 = usuaiorInfraRepository.buscaUsuarioPorId(idUsuario1);
    Usuario resultado2 = usuaiorInfraRepository.buscaUsuarioPorId(idUsuario2);

    assertEquals(usuario1, resultado1);
    assertEquals(usuario2, resultado2);

    verify(usuarioJPARepository).findByIdUsuario(idUsuario1);

    verify(usuarioJPARepository).findByIdUsuario(idUsuario2);
  }

  @Test
  void salva_DeveRetornarUsuarioSalvo_QuandoSalvamentoESucesso() {
    UsuarioNovoRequest usuarioNovoRequest = new UsuarioNovoRequest("usuario@teste.com", "senha123");
    Usuario usuarioParaSalvar = new Usuario(usuarioNovoRequest);
    when(usuarioJPARepository.save(any(Usuario.class))).thenReturn(usuarioParaSalvar);

    Usuario resultado = usuaiorInfraRepository.salva(usuarioParaSalvar);

    assertNotNull(resultado);
    assertEquals(usuarioParaSalvar, resultado);

    verify(usuarioJPARepository).save(usuarioParaSalvar);
  }
}
