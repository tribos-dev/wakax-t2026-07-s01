package br.com.wakax.wakax_ecommerce.fornecedor.application.api.response;

import java.util.UUID;

import br.com.wakax.wakax_ecommerce.fornecedor.domain.Fornecedor;
import lombok.Getter;

@Getter
public class FornecedorResponse {
  private final UUID id;
  private final String documento;
  private final String razaoSocial;
  private final String nomeFantasia;

  public FornecedorResponse(Fornecedor fornecedor) {
    this.id = fornecedor.getId();
    this.documento = fornecedor.getDocumento();
    this.razaoSocial = fornecedor.getRazaoSocial();
    this.nomeFantasia = fornecedor.getNomeFantasia();
  }
}
