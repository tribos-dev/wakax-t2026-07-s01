package br.com.wakax.wakax_ecommerce.auth.credencial.application.repository;

import br.com.wakax.wakax_ecommerce.auth.credencial.domain.Credencial;

public interface CredencialRepository {
  Credencial salva(Credencial credencial);

  Credencial buscaCredencialPorUsuario(String usuario);
}
