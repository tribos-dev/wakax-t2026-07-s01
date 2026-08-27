package br.com.wakax.wakax_ecommerce.pagamento.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.wakax.wakax_ecommerce.pagamento.application.service.PagamentoDataHelper;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.TentativaPagamento;

@ExtendWith(MockitoExtension.class)
class TentativaPagamentoInfraRepositoryTest {

  @Mock private TentativaPagamentoJPARepository tentativaPagamentoJPARepository;

  @InjectMocks private TentativaPagamentoInfraRepository tentativaPagamentoInfraRepository;

  @Test
  void deveSalvarHistoricoDaTentativa() {
    TentativaPagamento tentativa = criaTentativa();
    when(tentativaPagamentoJPARepository.save(tentativa)).thenReturn(tentativa);

    TentativaPagamento resultado = tentativaPagamentoInfraRepository.salva(tentativa);

    assertEquals(tentativa, resultado);
    verify(tentativaPagamentoJPARepository).save(tentativa);
  }

  @Test
  void deveBuscarHistoricoPelaChaveIdempotente() {
    TentativaPagamento tentativa = criaTentativa();
    when(tentativaPagamentoJPARepository.findByChaveIdempotencia(tentativa.getChaveIdempotencia()))
        .thenReturn(Optional.of(tentativa));

    TentativaPagamento resultado =
        tentativaPagamentoInfraRepository.buscaPorChaveIdempotencia(
            tentativa.getChaveIdempotencia());

    assertEquals(tentativa, resultado);
  }

  @Test
  void deveFalharQuandoHistoricoNaoForEncontrado() {
    String chaveIdempotencia = "pagamento:inexistente:tentativa:2";
    when(tentativaPagamentoJPARepository.findByChaveIdempotencia(chaveIdempotencia))
        .thenReturn(Optional.empty());

    assertThrows(
        IllegalStateException.class,
        () -> tentativaPagamentoInfraRepository.buscaPorChaveIdempotencia(chaveIdempotencia));
  }

  private TentativaPagamento criaTentativa() {
    Pagamento pagamento =
        PagamentoDataHelper.criaPagamentoValido(PagamentoDataHelper.criaPedidoValido());
    pagamento.setStatusPagamento(StatusPagamento.FALHOU);
    pagamento.iniciarReprocessamento();
    return TentativaPagamento.pendente(pagamento);
  }
}
