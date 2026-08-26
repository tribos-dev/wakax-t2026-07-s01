package br.com.wakax.wakax_ecommerce.cliente.application.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.ClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.ClienteResponse;
import br.com.wakax.wakax_ecommerce.cliente.application.repository.ClienteRepository;
import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;

@ExtendWith(MockitoExtension.class)
public class ClienteApplicationServiceTest {

  @Mock private ClienteRepository clienteRepository;

  @InjectMocks private ClienteApplicationService clienteApplicationService;

  private ClienteRequest umClienteRequestValido() {
    return new ClienteRequest();
  }

  @Test
  void deveBuscarTodosClientesComPaginacao() {
    Pageable pageable = PageRequest.of(0, 10);
    Cliente cliente = mock(Cliente.class);
    Page<Cliente> page = new PageImpl<>(List.of(cliente), pageable, 1);
    when(clienteRepository.buscarTodos(pageable)).thenReturn(page);
    Page<Cliente> response = clienteApplicationService.buscarTodosClientes(pageable);
    assertNotNull(response);
    assertEquals(1, response.getTotalElements());
    assertEquals(1, response.getContent().size());
    verify(clienteRepository, times(1)).buscarTodos(pageable);
  }

  @Test
  void deveRetornarListaVaziaQuandoNaoExistiremClientes() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<Cliente> page = new PageImpl<>(List.of(), pageable, 0);
    when(clienteRepository.buscarTodos(pageable)).thenReturn(page);
    Page<Cliente> response = clienteApplicationService.buscarTodosClientes(pageable);
    assertNotNull(response);
    assertEquals(0, response.getTotalElements());
    assertEquals(0, response.getContent().size());
    verify(clienteRepository, times(1)).buscarTodos(pageable);
  }

  @Test
  @DisplayName("WX-17 Cenário 1: ativa cliente INATIVO, salva e retorna response")
  void ativarCliente_clienteInativo_ativaESalva() {
    UUID id = UUID.randomUUID();
    Cliente cliente = new Cliente(umClienteRequestValido());
    ReflectionTestUtils.setField(cliente.getPessoa(), "status", StatusPessoa.INATIVO);

    when(clienteRepository.buscaClientePorId(id)).thenReturn(cliente);

    ClienteResponse response = clienteApplicationService.ativarCliente(id);

    assertThat(cliente.getPessoa().getStatus()).isEqualTo(StatusPessoa.ATIVO);
    assertThat(cliente.getDataAtivacao()).isNotNull();
    verify(clienteRepository).salva(cliente);
    assertThat(response).isNotNull();
  }

  @Test
  @DisplayName("WX-17 Cenário 3: cliente inexistente propaga NOT_FOUND")
  void ativarCliente_clienteNaoExiste_propagaExcecao() {
    UUID id = UUID.randomUUID();
    when(clienteRepository.buscaClientePorId(id))
        .thenThrow(APIException.build(HttpStatus.NOT_FOUND, "Cliente não encontrado."));

    assertThatThrownBy(() -> clienteApplicationService.ativarCliente(id))
        .isInstanceOf(APIException.class);

    verify(clienteRepository, never()).salva(any());
  }

  @Test
  @DisplayName("WX-26 Cenário 1: desativa cliente, salva e retorna response")
  void desativarCliente_clienteAtivo_desativaESalva() {
    UUID id = UUID.randomUUID();
    Cliente cliente = new Cliente(umClienteRequestValido());
    when(clienteRepository.buscaClientePorId(id)).thenReturn(cliente);

    ClienteResponse response = clienteApplicationService.desativarCliente(id);

    assertThat(cliente.getStatus()).isEqualTo(StatusCliente.INATIVO);
    verify(clienteRepository).salva(cliente);
    assertThat(response).isNotNull();
  }

  @Test
  @DisplayName("WX-26 Cenário 3: cliente inexistente propaga NOT_FOUND")
  void desativarCliente_clienteNaoExiste_propagaExcecao() {
    UUID id = UUID.randomUUID();
    when(clienteRepository.buscaClientePorId(id))
        .thenThrow(APIException.build(HttpStatus.NOT_FOUND, "Cliente não encontrado."));

    assertThatThrownBy(() -> clienteApplicationService.desativarCliente(id))
        .isInstanceOf(APIException.class);

    verify(clienteRepository, never()).salva(any());
  }
}
