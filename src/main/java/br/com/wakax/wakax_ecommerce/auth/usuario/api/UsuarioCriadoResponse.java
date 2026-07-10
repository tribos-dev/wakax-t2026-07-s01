package br.com.wakax.wakax_ecommerce.auth.usuario.api;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.auth.usuario.domain.Usuario;
import lombok.Value;

@Value
public class UsuarioCriadoResponse {

  private final UUID idUsuario;
  private final String email;

  public UsuarioCriadoResponse(Usuario usuario) {
    this.idUsuario = usuario.getIdUsuario();
    this.email = usuario.getEmail();
  }
}
