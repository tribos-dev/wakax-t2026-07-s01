package br.com.wakax.wakax_ecommerce.auth.credencial.infra;

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

import br.com.wakax.wakax_ecommerce.auth.credencial.domain.Credencial;
import br.com.wakax.wakax_ecommerce.auth.usuario.domain.Usuario;
import br.com.wakax.wakax_ecommerce.handler.APIException;

@ExtendWith(MockitoExtension.class)
class CredencialRepositoryJPATest {

  @Mock private CredencialJPARepository credencialJPARepository;

  @Mock private Credencial credencial;

  @Mock private Usuario usuario;

  @InjectMocks private CredencialRepositoryJPA credencialRepositoryJPA;

  private String usuarioEmail;
  private UUID idUsuario;

  @BeforeEach
  void setUp() {
    idUsuario = UUID.randomUUID();
    usuarioEmail = "usuario@teste.com";
  }

  @Test
  void salva_DeveSalvarCredencialComSucesso_QuandoCredencialEValida() {
    when(credencialJPARepository.save(any(Credencial.class))).thenReturn(credencial);

    Credencial resultado = credencialRepositoryJPA.salva(credencial);

    assertNotNull(resultado);
    assertEquals(credencial, resultado);
    verify(credencialJPARepository).save(credencial);
  }

  @Test
  void salva_DeveLancarExcecao_QuandoRepositoryFalha() {
    when(credencialJPARepository.save(any(Credencial.class)))
        .thenThrow(new RuntimeException("Erro ao salvar credencial"));

    assertThrows(
        RuntimeException.class,
        () -> {
          credencialRepositoryJPA.salva(credencial);
        });
    verify(credencialJPARepository).save(credencial);
  }

  @Test
  void buscaCredencialPorUsuario_DeveRetornarCredencial_QuandoUsuarioExiste() {
    when(credencialJPARepository.findByUsuario(usuarioEmail)).thenReturn(Optional.of(credencial));

    Credencial resultado = credencialRepositoryJPA.buscaCredencialPorUsuario(usuarioEmail);

    assertNotNull(resultado);
    assertEquals(credencial, resultado);
    verify(credencialJPARepository).findByUsuario(usuarioEmail);
  }

  @Test
  void buscaCredencialPorUsuario_DeveLancarAPIException_QuandoUsuarioNaoExiste() {
    when(credencialJPARepository.findByUsuario(usuarioEmail)).thenReturn(Optional.empty());

    APIException exception =
        assertThrows(
            APIException.class,
            () -> {
              credencialRepositoryJPA.buscaCredencialPorUsuario(usuarioEmail);
            });

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
    assertEquals("Não existe credencial para o Usuario informado!", exception.getMessage());

    verify(credencialJPARepository).findByUsuario(usuarioEmail);
  }

  @Test
  void buscaCredencialPorUsuario_DeveLancarAPIException_QuandoUsuarioENull() {
    APIException exception =
        assertThrows(
            APIException.class,
            () -> {
              credencialRepositoryJPA.buscaCredencialPorUsuario(null);
            });

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
    assertEquals("Não existe credencial para o Usuario informado!", exception.getMessage());
  }

  @Test
  void buscaCredencialPorUsuario_DeveLancarAPIException_QuandoUsuarioEVazio() {
    APIException exception =
        assertThrows(
            APIException.class,
            () -> {
              credencialRepositoryJPA.buscaCredencialPorUsuario("");
            });

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
    assertEquals("Não existe credencial para o Usuario informado!", exception.getMessage());
  }

  @Test
  void buscaCredencialPorUsuario_DeveLancarExcecao_QuandoRepositoryFalha() {
    when(credencialJPARepository.findByUsuario(usuarioEmail))
        .thenThrow(new RuntimeException("Erro no banco de dados"));

    assertThrows(
        RuntimeException.class,
        () -> {
          credencialRepositoryJPA.buscaCredencialPorUsuario(usuarioEmail);
        });

    verify(credencialJPARepository).findByUsuario(usuarioEmail);
  }

  @Test
  void buscaCredencialPorUsuario_DeveRetornarCredencialCorreta_QuandoMultiplosUsuariosExistem() {
    String usuarioEmail1 = "usuario1@teste.com";
    String usuarioEmail2 = "usuario2@teste.com";

    Credencial credencial1 = mock(Credencial.class);
    Credencial credencial2 = mock(Credencial.class);

    when(credencialJPARepository.findByUsuario(usuarioEmail1)).thenReturn(Optional.of(credencial1));
    when(credencialJPARepository.findByUsuario(usuarioEmail2)).thenReturn(Optional.of(credencial2));

    Credencial resultado1 = credencialRepositoryJPA.buscaCredencialPorUsuario(usuarioEmail1);
    Credencial resultado2 = credencialRepositoryJPA.buscaCredencialPorUsuario(usuarioEmail2);

    assertEquals(credencial1, resultado1);
    assertEquals(credencial2, resultado2);

    verify(credencialJPARepository).findByUsuario(usuarioEmail1);
    verify(credencialJPARepository).findByUsuario(usuarioEmail2);
  }

  @Test
  void salva_DeveRetornarCredencialSalva_QuandoSalvamentoESucesso() {
    Credencial credencialParaSalvar = new Credencial(usuario, usuarioEmail, "senha123");
    when(credencialJPARepository.save(any(Credencial.class))).thenReturn(credencialParaSalvar);

    Credencial resultado = credencialRepositoryJPA.salva(credencialParaSalvar);

    assertNotNull(resultado);
    assertEquals(credencialParaSalvar, resultado);
    verify(credencialJPARepository).save(credencialParaSalvar);
  }
}
