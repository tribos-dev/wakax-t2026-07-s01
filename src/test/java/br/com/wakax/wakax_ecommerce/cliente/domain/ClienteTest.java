package br.com.wakax.wakax_ecommerce.cliente.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.ClienteRequest;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;

class ClienteTest {

  private ClienteRequest umClienteRequestValido() {
    return new ClienteRequest();
  }

  private Cliente clienteInativo() {
    Cliente cliente = new Cliente(umClienteRequestValido());
    ReflectionTestUtils.setField(cliente.getPessoa(), "status", StatusPessoa.INATIVO);
    return cliente;
  }

  @Test
  @DisplayName("WX-17 Cenário 1: ativar cliente INATIVO muda status para ATIVO")
  void ativar_clienteInativo_tornaStatusAtivo() {
    Cliente cliente = clienteInativo();

    cliente.ativar();

    assertThat(cliente.getPessoa().getStatus()).isEqualTo(StatusPessoa.ATIVO);
  }

  @Test
  @DisplayName("WX-17 Cenário 1 (extra): ativar registra dataAtivacao")
  void ativar_clienteInativo_registraDataAtivacao() {
    Cliente cliente = clienteInativo();

    cliente.ativar();

    assertThat(cliente.getDataAtivacao()).isNotNull();
  }

  @Test
  @DisplayName("WX-17 Cenário 4: ativar cliente já ATIVO lança CONFLICT")
  void ativar_clienteJaAtivo_lancaConflict() {
    Cliente cliente = new Cliente(umClienteRequestValido());

    assertThatThrownBy(cliente::ativar)
        .isInstanceOf(APIException.class)
        .hasMessageContaining("já está ativo");
  }

  @Test
  @DisplayName("WX-26 Cenário 1: desativar cliente ATIVO muda status para INATIVO")
  void desativar_clienteAtivo_tornaStatusInativo() {
    Cliente cliente = new Cliente(umClienteRequestValido());

    cliente.desativar();

    assertThat(cliente.getPessoa().getStatus()).isEqualTo(StatusPessoa.INATIVO);
  }

  @Test
  @DisplayName("WX-26 Cenário 1 (extra): desativar registra dataDesativacao")
  void desativar_clienteAtivo_registraDataDesativacao() {
    Cliente cliente = new Cliente(umClienteRequestValido());

    cliente.desativar();

    assertThat(cliente.getDataDesativacao()).isNotNull();
  }

  @Test
  @DisplayName("WX-26 Cenário 4: desativar cliente já INATIVO lança CONFLICT")
  void desativar_clienteJaInativo_lancaConflict() {
    Cliente cliente = new Cliente(umClienteRequestValido());
    cliente.desativar();

    assertThatThrownBy(cliente::desativar)
        .isInstanceOf(APIException.class)
        .hasMessageContaining("já está inativo");
  }
}
