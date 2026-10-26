package br.com.wakax.wakax_ecommerce.produto.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProdutoTest {

    private Produto umProduto(StatusProduto status) {
        return Produto.builder()
                .descricao("Produto Teste")
                .status(status)
                .pesoLiquido(new BigDecimal("1.0"))
                .pesoBruto(new BigDecimal("1.5"))
                .build();
    }

    @Test
    @DisplayName("Alterar status: inativar produto ATIVO muda status para INATIVO")
    void alteraStatus_produtoAtivo_tornaStatusInativo() {
        Produto produto = umProduto(StatusProduto.ATIVO);

        produto.alteraStatus(StatusProduto.INATIVO, "Produto descontinuado");

        assertThat(produto.getStatus()).isEqualTo(StatusProduto.INATIVO);
    }

    @Test
    @DisplayName("Alterar status: ativar produto INATIVO muda status para ATIVO")
    void alteraStatus_produtoInativo_tornaStatusAtivo() {
        Produto produto = umProduto(StatusProduto.INATIVO);

        produto.alteraStatus(StatusProduto.ATIVO, "Produto voltou ao catalogo");

        assertThat(produto.getStatus()).isEqualTo(StatusProduto.ATIVO);
    }

    @Test
    @DisplayName("Alterar status: registra motivo e data da alteracao")
    void alteraStatus_statusDiferente_registraMotivoEData() {
        Produto produto = umProduto(StatusProduto.ATIVO);

        produto.alteraStatus(StatusProduto.INATIVO, "Produto descontinuado");

        assertThat(produto.getMotivoAlteracaoStatus()).isEqualTo("Produto descontinuado");
        assertThat(produto.getDataAlteracaoStatus()).isNotNull();
    }

    @Test
    @DisplayName("Alterar status: novo motivo substitui o anterior")
    void alteraStatus_segundaAlteracao_substituiMotivo() {
        Produto produto = umProduto(StatusProduto.ATIVO);
        produto.alteraStatus(StatusProduto.INATIVO, "Produto descontinuado");

        produto.alteraStatus(StatusProduto.ATIVO, "Produto voltou ao catalogo");

        assertThat(produto.getStatus()).isEqualTo(StatusProduto.ATIVO);
        assertThat(produto.getMotivoAlteracaoStatus()).isEqualTo("Produto voltou ao catalogo");
    }

    @Test
    @DisplayName("Alterar status: mesmo status nao altera motivo nem data")
    void alteraStatus_mesmoStatus_naoAlteraNada() {
        Produto produto = umProduto(StatusProduto.ATIVO);

        produto.alteraStatus(StatusProduto.ATIVO, "Sem mudanca");

        assertThat(produto.getStatus()).isEqualTo(StatusProduto.ATIVO);
        assertThat(produto.getMotivoAlteracaoStatus()).isNull();
        assertThat(produto.getDataAlteracaoStatus()).isNull();
    }
}