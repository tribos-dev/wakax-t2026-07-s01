package br.com.wakax.wakax_ecommerce.auth.autenticacao.application.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import br.com.wakax.wakax_ecommerce.auth.autenticacao.domain.Token;
import br.com.wakax.wakax_ecommerce.auth.credencial.application.service.CredencialService;
import br.com.wakax.wakax_ecommerce.auth.credencial.domain.Credencial;
import br.com.wakax.wakax_ecommerce.auth.security.service.TokenService;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class AutenticacaoService implements AutenticacaoApplicationService {
  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;
  private final CredencialService credencialService;

  @Override
  public Token autentica(UsernamePasswordAuthenticationToken userCredentials) {
    log.info("[inicio] AutenticacaoService - autentica");
    var authentication = authenticationManager.authenticate(userCredentials);
    Credencial credencial = (Credencial) authentication.getPrincipal();
    Token token =
        Token.builder()
            .tipo("Bearer")
            .token(tokenService.gerarToken(authentication))
            .idUsuario(credencial.getUser().getIdUsuario())
            .build();
    log.info("[finaliza] AutenticacaoService - autentica");
    return token;
  }

  @Override
  public Token reativaToken(String tokenExpirado) {
    log.info("[inicio] service - reativaAutenticacao");
    var usuario = extraiUsuario(tokenExpirado);
    Credencial credencial = credencialService.buscaCredencialPorUsuario(usuario);
    log.info("[finaliza] service - reativaAutenticacao");
    return Token.builder().tipo("Bearer").token(tokenService.gerarToken(credencial)).build();
  }

  private String extraiUsuario(String tokenExpirado) {
    return tokenService
        .getUsuario(tokenExpirado)
        .orElseThrow(() -> APIException.build(HttpStatus.BAD_REQUEST, "Token Invalido!"));
  }
}
