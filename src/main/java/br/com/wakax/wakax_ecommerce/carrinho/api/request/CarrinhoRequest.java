package br.com.wakax.wakax_ecommerce.carrinho.api.request;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class CarrinhoRequest {
  @NotNull private UUID idCliente;
}
