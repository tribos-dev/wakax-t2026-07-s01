package br.com.wakax.wakax_ecommerce.produto.api.request;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoUpdateRequest {
    @Size(max = 150)
    private String descricao;

    private BigDecimal pesoLiquido;

    private BigDecimal pesoBruto;

    @Size(max = 500)
    private String descricaoComplementar;

    @Size(max = 50)
    private String grupo;

    @Size(max = 10)
    private String unidade;

    private Integer estoqueMinimo;
    private Integer estoqueMaximo;

    @Valid
    private List<PrecoRequest> precos;
}
