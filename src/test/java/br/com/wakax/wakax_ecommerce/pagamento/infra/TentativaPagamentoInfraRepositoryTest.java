package br.com.wakax.wakax_ecommerce.pagamento.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.wakax.wakax_ecommerce.pagamento.application.service.PagamentoDataHelper;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.TentativaPagamento;

@ExtendWith(MockitoExtension.class)
class TentativaPagamentoInfraRepositoryTest {

  @Mock private TentativaPagamentoJPARepository tentativaPagamentoJPARepository;

  @InjectMocks private TentativaPagamentoInfraRepository tentativaPagamentoInfraRepository;

  @Test
  void deveSalvarHistoricoDaTentativa() {
    Pagamento pagamento =
        PagamentoDataHelper.criaPagamentoValido(PagamentoDataHelper.criaPedidoValido());
    TentativaPagamento tentativa = TentativaPagamento.nova(pagamento, 1);
    when(tentativaPagamentoJPARepository.save(tentativa)).thenReturn(tentativa);

    TentativaPagamento resultado = tentativaPagamentoInfraRepository.salva(tentativa);

    assertEquals(tentativa, resultado);
    verify(tentativaPagamentoJPARepository).save(tentativa);
  }

  @Test
  void deveContarTentativasDoPagamento() {
    UUID idPagamento = UUID.randomUUID();
    when(tentativaPagamentoJPARepository.countByPagamentoId(idPagamento)).thenReturn(2L);

    long totalTentativas =
        tentativaPagamentoInfraRepository.contaTentativasDoPagamento(idPagamento);

    assertEquals(2L, totalTentativas);
    verify(tentativaPagamentoJPARepository).countByPagamentoId(idPagamento);
  }
}
