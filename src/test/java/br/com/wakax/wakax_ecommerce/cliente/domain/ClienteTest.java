package br.com.wakax.wakax_ecommerce.cliente.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.ClienteRequest;
import br.com.wakax.wakax_ecommerce.handler.APIException;

class ClienteTest {

  private ClienteRequest umClienteRequestValido() {
    return new ClienteRequest();
  }

  private Cliente clienteInativo() {
    Cliente cliente = new Cliente(umClienteRequestValido());
    ReflectionTestUtils.setField(cliente, "status", StatusCliente.INATIVO);
    return cliente;
  }

  @Test
  @DisplayName("WX-17 Cenário 1: ativar cliente INATIVO muda status para ATIVO")
  void ativar_clienteInativo_tornaStatusAtivo() {
    Cliente cliente = clienteInativo();
    cliente.ativar();
    assertThat(cliente.getStatus()).isEqualTo(StatusCliente.ATIVO);
  }

  @Test
  @DisplayName("WX-17 Cenário 4: ativar cliente já ATIVO lança CONFLICT")
  void ativar_clienteJaAtivo_lancaConflict() {
    Cliente cliente = new Cliente(umClienteRequestValido()); // nasce ATIVO
    assertThatThrownBy(cliente::ativar)
        .isInstanceOf(APIException.class)
        .hasMessageContaining("já está ativo");
  }

  @Test
  @DisplayName("WX-26 Cenário 1: desativar cliente ATIVO muda status para INATIVO")
  void desativar_clienteAtivo_tornaStatusInativo() {
    Cliente cliente = new Cliente(umClienteRequestValido());

    cliente.desativar();

    assertThat(cliente.getStatus()).isEqualTo(StatusCliente.INATIVO);
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
