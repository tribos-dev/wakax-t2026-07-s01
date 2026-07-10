package br.com.wakax.wakax_ecommerce.auth.credencial.application.service;

import javax.validation.Valid;

import br.com.wakax.wakax_ecommerce.auth.credencial.domain.Credencial;
import br.com.wakax.wakax_ecommerce.auth.usuario.api.UsuarioNovoRequest;
import br.com.wakax.wakax_ecommerce.auth.usuario.domain.Usuario;

public interface CredencialService {
  void criaNovaCredencial(Usuario usuario, @Valid UsuarioNovoRequest usuarioNovo);

  Credencial buscaCredencialPorUsuario(String usuario);
}
