package br.com.wakax.wakax_ecommerce.pagamento.application.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pagamento.application.gateway.SolicitacaoReprocessamentoPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.application.repository.PagamentoRepository;
import br.com.wakax.wakax_ecommerce.pagamento.application.repository.TentativaPagamentoRepository;
import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.TentativaPagamento;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReprocessamentoPagamentoTransacionalService {

  private final PagamentoRepository pagamentoRepository;
  private final TentativaPagamentoRepository tentativaPagamentoRepository;

  @Transactional
  public SolicitacaoReprocessamentoPagamento preparaReprocessamento(UUID idPagamento) {
    Pagamento pagamento = pagamentoRepository.buscaPagamentoPorIdParaAtualizacao(idPagamento);
    validaReprocessamento(pagamento);

    pagamento.iniciarReprocessamento();
    TentativaPagamento tentativa = TentativaPagamento.pendente(pagamento);

    pagamentoRepository.salva(pagamento);
    tentativaPagamentoRepository.salva(tentativa);

    return SolicitacaoReprocessamentoPagamento.builder()
        .idPagamento(pagamento.getId())
        .idPedido(pagamento.getPedido().getId())
        .valor(pagamento.getValor())
        .formaPagamento(pagamento.getPedido().getFormaPagamento())
        .numeroTentativa(pagamento.getNumeroTentativas())
        .chaveIdempotencia(tentativa.getChaveIdempotencia())
        .build();
  }

  private void validaReprocessamento(Pagamento pagamento) {
    if (pagamento.getStatusPagamento() == StatusPagamento.PAGO) {
      throw new APIException(HttpStatus.CONFLICT, ErrorCode.PAGAMENTO_JA_PROCESSADO_COM_SUCESSO);
    }

    if (pagamento.getStatusPagamento() != StatusPagamento.FALHOU) {
      throw new APIException(
          HttpStatus.CONFLICT,
          ErrorCode.PAGAMENTO_NAO_PODE_SER_REPROCESSADO,
          pagamento.getStatusPagamento());
    }

    if (pagamento.atingiuLimiteTentativas()) {
      throw new APIException(
          HttpStatus.CONFLICT, ErrorCode.LIMITE_TENTATIVAS_PAGAMENTO_EXCEDIDO, pagamento.getId());
    }
  }

  @Transactional
  public Pagamento registraEnvioAceito(UUID idPagamento, String chaveIdempotencia) {
    Pagamento pagamento = pagamentoRepository.buscaPagamentoPorIdParaAtualizacao(idPagamento);
    TentativaPagamento tentativa =
        tentativaPagamentoRepository.buscaPorChaveIdempotencia(chaveIdempotencia);

    tentativa.marcarComoEnviada();
    tentativaPagamentoRepository.salva(tentativa);
    return pagamento;
  }

  @Transactional
  public void registraFalhaEnvio(UUID idPagamento, String chaveIdempotencia, String detalheFalha) {
    Pagamento pagamento = pagamentoRepository.buscaPagamentoPorIdParaAtualizacao(idPagamento);
    TentativaPagamento tentativa =
        tentativaPagamentoRepository.buscaPorChaveIdempotencia(chaveIdempotencia);

    pagamento.registrarFalha();
    tentativa.marcarComoFalha(detalheFalha);

    pagamentoRepository.salva(pagamento);
    tentativaPagamentoRepository.salva(tentativa);
  }
}
