package br.com.wakax.wakax_ecommerce.produto.infra;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;

public interface ProdutoJPARepository extends JpaRepository<Produto, UUID> {
  @Query("SELECT p FROM Produto p LEFT JOIN FETCH p.precos WHERE p.id = :id")
  Optional<Produto> findByIdComPrecos(@Param("id") UUID id);

  boolean existsByDescricao(String descricao);

  // Fase 1: pagina SÓ os ids. Como não há join, 1 linha = 1 produto, então o
  // "limit ? offset ?" é aplicado de verdade no banco (sem HHH000104).
  // countQuery é obrigatório porque o SELECT usa um @Query customizado.
  @Query(value = "SELECT p.id FROM Produto p", countQuery = "SELECT COUNT(p) FROM Produto p")
  Page<UUID> paginaIds(Pageable pageable);

  // Fase 2: busca os produtos desses ids JÁ com os preços (LEFT JOIN FETCH),
  // sem paginação -> a expansão do join é inofensiva. DISTINCT colapsa as
  // linhas duplicadas; o ORDER BY preserva a ordem alfabética (o IN não garante).
  @Query(
      "SELECT DISTINCT p FROM Produto p LEFT JOIN FETCH p.precos "
          + "WHERE p.id IN :ids ORDER BY p.descricao")
  List<Produto> buscaComPrecosPorIds(@Param("ids") List<UUID> ids);

  @Query(
      value =
          """
          SELECT p.id
          FROM Estoque e
          JOIN e.produto p
          WHERE p.status = :status
            AND e.quantidadeDisponivel > 0
          ORDER BY p.grupo ASC, p.descricao ASC, p.id ASC
          """,
      countQuery =
          """
          SELECT COUNT(p.id)
          FROM Estoque e
          JOIN e.produto p
          WHERE p.status = :status
            AND e.quantidadeDisponivel > 0
          """)
  Page<UUID> paginaIdsProdutosComEstoquePorStatus(
      @Param("status") StatusProduto status, Pageable pageable);

  @Query(
      """
      SELECT DISTINCT p, e.quantidadeDisponivel
      FROM Estoque e
      JOIN e.produto p
      LEFT JOIN FETCH p.precos
      WHERE p.id IN :ids
      """)
  List<Object[]> buscaProdutosComPrecosEQuantidadePorIds(@Param("ids") List<UUID> ids);
}
