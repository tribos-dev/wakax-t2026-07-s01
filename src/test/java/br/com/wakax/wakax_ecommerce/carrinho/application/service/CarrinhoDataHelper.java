package br.com.wakax.wakax_ecommerce.carrinho.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.carrinho.api.request.ItemCarrinhoRequest;
import br.com.wakax.wakax_ecommerce.carrinho.domain.Carrinho;
import br.com.wakax.wakax_ecommerce.carrinho.domain.ItemCarrinho;
import br.com.wakax.wakax_ecommerce.carrinho.domain.StatusCarrinho;
import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Pessoa;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;
import br.com.wakax.wakax_ecommerce.produto.domain.Preco;
import br.com.wakax.wakax_ecommerce.produto.domain.Produto;
import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;
import br.com.wakax.wakax_ecommerce.produto.domain.TipoPreco;

public final class CarrinhoDataHelper {

  private CarrinhoDataHelper() {}

  public static Produto criaProduto() {
    Produto produto =
        Produto.builder()
            .id(UUID.fromString("06a7f8a7-3b28-44d4-9467-5f1e3153ce1a"))
            .descricao("Placa de Vídeo RTX 9090")
            .status(StatusProduto.ATIVO)
            .pesoLiquido(new BigDecimal("0.850"))
            .pesoBruto(new BigDecimal("1.200"))
            .descricaoComplementar("A mais potente do mercado.")
            .grupo("HARDWARE")
            .unidade("UN")
            .estoqueMinimo(10)
            .estoqueMaximo(100)
            .precos(new ArrayList<>())
            .build();

    Preco preco =
        new Preco(UUID.randomUUID(), TipoPreco.PADRAO, new BigDecimal("15000.00"), produto);
    produto.getPrecos().add(preco);
    return produto;
  }

  public static Produto criaSegundoProdutoValido() {
    Produto produto =
        Produto.builder()
            .id(UUID.fromString("e6f21a43-0545-4202-848d-639ff3a093b4"))
            .descricao("Processador Atomic i99")
            .status(StatusProduto.ATIVO)
            .pesoLiquido(new BigDecimal("0.150"))
            .pesoBruto(new BigDecimal("0.300"))
            .grupo("HARDWARE")
            .unidade("UN")
            .precos(new ArrayList<>())
            .build();

    Preco preco =
        new Preco(UUID.randomUUID(), TipoPreco.PADRAO, new BigDecimal("4500.50"), produto);
    produto.getPrecos().add(preco);
    return produto;
  }

  public static Pessoa criaPessoaValida() {
    return new Pessoa(
        UUID.fromString("c2c2a2b0-8c23-4e38-a2b8-a2b8e3a2c2c2"),
        "João da Silva",
        "123.456.789-00",
        Collections.singletonList("joao.silva@email.com"),
        Collections.singletonList("11987654321"),
        new ArrayList<>(),
        StatusPessoa.ATIVO);
  }

  public static Cliente criaCliente() {
    return Cliente.builder()
        .id(UUID.fromString("a1a1a1a1-b2b2-c3c3-d4d4-e5e5e5e5e5e5"))
        .pessoa(criaPessoaValida())
        .dataCriacao(LocalDateTime.now())
        .dataEdicao(LocalDateTime.now())
        .build();
  }

  public static ItemCarrinhoRequest criaItemCarrinhoRequest(UUID idProduto) {
    ItemCarrinhoRequest request = new ItemCarrinhoRequest();
    try {
      java.lang.reflect.Field fieldId = request.getClass().getDeclaredField("idProduto");
      fieldId.setAccessible(true);
      fieldId.set(request, idProduto);

      java.lang.reflect.Field fieldQtd = request.getClass().getDeclaredField("quantidade");
      fieldQtd.setAccessible(true);
      fieldQtd.set(request, 2);
    } catch (Exception e) {
      throw new RuntimeException("Falha ao criar ItemCarrinhoRequest para teste", e);
    }
    return request;
  }

  public static ItemCarrinho criaItemCarrinho() {
    Produto produto = criaProduto();
    return new ItemCarrinho(
        criaCarrinhoAtivoVazio(criaCliente()), produto, criaItemCarrinhoRequest(produto.getId()));
  }

  public static Carrinho criaCarrinhoAtivoVazio(Cliente cliente) {
    return Carrinho.builder()
        .id(UUID.fromString("3b7c8a6f-3c5d-4a1e-8f9a-2b7c8a6f3c5d"))
        .cliente(cliente)
        .dataCriacao(LocalDateTime.now())
        .statusCarrinho(StatusCarrinho.ATIVO)
        .itensCarrinho(new ArrayList<>())
        .build();
  }

  public static Carrinho criaCarrinhoAtivoComUmItem(Cliente cliente) {
    Carrinho carrinho = criaCarrinhoAtivoVazio(cliente);
    Produto produto = criaProduto();

    ItemCarrinho item =
        ItemCarrinho.builder()
            .id(UUID.fromString("e5d4c3b2-a1f0-9e8d-7c6b-5a4f3e2d1c0b"))
            .carrinho(carrinho)
            .produto(produto)
            .quantidade(2)
            .build();

    carrinho.getItensCarrinho().add(item);
    return carrinho;
  }
}
