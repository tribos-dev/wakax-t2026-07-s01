package br.com.wakax.wakax_ecommerce.estoque.infra;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wakax.wakax_ecommerce.estoque.domain.Estoque;

public interface EstoqueJPARepository extends JpaRepository<Estoque, UUID> {
  Optional<Estoque> findByProdutoId(UUID produtoId);

  @Query(
      value =
          """
            SELECT e.id
            FROM Estoque e
            JOIN e.produto p
            WHERE p.estoqueMinimo IS NOT NULL
            AND e.quantidadeDisponivel <= p.estoqueMinimo
            ORDER BY p.descricao ASC, e.id ASC
          """,
      countQuery =
          """
          SELECT COUNT(e.id)
          FROM Estoque e
          JOIN e.produto p
          WHERE p.estoqueMinimo IS NOT NULL
          AND e.quantidadeDisponivel <= p.estoqueMinimo
          """)
  Page<UUID> buscaIdsEstoquesComQuantidadeMinima(Pageable pageable);

  @Query(
      value =
          """
        SELECT e.id
        FROM Estoque e
        JOIN e.produto p
        ORDER BY p.descricao ASC, e.id ASC
        """,
      countQuery = """
        SELECT COUNT(e.id)
        FROM Estoque e
        """)
  Page<UUID> buscaIdsTodosEstoques(Pageable pageable);

  @Query(
      value =
          """
        SELECT e.id
        FROM Estoque e
        JOIN e.produto p
        WHERE e.quantidadeDisponivel = 0
        ORDER BY p.descricao ASC, e.id ASC
        """,
      countQuery =
          """
        SELECT COUNT(e.id)
        FROM Estoque e
        WHERE e.quantidadeDisponivel = 0
        """)
  Page<UUID> buscaIdsEstoquesEmFalta(Pageable pageable);

  @Query(
      """
         SELECT DISTINCT e
         FROM Estoque e
         JOIN FETCH e.produto p
         LEFT JOIN FETCH p.precos
         WHERE e.id IN :ids
         """)
  List<Estoque> buscaEstoquesComProdutoEPrecos(@Param("ids") Collection<UUID> ids);

  @Query("""
        SELECT SUM(e.custoTotal)
        FROM Estoque e
        """)
  BigDecimal calculaValorTotalInventario();

  @Query(
      """
        SELECT SUM(e.custoTotal)
        FROM Estoque e
        WHERE e.quantidadeDisponivel = 0
        """)
  BigDecimal calculaValorTotalInventarioEmFalta();

  @Query(
      """
        SELECT SUM(e.custoTotal)
        FROM Estoque e
        JOIN e.produto p
        WHERE p.estoqueMinimo IS NOT NULL
        AND e.quantidadeDisponivel <= p.estoqueMinimo
        """)
  BigDecimal calculaValorTotalInventarioQuantidadeMinima();
}
