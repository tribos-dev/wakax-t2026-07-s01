package br.com.wakax.wakax_ecommerce.auth.autenticacao.application.api;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.auth.autenticacao.domain.Token;
import lombok.Value;

@Value
public class TokenResponse {
  private String token;
  private String tipo;
  private UUID idUsuario;

  public TokenResponse(Token token) {
    this.token = token.getToken();
    this.tipo = token.getTipo();
    this.idUsuario = token.getIdUsuario();
  }
}
