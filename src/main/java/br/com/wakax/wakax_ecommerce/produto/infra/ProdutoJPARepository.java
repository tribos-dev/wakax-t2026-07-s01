package br.com.wakax.wakax_ecommerce.produto.infra;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;

public interface ProdutoJPARepository extends JpaRepository<Produto, UUID> {

  @Query("SELECT p FROM Produto p LEFT JOIN FETCH p.precos WHERE p.id = :id")
  Optional<Produto> findByIdComPrecos(@Param("id") UUID id);

  @Query(
      """
      SELECT DISTINCT p, e.quantidadeDisponivel
      FROM Estoque e
      JOIN e.produto p
      LEFT JOIN p.precos precos
      WHERE p.status = :status
        AND e.quantidadeDisponivel > 0
      ORDER BY p.grupo ASC, p.descricao ASC
      """)
  List<Object[]> listaProdutosPorStatusComEstoque(@Param("status") StatusProduto status);

  boolean existsByDescricao(String descricao);
}
