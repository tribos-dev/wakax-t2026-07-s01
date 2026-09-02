package br.com.wakax.wakax_ecommerce.cliente.application.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
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

import br.com.wakax.wakax_ecommerce.cliente.application.api.request.AtualizaClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.application.api.request.ClienteRequest;
import br.com.wakax.wakax_ecommerce.cliente.application.api.response.ClienteResponse;
import br.com.wakax.wakax_ecommerce.cliente.application.repository.ClienteRepository;
import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;

@ExtendWith(MockitoExtension.class)
public class ClienteApplicationServiceTest {

  @Mock private ClienteRepository clienteRepository;

  @InjectMocks private ClienteApplicationService clienteApplicationService;

  private ClienteRequest umClienteRequestValido() {
    return ClienteDataHelper.criaClienteRequestValido();
  }

  private UUID idCliente;
  private Cliente cliente;

  @BeforeEach
  void setUp() {
    cliente = ClienteDataHelper.criaClienteValido();
    idCliente = cliente.getId();
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
  void deveAtualizarNomeETelefoneDoClienteComSucesso() {
    AtualizaClienteRequest request = ClienteDataHelper.criaAtualizaClienteRequestValido();
    String documentoOriginal = cliente.getPessoa().getCpfCnpj();
    StatusPessoa statusOriginal = cliente.getPessoa().getStatus();

    when(clienteRepository.buscaClientePorId(idCliente)).thenReturn(cliente);
    when(clienteRepository.salva(cliente)).thenReturn(cliente);

    ClienteResponse response = clienteApplicationService.atualizaCliente(idCliente, request);

    assertNotNull(response);
    assertEquals(request.getNome(), response.getNome());
    assertEquals(request.getTelefones(), cliente.getPessoa().getTelefones());
    assertEquals(request.getEmails(), cliente.getPessoa().getEmails());
    assertEquals(documentoOriginal, response.getDocumento());
    assertEquals(statusOriginal, response.getStatusPessoa());
    assertNotNull(response.getDataEdicao());

    verify(clienteRepository).buscaClientePorId(idCliente);
    verify(clienteRepository).salva(cliente);
  }

  @Test
  void deveIncluirNovoEnderecoNaListaDoCliente() {
    AtualizaClienteRequest request = ClienteDataHelper.criaAtualizaClienteRequestValido();

    when(clienteRepository.buscaClientePorId(idCliente)).thenReturn(cliente);
    when(clienteRepository.salva(cliente)).thenReturn(cliente);

    ClienteResponse response = clienteApplicationService.atualizaCliente(idCliente, request);

    assertEquals(1, response.getEnderecos().size());
    assertEquals(request.getEnderecos().get(0).getCep(), response.getEnderecos().get(0).getCep());
    assertEquals(1, cliente.getPessoa().getEnderecos().size());
  }

  @Test
  void deveLancarExcecaoAoAtualizarClienteInexistente() {
    AtualizaClienteRequest request = ClienteDataHelper.criaAtualizaClienteRequestValido();
    when(clienteRepository.buscaClientePorId(idCliente))
        .thenThrow(new APIException(HttpStatus.NOT_FOUND, ErrorCode.CLIENTE_NAO_ENCONTRADO));

    APIException exception =
        assertThrows(
            APIException.class,
            () -> clienteApplicationService.atualizaCliente(idCliente, request));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
    assertEquals(ErrorCode.CLIENTE_NAO_ENCONTRADO, exception.getErrorCode());

    verify(clienteRepository).buscaClientePorId(idCliente);
    verify(clienteRepository, never()).salva(any());
  }
}
