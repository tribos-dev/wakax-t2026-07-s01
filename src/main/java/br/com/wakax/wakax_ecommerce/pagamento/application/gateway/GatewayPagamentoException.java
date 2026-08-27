package br.com.wakax.wakax_ecommerce.pagamento.application.gateway;

public class GatewayPagamentoException extends RuntimeException {

  public GatewayPagamentoException(String mensagem) {
    super(mensagem);
  }

  public GatewayPagamentoException(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }

  private static final long serialVersionUID = 1L;
}
