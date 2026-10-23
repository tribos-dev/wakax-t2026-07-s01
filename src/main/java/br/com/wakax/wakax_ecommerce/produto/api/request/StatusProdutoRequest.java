package br.com.wakax.wakax_ecommerce.produto.api.request;

import br.com.wakax.wakax_ecommerce.produto.domain.StatusProduto;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusProdutoRequest {
    @NotNull private StatusProduto status;
    @NotBlank @Size(max = 255) private String motivo;

}
