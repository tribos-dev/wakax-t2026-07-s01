package br.com.wakax.wakax_ecommerce.auth.usuario.infra;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import br.com.wakax.wakax_ecommerce.auth.usuario.api.repository.UsuarioRepository;
import br.com.wakax.wakax_ecommerce.auth.usuario.domain.Usuario;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Repository
@Log4j2
@RequiredArgsConstructor
public class UsuaiorInfraRepository implements UsuarioRepository {

  private final UsuarioJPARepository usuarioJPARepository;

  @Override
  public Usuario salva(Usuario usuario) {
    log.info("[inicia] UsuaiorInfraRepository - salva");
    Usuario novoUsuario = usuarioJPARepository.save(usuario);
    log.info("[inicia] UsuaiorInfraRepository - salva");
    return novoUsuario;
  }

  @Override
  public Usuario buscaUsuarioPorId(UUID idUsuario) {
    log.info("[inicia] UsuaiorInfraRepository - buscaUsuarioPorId");
    Usuario usuario =
        usuarioJPARepository
            .findByIdUsuario(idUsuario)
            .orElseThrow(
                () -> APIException.build(HttpStatus.BAD_REQUEST, "Usuario não encontrado!"));
    log.info("[finaliza] UsuaiorInfraRepository - buscaUsuarioPorId");
    return usuario;
  }
}
