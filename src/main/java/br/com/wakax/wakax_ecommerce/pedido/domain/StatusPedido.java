package br.com.wakax.wakax_ecommerce.pedido.domain;

public enum StatusPedido {
  CRIADO,
  PAGO,
  ENVIADO,
  ENTREGUE,
  CANCELADO,
  AGUARDANDO_PAGAMENTO;

  public boolean podeTransicionarPara(StatusPedido novoStatus) {
    if (novoStatus == null) {
      return false;
    }

    switch (this) {
      case CRIADO:
        return novoStatus == PAGO || novoStatus == AGUARDANDO_PAGAMENTO || novoStatus == CANCELADO;
      case AGUARDANDO_PAGAMENTO:
        return novoStatus == PAGO || novoStatus == CANCELADO;
      case PAGO:
        return novoStatus == ENVIADO || novoStatus == CANCELADO;
      case ENVIADO:
        return novoStatus == ENTREGUE;
      case ENTREGUE:
      case CANCELADO:
      default:
        return false;
    }
  }
}
