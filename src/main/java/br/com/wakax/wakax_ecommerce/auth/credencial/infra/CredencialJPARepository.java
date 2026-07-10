package br.com.wakax.wakax_ecommerce.auth.credencial.infra;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wakax.wakax_ecommerce.auth.credencial.domain.Credencial;

public interface CredencialJPARepository extends JpaRepository<Credencial, UUID> {
  Optional<Credencial> findByUsuario(String usuario);
}
