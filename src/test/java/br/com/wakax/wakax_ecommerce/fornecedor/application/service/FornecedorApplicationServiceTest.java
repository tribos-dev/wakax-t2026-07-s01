package br.com.wakax.wakax_ecommerce.fornecedor.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import br.com.wakax.wakax_ecommerce.fornecedor.application.api.request.FornecedorAlteracaoRequest;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.request.FornecedorRequest;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.response.FornecedorAlteracaoResponse;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.response.FornecedorListResponse;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.response.FornecedorPageResponse;
import br.com.wakax.wakax_ecommerce.fornecedor.application.api.response.FornecedorResponse;
import br.com.wakax.wakax_ecommerce.fornecedor.application.repository.FornecedorRepository;
import br.com.wakax.wakax_ecommerce.fornecedor.domain.Fornecedor;
import br.com.wakax.wakax_ecommerce.handler.APIException;
import br.com.wakax.wakax_ecommerce.handler.ErrorCode;
import br.com.wakax.wakax_ecommerce.pessoa.domain.Endereco;
import br.com.wakax.wakax_ecommerce.pessoa.domain.StatusPessoa;

@ExtendWith(MockitoExtension.class)
class FornecedorApplicationServiceTest {

  @Mock private FornecedorRepository fornecedorRepository;

  @InjectMocks private FornecedorApplicationService fornecedorApplicationService;

  private FornecedorRequest fornecedorRequest;
  private Fornecedor fornecedor;
  private UUID fornecedorId;

  @BeforeEach
  void setUp() {
    fornecedorId = UUID.randomUUID();

    List<String> emails = Arrays.asList("contato@empresa.com.br", "vendas@empresa.com.br");
    List<String> telefones = Arrays.asList("(11) 99999-9999", "(11) 88888-8888");
    List<Endereco> enderecos =
        Arrays.asList(
            Endereco.builder()
                .logradouro("Rua das Flores")
                .numero("123")
                .complemento("Sala 45")
                .bairro("Centro")
                .cidade("São Paulo")
                .estado("SP")
                .cep("01234-567")
                .principal(true)
                .build());

    fornecedorRequest =
        new FornecedorRequest(
            "Empresa ABC Ltda",
            "12.345.678/0001-90",
            emails,
            telefones,
            enderecos,
            "123456789",
            "Empresa ABC Ltda",
            "ABC");
  }

  private void mockFornecedorRepositorySalvaComId() {
    when(fornecedorRepository.salva(any(Fornecedor.class)))
        .thenAnswer(
            (Answer<Fornecedor>)
                invocation -> {
                  Fornecedor f = invocation.getArgument(0);
                  f.setId(fornecedorId);
                  return f;
                });
  }

  @Test
  void deveCadastrarFornecedorComSucesso() {
    mockFornecedorRepositorySalvaComId();
    FornecedorResponse response =
        fornecedorApplicationService.cadastraFornecedor(fornecedorRequest);
    assertNotNull(response);
    assertEquals(fornecedorId, response.getId());
    assertEquals(fornecedorRequest.getDocumento(), response.getDocumento());
    verify(fornecedorRepository, times(1)).salva(any(Fornecedor.class));
  }

  @Test
  void deveLancarExcecaoQuandoFornecedorDuplicado() {
    when(fornecedorRepository.salva(any(Fornecedor.class)))
        .thenThrow(
            new APIException(
                org.springframework.http.HttpStatus.CONFLICT,
                ErrorCode.FORNECEDOR_DUPLICADO,
                fornecedorRequest.getDocumento()));

    APIException exception =
        assertThrows(
            APIException.class,
            () -> {
              fornecedorApplicationService.cadastraFornecedor(fornecedorRequest);
            });

    assertEquals(ErrorCode.FORNECEDOR_DUPLICADO, exception.getErrorCode());
    assertEquals(fornecedorRequest.getDocumento(), exception.getArgs()[0]);
    verify(fornecedorRepository, times(1)).salva(any(Fornecedor.class));
  }

  @Test
  void deveBuscarFornecedorPorIdComSucesso() {
    Fornecedor fornecedor = new Fornecedor(fornecedorRequest);
    fornecedor.setId(fornecedorId);
    when(fornecedorRepository.buscaFornecedorPorId(fornecedorId)).thenReturn(fornecedor);

    FornecedorListResponse response =
        fornecedorApplicationService.buscaFornecedorPorId(fornecedorId);

    assertNotNull(response);
    assertEquals(fornecedorId, response.getId());
    assertEquals(fornecedorRequest.getNome(), response.getNome());
    assertEquals(fornecedorRequest.getDocumento(), response.getDocumento());
    assertEquals(fornecedorRequest.getInscricaoEstadual(), response.getInscricaoEstadual());
    assertEquals(fornecedorRequest.getRazaoSocial(), response.getRazaoSocial());
    assertEquals(fornecedorRequest.getNomeFantasia(), response.getNomeFantasia());

    verify(fornecedorRepository, times(1)).buscaFornecedorPorId(fornecedorId);
  }

  @Test
  void deveLancarExcecaoQuandoFornecedorNaoEncontrado() {
    when(fornecedorRepository.buscaFornecedorPorId(fornecedorId))
        .thenThrow(
            new APIException(
                org.springframework.http.HttpStatus.NOT_FOUND,
                ErrorCode.FORNECEDOR_NAO_ENCONTRADO,
                fornecedorId));

    APIException exception =
        assertThrows(
            APIException.class,
            () -> {
              fornecedorApplicationService.buscaFornecedorPorId(fornecedorId);
            });

    assertEquals(ErrorCode.FORNECEDOR_NAO_ENCONTRADO, exception.getErrorCode());
    assertEquals(fornecedorId, exception.getArgs()[0]);
    verify(fornecedorRepository, times(1)).buscaFornecedorPorId(fornecedorId);
  }

  @Test
  void deveListarFornecedoresPaginadosComSucesso() {
    Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "razaoSocial"));
    Fornecedor fornecedorAtivo = FornecedorDataHelper.criarFornecedorAtivo();
    Fornecedor fornecedorInativo = FornecedorDataHelper.criarFornecedorInativo();
    Page<Fornecedor> fornecedores = new PageImpl<>(List.of(fornecedorAtivo, fornecedorInativo));
    when(fornecedorRepository.buscaFornecedoresPaginados(null, pageable)).thenReturn(fornecedores);

    FornecedorPageResponse response =
        fornecedorApplicationService.listarFornecedores(null, pageable);

    assertNotNull(response);
    assertEquals(2, response.getTotalElementos());
    assertEquals(0, response.getPaginaAtual());
    assertEquals(1, response.getTotalPaginas());
    assertEquals(2, response.getFornecedoresResumidos().size());

    verify(fornecedorRepository, times(1)).buscaFornecedoresPaginados(null, pageable);
  }

  @Test
  void deveRetornarListaVaziaDeFornecedoresPaginados() {
    Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "razaoSocial"));
    Page<Fornecedor> fornecedores = new PageImpl<>(List.of(), pageable, 0);
    when(fornecedorRepository.buscaFornecedoresPaginados(null, pageable)).thenReturn(fornecedores);

    FornecedorPageResponse response =
        fornecedorApplicationService.listarFornecedores(null, pageable);

    assertNotNull(response);
    assertEquals(0, response.getTotalElementos());
    assertEquals(0, response.getPaginaAtual());
    assertEquals(0, response.getTotalPaginas());
    assertTrue(response.getFornecedoresResumidos().isEmpty());

    verify(fornecedorRepository, times(1)).buscaFornecedoresPaginados(null, pageable);
  }

  @Test
  void deveRetornarListaDeFornecedoresAtivos() {
    Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "razaoSocial"));
    Fornecedor fornecedorAtivo = FornecedorDataHelper.criarFornecedorAtivo();
    Page<Fornecedor> fornecedores = new PageImpl<>(List.of(fornecedorAtivo));
    when(fornecedorRepository.buscaFornecedoresPaginados(StatusPessoa.ATIVO, pageable))
        .thenReturn(fornecedores);

    FornecedorPageResponse response =
        fornecedorApplicationService.listarFornecedores(StatusPessoa.ATIVO, pageable);

    assertNotNull(response);
    assertEquals(1, response.getTotalElementos());
    assertEquals(0, response.getPaginaAtual());
    assertEquals(1, response.getTotalPaginas());
    assertEquals(1, response.getFornecedoresResumidos().size());

    verify(fornecedorRepository, times(1)).buscaFornecedoresPaginados(StatusPessoa.ATIVO, pageable);
  }

  @Test
  void deveInativarFornecedorComSucesso() {
    UUID idFornecedor = UUID.randomUUID();
    doNothing().when(fornecedorRepository).inativar(idFornecedor);

    fornecedorApplicationService.inativarFornecedor(idFornecedor);

    verify(fornecedorRepository, times(1)).inativar(idFornecedor);
  }

  @Test
  void deveLancarExcecaoQuandoFornecedorNaoEncontradoAoInativar() {
    UUID idFornecedor = UUID.randomUUID();
    doThrow(
            new APIException(
                HttpStatus.NOT_FOUND, ErrorCode.FORNECEDOR_NAO_ENCONTRADO, idFornecedor))
        .when(fornecedorRepository)
        .inativar(idFornecedor);

    APIException exception =
        assertThrows(
            APIException.class,
            () -> fornecedorApplicationService.inativarFornecedor(idFornecedor));

    assertEquals(ErrorCode.FORNECEDOR_NAO_ENCONTRADO, exception.getErrorCode());
    assertEquals(idFornecedor, exception.getArgs()[0]);
    verify(fornecedorRepository, times(1)).inativar(idFornecedor);
  }

  @Test
  void deveLancarExcecaoQuandoFornecedorComRestricaoDominio() {
    UUID idFornecedor = UUID.randomUUID();
    doThrow(new APIException(HttpStatus.CONFLICT, ErrorCode.FORNECEDOR_COM_RESTRICAO, idFornecedor))
        .when(fornecedorRepository)
        .inativar(idFornecedor);

    APIException exception =
        assertThrows(
            APIException.class,
            () -> fornecedorApplicationService.inativarFornecedor(idFornecedor));

    assertEquals(ErrorCode.FORNECEDOR_COM_RESTRICAO, exception.getErrorCode());
    assertEquals(idFornecedor, exception.getArgs()[0]);
    verify(fornecedorRepository, times(1)).inativar(idFornecedor);
  }

  @Test
  void deveAlterarRazaoSocialENomeFantasiaComSucesso() {
    Fornecedor fornecedorExistente = FornecedorDataHelper.criarFornecedorAtivo();
    UUID idFornecedor = fornecedorExistente.getId();
    LocalDateTime dataEdicaoAnterior = fornecedorExistente.getDataEdicao();

    FornecedorAlteracaoRequest request =
        FornecedorDataHelper.criarRequestAlteracaoRazaoSocialENomeFantasia();
    when(fornecedorRepository.buscaFornecedorPorId(idFornecedor)).thenReturn(fornecedorExistente);
    when(fornecedorRepository.atualiza(any(Fornecedor.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    FornecedorAlteracaoResponse response =
        fornecedorApplicationService.alterarDadosFornecedor(idFornecedor, request);

    assertNotNull(response);
    assertEquals("Nova Razao Social", response.getRazaoSocial());
    assertEquals("Nova Fantasia", response.getNomeFantasia());
    assertEquals(fornecedorExistente.getDocumento(), response.getDocumento());
    assertNotNull(response.getDataEdicao());
    assertFalse(response.getDataEdicao().isBefore(dataEdicaoAnterior));
    verify(fornecedorRepository).buscaFornecedorPorId(idFornecedor);
    verify(fornecedorRepository).atualiza(fornecedorExistente);
  }

  @Test
  void deveAlterarEmailsETelefonesComSucesso() {
    Fornecedor fornecedorExistente = FornecedorDataHelper.criarFornecedorAtivo();
    UUID idFornecedor = fornecedorExistente.getId();
    List<String> novosEmails = List.of("novo@empresa.com.br", "vendas@empresa.com.br");
    List<String> novosTelefones = List.of("(11) 97777-7777");
    FornecedorAlteracaoRequest request =
        FornecedorDataHelper.criarRequestAlteracaoEmailsETelefones(novosEmails, novosTelefones);

    when(fornecedorRepository.buscaFornecedorPorId(idFornecedor)).thenReturn(fornecedorExistente);
    when(fornecedorRepository.atualiza(any(Fornecedor.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    FornecedorAlteracaoResponse response =
        fornecedorApplicationService.alterarDadosFornecedor(idFornecedor, request);

    assertNotNull(response);
    assertEquals(novosEmails, response.getEmails());
    assertEquals(novosTelefones, response.getTelefones());
    verify(fornecedorRepository).buscaFornecedorPorId(idFornecedor);
    verify(fornecedorRepository).atualiza(fornecedorExistente);
  }

  @Test
  void deveLancarExcecaoQuandoFornecedorNaoExisteNaAlteracao() {
    UUID idInexistente = UUID.randomUUID();
    FornecedorAlteracaoRequest request =
        FornecedorDataHelper.criarRequestAlteracaoRazaoSocialENomeFantasia();

    when(fornecedorRepository.buscaFornecedorPorId(idInexistente))
        .thenThrow(
            new APIException(
                HttpStatus.NOT_FOUND, ErrorCode.FORNECEDOR_NAO_ENCONTRADO, idInexistente));
    APIException exception =
        assertThrows(
            APIException.class,
            () -> fornecedorApplicationService.alterarDadosFornecedor(idInexistente, request));

    assertEquals(ErrorCode.FORNECEDOR_NAO_ENCONTRADO, exception.getErrorCode());
    assertEquals(idInexistente, exception.getArgs()[0]);
    verify(fornecedorRepository).buscaFornecedorPorId(idInexistente);
    verify(fornecedorRepository, never()).atualiza(any(Fornecedor.class));
  }

  @Test
  void deveIgnorarCamposNulosOuEmBrancoNaAlteracao() {
    Fornecedor fornecedorExistente = FornecedorDataHelper.criarFornecedorAtivo();
    UUID idFornecedor = fornecedorExistente.getId();
    UUID enderecoId = UUID.randomUUID();
    Endereco enderecoAtual =
        Endereco.builder()
            .id(enderecoId)
            .logradouro("Rua Antiga")
            .numero("10")
            .complemento("Sala 1")
            .bairro("Centro")
            .cidade("São Paulo")
            .estado("SP")
            .cep("01000-000")
            .principal(true)
            .build();
    fornecedorExistente.getPessoa().getEnderecos().add(enderecoAtual);

    Endereco enderecoRecebido =
        Endereco.builder()
            .id(enderecoId)
            .logradouro("   ")
            .numero("20")
            .complemento("")
            .bairro(null)
            .cidade("  ")
            .estado(null)
            .cep("")
            .principal(true)
            .build();
    FornecedorAlteracaoRequest request =
        FornecedorDataHelper.criarRequestAlteracao(
            "   ",
            List.of("", " "),
            List.of(" "),
            List.of(enderecoRecebido),
            "",
            "Nova Razao Social",
            null);

    when(fornecedorRepository.buscaFornecedorPorId(idFornecedor)).thenReturn(fornecedorExistente);
    when(fornecedorRepository.atualiza(any(Fornecedor.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    FornecedorAlteracaoResponse response =
        fornecedorApplicationService.alterarDadosFornecedor(idFornecedor, request);

    assertEquals("Nova Razao Social", response.getRazaoSocial());
    assertEquals("Fantasia Empresa Ativa LTDA", response.getNomeFantasia());
    assertEquals("123.456.789", response.getInscricaoEstadual());
    assertEquals("Nome Empresa Ativa LTDA", response.getNome());
    assertEquals(List.of("contato@empresa.com"), response.getEmails());
    assertEquals(List.of("(11) 99999-9999"), response.getTelefones());
    assertEquals("Rua Antiga", response.getEnderecos().get(0).getLogradouro());
    assertEquals("20", response.getEnderecos().get(0).getNumero());
    assertEquals("Sala 1", response.getEnderecos().get(0).getComplemento());
    assertEquals("Centro", response.getEnderecos().get(0).getBairro());
    assertEquals("São Paulo", response.getEnderecos().get(0).getCidade());
    assertEquals("SP", response.getEnderecos().get(0).getEstado());
    assertEquals("01000-000", response.getEnderecos().get(0).getCep());
    assertTrue(response.getEnderecos().get(0).isPrincipal());
  }

  @Test
  void deveAlterarPrincipalQuandoInformadoExplicitamente() {
    Fornecedor fornecedorExistente = FornecedorDataHelper.criarFornecedorAtivo();
    UUID idFornecedor = fornecedorExistente.getId();
    UUID enderecoId = UUID.randomUUID();
    Endereco enderecoAtual =
        Endereco.builder()
            .id(enderecoId)
            .logradouro("Rua Antiga")
            .numero("10")
            .principal(true)
            .build();
    fornecedorExistente.getPessoa().getEnderecos().add(enderecoAtual);

    Endereco enderecoRecebido = Endereco.builder().id(enderecoId).principal(false).build();
    FornecedorAlteracaoRequest request =
        FornecedorDataHelper.criarRequestAlteracao(
            null, null, null, List.of(enderecoRecebido), null, null, null);

    when(fornecedorRepository.buscaFornecedorPorId(idFornecedor)).thenReturn(fornecedorExistente);
    when(fornecedorRepository.atualiza(any(Fornecedor.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    FornecedorAlteracaoResponse response =
        fornecedorApplicationService.alterarDadosFornecedor(idFornecedor, request);

    assertFalse(response.getEnderecos().get(0).isPrincipal());
    assertEquals("Rua Antiga", response.getEnderecos().get(0).getLogradouro());
    assertEquals("10", response.getEnderecos().get(0).getNumero());
  }
}
