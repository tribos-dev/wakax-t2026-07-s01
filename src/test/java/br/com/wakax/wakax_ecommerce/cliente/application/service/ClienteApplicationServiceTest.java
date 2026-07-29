package br.com.wakax.wakax_ecommerce.cliente.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.wakax.wakax_ecommerce.cliente.application.repository.ClienteRepository;
import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;

@ExtendWith(MockitoExtension.class)
public class ClienteApplicationServiceTest {

  @Mock private ClienteRepository clienteRepository;

  @InjectMocks private ClienteApplicationService clienteApplicationService;

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
}
