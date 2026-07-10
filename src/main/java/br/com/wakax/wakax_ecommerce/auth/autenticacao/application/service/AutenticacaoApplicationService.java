package br.com.wakax.wakax_ecommerce.auth.autenticacao.application.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import br.com.wakax.wakax_ecommerce.auth.autenticacao.domain.Token;

public interface AutenticacaoApplicationService {
  Token autentica(UsernamePasswordAuthenticationToken userCredentials);

  Token reativaToken(String tokenExpirado);
}
