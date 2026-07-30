package br.com.wakax.wakax_ecommerce.estoque.api.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueListagemResponse {
  private List<EstoqueResponse> estoques;
  private BigDecimal valorTotalInventario;
  private int pagina;
  private int tamanho;
  private long totalItens;
}
