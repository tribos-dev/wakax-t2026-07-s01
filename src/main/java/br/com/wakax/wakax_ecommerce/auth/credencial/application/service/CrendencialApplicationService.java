package br.com.wakax.wakax_ecommerce.auth.credencial.application.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import br.com.wakax.wakax_ecommerce.auth.credencial.application.repository.CredencialRepository;
import br.com.wakax.wakax_ecommerce.auth.credencial.domain.Credencial;
import br.com.wakax.wakax_ecommerce.auth.usuario.api.UsuarioNovoRequest;
import br.com.wakax.wakax_ecommerce.auth.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrendencialApplicationService implements CredencialService {
  private final CredencialRepository credencialRepository;

  @Override
  public void criaNovaCredencial(Usuario usuario, @Valid UsuarioNovoRequest usuarioNovo) {
    log.info("[inicia] CrendencialService - criaNovaCredencial");
    var novaCredencial = new Credencial(usuario, usuarioNovo.getEmail(), usuarioNovo.getSenha());
    credencialRepository.salva(novaCredencial);
    log.info("[finaliza] CrendencialService - criaNovaCredencial");
  }

  @Override
  public Credencial buscaCredencialPorUsuario(String usuario) {
    log.info("[inicia] CredencialSpringDataJpaService - buscaCredencial");
    Credencial credencial = credencialRepository.buscaCredencialPorUsuario(usuario);
    log.info("[finaliza] CredencialSpringDataJpaService - buscaCredencial");
    return credencial;
  }
}
