package br.com.wakax.wakax_ecommerce.fornecedor.application.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.fornecedor.domain.Fornecedor;
import lombok.Getter;

@Getter
public class FornecedorListResponse {
  private final UUID id;
  private final String nome;
  private final String documento;
  private final String inscricaoEstadual;
  private final String razaoSocial;
  private final String nomeFantasia;
  private final LocalDateTime dataCriacao;
  private final LocalDateTime dataEdicao;

  public FornecedorListResponse(Fornecedor fornecedor) {
    this.id = fornecedor.getId();
    this.nome = fornecedor.getPessoa().getNome();
    this.documento = fornecedor.getDocumento();
    this.inscricaoEstadual = fornecedor.getInscricaoEstadual();
    this.razaoSocial = fornecedor.getRazaoSocial();
    this.nomeFantasia = fornecedor.getNomeFantasia();
    this.dataCriacao = fornecedor.getDataCriacao();
    this.dataEdicao = fornecedor.getDataEdicao();
  }
}
