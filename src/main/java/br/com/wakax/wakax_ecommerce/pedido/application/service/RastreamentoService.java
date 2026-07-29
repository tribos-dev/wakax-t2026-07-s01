package br.com.wakax.wakax_ecommerce.pedido.application.service;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.pedido.application.api.request.RastreamentoRequest;
import br.com.wakax.wakax_ecommerce.pedido.application.api.response.RastreamentoResponse;

public interface RastreamentoService {
  RastreamentoResponse cadastraRastreamento(UUID idPedido, RastreamentoRequest request);

  RastreamentoResponse consultaRastreamento(UUID idCliente, UUID idPedido);
}
