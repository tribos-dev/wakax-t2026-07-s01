package br.com.wakax.wakax_ecommerce.auth.usuario.api.repository;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.auth.usuario.domain.Usuario;

public interface UsuarioRepository {
  Usuario salva(Usuario usuario);

  Usuario buscaUsuarioPorId(UUID idUsuario);
}
