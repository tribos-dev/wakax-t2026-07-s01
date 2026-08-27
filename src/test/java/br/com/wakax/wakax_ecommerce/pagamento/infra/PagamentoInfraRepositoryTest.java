package br.com.wakax.wakax_ecommerce.pagamento.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.LockModeType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.Lock;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pagamento.application.service.PagamentoDataHelper;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;

@ExtendWith(MockitoExtension.class)
class PagamentoInfraRepositoryTest {

  @Mock private PagamentoJPARepository pagamentoJPARepository;

  @InjectMocks private PagamentoInfraRepository pagamentoInfraRepository;

  @Test
  void deveBuscarPagamentoParaAtualizacaoComBloqueio() {
    UUID idPagamento = UUID.randomUUID();
    Pagamento pagamento =
        PagamentoDataHelper.criaPagamentoValido(PagamentoDataHelper.criaPedidoValido());
    when(pagamentoJPARepository.findByIdComPedidoParaAtualizacao(idPagamento))
        .thenReturn(Optional.of(pagamento));

    Pagamento resultado = pagamentoInfraRepository.buscaPagamentoPorIdParaAtualizacao(idPagamento);

    assertEquals(pagamento, resultado);
    verify(pagamentoJPARepository).findByIdComPedidoParaAtualizacao(idPagamento);
  }

  @Test
  void deveLancarErroQuandoPagamentoParaAtualizacaoNaoExiste() {
    UUID idPagamento = UUID.randomUUID();
    when(pagamentoJPARepository.findByIdComPedidoParaAtualizacao(idPagamento))
        .thenReturn(Optional.empty());

    APIException exception =
        assertThrows(
            APIException.class,
            () -> pagamentoInfraRepository.buscaPagamentoPorIdParaAtualizacao(idPagamento));

    assertEquals(ErrorCode.PAGAMENTO_NAO_ENCONTRADO, exception.getErrorCode());
  }

  @Test
  void consultaDeAtualizacaoDeveUsarBloqueioPessimista() throws NoSuchMethodException {
    Method metodo =
        PagamentoJPARepository.class.getMethod("findByIdComPedidoParaAtualizacao", UUID.class);

    Lock lock = metodo.getAnnotation(Lock.class);

    assertEquals(LockModeType.PESSIMISTIC_WRITE, lock.value());
  }
}
