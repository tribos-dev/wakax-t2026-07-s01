package br.com.wakax.wakax_ecommerce.produto.api.request;

import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class StatusProdutoRequest {
    @NotNull private StatusProduto status;
    @NotBlank private String motivo;

}
