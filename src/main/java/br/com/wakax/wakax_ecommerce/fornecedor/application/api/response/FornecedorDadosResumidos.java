package br.com.wakax.wakax_ecommerce.fornecedor.application.api.response;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.fornecedor.domain.Fornecedor;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;
import lombok.Getter;

@Getter
public class FornecedorDadosResumidos {
  private UUID idFornecedor;
  private String nome;
  private String razaoSocial;
  private String nomeFantasia;
  private StatusPessoa statusPessoa;
  private LocalDateTime dataHoraCadastro;
  private LocalDateTime dataHoraUltimaAtualizacao;

  public FornecedorDadosResumidos(Fornecedor fornecedor) {
    this.idFornecedor = fornecedor.getId();
    this.nome = fornecedor.getPessoa().getNome();
    this.razaoSocial = fornecedor.getRazaoSocial();
    this.nomeFantasia = fornecedor.getNomeFantasia();
    this.statusPessoa = fornecedor.getPessoa().getStatus();
    this.dataHoraCadastro = fornecedor.getDataCriacao();
    this.dataHoraUltimaAtualizacao = fornecedor.getDataEdicao();
  }
}
