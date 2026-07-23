package br.com.wakax.wakax_ecommerce.cliente.infra;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;

public interface ClienteSpringDataJpaRepository
    extends JpaRepository<Cliente, UUID>, JpaSpecificationExecutor<Cliente> {

  @EntityGraph(attributePaths = {"pessoa", "pessoa.emails"})
  @Query(
      """
            SELECT c
            FROM Cliente c
            JOIN c.pessoa p
            ORDER BY p.nome ASC
            """)
  Page<Cliente> buscarTodosClientes(Pageable pageable);
}
