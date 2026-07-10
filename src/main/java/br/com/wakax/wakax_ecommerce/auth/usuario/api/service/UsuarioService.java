package br.com.wakax.wakax_ecommerce.auth.usuario.api.service;

import java.util.UUID;

import javax.validation.Valid;

import br.com.wakax.wakax_ecommerce.auth.usuario.api.UsuarioCriadoResponse;
import br.com.wakax.wakax_ecommerce.auth.usuario.api.UsuarioNovoRequest;

public interface UsuarioService {

  UsuarioCriadoResponse criaNovoUsuario(@Valid UsuarioNovoRequest usuarioNovo);

  UsuarioCriadoResponse buscaUsuarioPorId(UUID idUsuario);
}
