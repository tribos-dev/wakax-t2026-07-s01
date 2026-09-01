package br.com.wakax.wakax_ecommerce.pagamento.application.api.response;

import br.com.wakax.wakax_ecommerce.pagamento.domain.Pagamento;
import br.com.wakax.wakax_ecommerce.pagamento.domain.StatusPagamento;
import br.com.wakax.wakax_ecommerce.pedido.domain.FormaPagamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class PagamentoConfirmadoResponse {
    private final UUID idPagamento;
    private final UUID pedidoId;
    private final StatusPagamento statusPagamento;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime dataConfirmacaoPagamento;
    private final BigDecimal valor;
    private final FormaPagamento formaPagamento;

    public PagamentoConfirmadoResponse(Pagamento pagamento) {
        this.idPagamento = pagamento.getId();
        this.pedidoId = pagamento.getPedido().getId();
        this.statusPagamento = pagamento.getStatusPagamento();
        this.dataConfirmacaoPagamento = pagamento.getDataConfirmacao();
        this.valor = pagamento.getValor();
        this.formaPagamento = pagamento.getPedido().getFormaPagamento();
    }

}
