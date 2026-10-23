package br.com.wakax.wakax_ecommerce.pessoa.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationHelper {

  public static Optional<String> validaStringEmBranco(String valor) {
    if (valor == null || valor.isBlank()) {
      return Optional.empty();
    }
    return Optional.of(valor.trim());
  }

  public static Optional<List<String>> validaListaStrings(List<String> valores) {
    if (valores == null || valores.isEmpty()) {
      return Optional.empty();
    }
    List<String> informados = new ArrayList<>();
    for (String valor : valores) {
      validaStringEmBranco(valor).ifPresent(informados::add);
    }
    if (informados.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(informados);
  }
}
