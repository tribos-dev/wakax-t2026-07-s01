package br.com.wakax.wakax_ecommerce.pedido.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.*;

import br.com.wakax.wakax_ecommerce.carrinho.domain.Carrinho;
import br.com.wakax.wakax_ecommerce.carrinho.domain.ItemCarrinho;
import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import br.com.wakax.wakax_ecommerce.pedido.application.api.request.PedidoRequest;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
  @Id @GeneratedValue private UUID id;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  @NotNull
  private Cliente cliente;

  @Column(nullable = false)
  @NotNull
  private LocalDateTime dataPedido;

  @Column(name = "data_atualizacao", nullable = false)
  @NotNull
  private LocalDateTime dataAtualizacao;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @NotNull
  private StatusPedido status;

  @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
  @NotNull
  private List<ItemPedido> itensPedido;

  @Column(nullable = false)
  @NotNull
  @PositiveOrZero
  private BigDecimal valorTotal;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @NotNull
  private FormaPagamento formaPagamento;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  @NotNull
  private Endereco enderecoEntrega;

  @OneToOne(mappedBy = "pedido")
  private Rastreamento rastreamento;

  public Pedido(PedidoRequest request, Carrinho carrinho) {
    this.cliente = carrinho.getCliente();
    this.dataPedido = LocalDateTime.now();
    this.dataAtualizacao = LocalDateTime.now();
    this.status = StatusPedido.CRIADO;
    this.formaPagamento = request.getFormaPagamento();
    this.enderecoEntrega = carrinho.getCliente().getPessoa().getEnderecos().get(0);
    this.itensPedido = mapearItensCarrinhoParaPedido(carrinho.getItensCarrinho());
    this.valorTotal = calcularValorTotal();
  }

  private List<ItemPedido> mapearItensCarrinhoParaPedido(List<ItemCarrinho> itensCarrinho) {
    return itensCarrinho.stream().map(this::mapearItem).collect(Collectors.toList());
  }

  private ItemPedido mapearItem(ItemCarrinho item) {
    var produto = item.getProduto();
    var valor = produto.getPrecos().get(0).getValor();
    return ItemPedido.builder()
        .produto(produto)
        .quantidade(item.getQuantidade())
        .valorUnitario(valor)
        .pedido(this)
        .build();
  }

  private BigDecimal calcularValorTotal() {
    return itensPedido.stream()
        .map(i -> i.getValorUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public void marcarComoPago() {
    this.status = StatusPedido.PAGO;
  }

  public void aguardarPagamento() {
    this.status = StatusPedido.AGUARDANDO_PAGAMENTO;
  }
}
