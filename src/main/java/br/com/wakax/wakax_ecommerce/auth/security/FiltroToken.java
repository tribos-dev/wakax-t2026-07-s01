package br.com.wakax.wakax_ecommerce.auth.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.wakax.wakax_ecommerce.auth.credencial.application.service.CredencialService;
import br.com.wakax.wakax_ecommerce.auth.credencial.domain.Credencial;
import br.com.wakax.wakax_ecommerce.auth.security.domain.ValidaConteudoAuthorizationHeader;
import br.com.wakax.wakax_ecommerce.auth.security.service.TokenService;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class FiltroToken extends OncePerRequestFilter {

  private final TokenService tokenService;
  private final CredencialService credencialService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    log.info("[inicio] Filtro - filtrando requisicao");
    String token = recuperaToken(request);
    autenticaCliente(token);
    log.info("[finaliza] Filtro - filtrando requisicao");
    filterChain.doFilter(request, response);
  }

  private void autenticaCliente(String token) {
    log.info("[inicio] autenticacaoCliente - utilizando token válido para autenticar o usuário");
    Credencial credencial = recuperaUsuario(token);
    var authenticationToken =
        new UsernamePasswordAuthenticationToken(credencial, null, credencial.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    log.info("[finaliza] autenticacaoCliente - utilizando token válido para autenticar o usuário");
  }

  private Credencial recuperaUsuario(String token) {
    var usuario =
        tokenService
            .getUsuario(token)
            .orElseThrow(
                () ->
                    APIException.build(
                        HttpStatus.FORBIDDEN, "O Token enviado está inválido. Tente novamente."));
    return credencialService.buscaCredencialPorUsuario(usuario);
  }

  private String recuperaToken(HttpServletRequest requestOpt) {
    log.info("[inicio] recuperaToken - extraindo o token dos cabecalhos da requisicao");
    var AuthorizationHeaderValueOpt =
        Optional.ofNullable(recuperaValorAuthorizationHeader(requestOpt));
    String AuthorizationHeaderValue =
        AuthorizationHeaderValueOpt.filter(new ValidaConteudoAuthorizationHeader())
            .orElseThrow(() -> APIException.build(HttpStatus.UNAUTHORIZED, "Token inválido!"));
    log.info("[finaliza] recuperaToken - extraindo o token dos cabecalhos da requisicao");
    return AuthorizationHeaderValue.substring(7, AuthorizationHeaderValue.length());
  }

  private String recuperaValorAuthorizationHeader(HttpServletRequest request) {
    log.info("Cabeçalhos recebidos: " + Collections.list(request.getHeaderNames()));
    log.info("Authorization Header: " + request.getHeader("Authorization"));

    String authorization = request.getHeader("Authorization");

    if (authorization == null || authorization.isBlank()) {
      log.error("Nenhum token foi encontrado nos cabeçalhos!");
      throw APIException.build(HttpStatus.FORBIDDEN, "Token não está presente na requisição!");
    }

    log.info("Token encontrado");
    return authorization;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();
    return path.contains("/public/") || path.contains("/swagger-ui/") || path.contains("/webhook");
  }
}
