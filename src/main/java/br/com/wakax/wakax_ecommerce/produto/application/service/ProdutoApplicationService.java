package br.com.wakax.wakax_ecommerce.produto.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.wakax.wakax_ecommerce.auth.usuario.api.repository.UsuarioRepository;
import br.com.wakax.wakax_ecommerce.auth.usuario.domain.Usuario;
import br.com.wakax.wakax_ecommerce.produto.api.response.*;
import br.com.wakax.wakax_ecommerce.produto.application.repository.HistoricoProdutoRepository;
import br.com.wakax.wakax_ecommerce.produto.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wakax.wakax_ecommerce.auth.credencial.domain.Credencial;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.produto.api.request.PrecoUpdateRequest;
import br.com.wakax.wakax_ecommerce.produto.api.request.ProdutoRequest;
import br.com.wakax.wakax_ecommerce.produto.application.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProdutoApplicationService implements ProdutoService {
  private final ProdutoRepository produtoRepository;
  private final HistoricoProdutoRepository historicoProdutoRepository;
  private  final UsuarioRepository usuarioRepository;
  @Override
  public ProdutoResponse cadastraProduto(ProdutoRequest novoProduto) {
    log.debug("[start] ProdutoApplicationService - cadastraProduto");
    Produto produto = new Produto(novoProduto);
    produtoRepository.salva(produto);
    log.debug("[finish] ProdutoApplicationService - cadastraProduto");
    return new ProdutoResponse(produto);
  }

  @Override
  public ProdutoListResponse buscaProdutoPorId(UUID idProduto) {
    log.debug("[start] ProdutoApplicationService - buscaProdutoPorId");
    Produto produto = produtoRepository.buscaProdutoPorId(idProduto);
    log.debug("[finish] ProdutoApplicationService - buscaProdutoPorId");
    return new ProdutoListResponse(produto);
  }

  @Transactional(readOnly = true)
  @Override
  public ProdutoPaginadoResponse listaProduto(int pagina, int tamanho) {
    log.debug("[start] ProdutoApplicationService - listaProduto");
    Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by("descricao"));
    Page<Produto> produtos = produtoRepository.listaTodos(pageable);
    log.debug("[finish] ProdutoApplicationService - listaProduto");
    return new ProdutoPaginadoResponse(produtos);
  }

  @Transactional(readOnly = true)
  @Override
  public ProdutoAtivoPaginadoResponse listarProdutosAtivos(int pagina, int tamanho) {
    log.debug("[start] ProdutoApplicationService - listarProdutosAtivos");
    Pageable pageable = PageRequest.of(pagina, tamanho);
    Page<ProdutoDisponivel> produtosDisponiveis =
        produtoRepository.listaProdutosAtivosComEstoque(pageable);
    log.debug("[finish] ProdutoApplicationService - listarProdutosAtivos");
    return new ProdutoAtivoPaginadoResponse(produtosDisponiveis);
  }

  @Transactional
  @Override
  public PrecoResponse atualizaPreco(UUID idProduto, PrecoUpdateRequest precoUpdateRequest) {
    log.debug("[start] ProdutoApplicationService - atualizaPreco");
    Produto produto = produtoRepository.buscaProdutoPorId(idProduto);

    Preco preco = buscaPrecoPorTipo(produto.getPrecos(), precoUpdateRequest.getTipo(), idProduto);

    Credencial credencial = buscaUsuarioLogado();

    preco.atualizaValor(
        precoUpdateRequest.getValor(), precoUpdateRequest.getMotivo(), credencial.getUsername());
    produtoRepository.salva(produto);

    log.debug("[finish] ProdutoApplicationService - atualizaPreco");
    return new PrecoResponse(preco);
  }

  @Override
  public ProdutoAtualizaResponse atualizarProduto(UUID idProduto, ProdutoRequest produtoRequest) {
    log.debug("[start] ProdutoApplicationService - atualizaProduto");
    Produto produto = produtoRepository.buscaProdutoPorId(idProduto);
    produto.atualiza(produtoRequest);
    produtoRepository.salva(produto);
    Credencial credencial = buscaUsuarioLogado();
    Usuario usuario = credencial.getUser();
    HistoricoProduto historico = HistoricoProduto.builder()
            .produto(produto)
            .dataRegistro(LocalDateTime.now())
            .usuario(usuario)
            .build();

    historicoProdutoRepository.salva(historico);
    log.debug("[finish] ProdutoApplicationService - atualizaProduto");
    return new ProdutoAtualizaResponse(produto);
  }

  private Preco buscaPrecoPorTipo(List<Preco> precos, TipoPreco tipo, UUID idProduto) {
    return precos.stream()
        .filter(preco -> preco.getTipo() == tipo)
        .findFirst()
        .orElseThrow(
            () ->
                new APIException(HttpStatus.NOT_FOUND, ErrorCode.PRECO_NAO_ENCONTRADO, idProduto));
  }

  private Credencial buscaUsuarioLogado() {
    return (Credencial) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
