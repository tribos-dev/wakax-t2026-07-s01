package br.com.wakax.wakax_ecommerce.produto.infra;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;

public interface ProdutoJPARepository extends JpaRepository<Produto, UUID> {
  @Query("SELECT p FROM Produto p LEFT JOIN FETCH p.precos WHERE p.id = :id")
  Optional<Produto> findByIdComPrecos(@Param("id") UUID id);

  boolean existsByDescricao(String descricao);
}
