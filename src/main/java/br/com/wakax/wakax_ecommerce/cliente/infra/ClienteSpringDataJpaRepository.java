package br.com.wakax.wakax_ecommerce.cliente.infra;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;

public interface ClienteSpringDataJpaRepository extends JpaRepository<Cliente, UUID> {}
