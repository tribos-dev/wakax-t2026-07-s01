package br.com.wakax.wakax_ecommerce.cliente.infra;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import br.com.wakax.wakax_ecommerce.cliente.application.repository.ClienteRepository;
import br.com.wakax.wakax_ecommerce.cliente.domain.Cliente;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ClienteInfraRepository implements ClienteRepository {
  private final ClienteSpringDataJpaRepository clienteSpringDataJpaRepository;

  public Cliente salva(Cliente cliente) {
    log.info("[start] ClienteInfraRepository - salva");
    Cliente clienteSalvo = clienteSpringDataJpaRepository.save(cliente);
    log.debug("[finish] ClienteInfraRepository - salva");
    return clienteSalvo;
  }

  @Override
  public Cliente buscaClientePorId(UUID idCliente) {
    log.info("[start] ClienteInfraRepository - buscaClientePorId");
    Cliente cliente =
        clienteSpringDataJpaRepository
            .findById(idCliente)
            .orElseThrow(
                () -> new APIException(HttpStatus.NOT_FOUND, ErrorCode.CLIENTE_NAO_ENCONTRADO));
    log.debug("[finish] ClienteInfraRepository - buscaClientePorId");
    return cliente;
  }

  @Override
  public Page<Cliente> buscarTodos(Pageable pageable) {
    log.debug("[start] ClienteInfraRepository - buscarTodos");
    Page<Cliente> clientes = clienteSpringDataJpaRepository.buscarTodosClientes(pageable);
    log.debug("[finish] ClienteInfraRepository - buscarTodos");
    return clientes;
  }
}
