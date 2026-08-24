package br.com.wakax.wakax_ecommerce.produto.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import br.com.wakax.wakax_ecommerce.auth.credencial.application.service.CredencialService;
import br.com.wakax.wakax_ecommerce.auth.security.SecurityConfiguration;
import br.com.wakax.wakax_ecommerce.auth.security.service.AutenticacaoSecurityService;
import br.com.wakax.wakax_ecommerce.auth.security.service.TokenService;
import br.com.wakax.wakax_ecommerce.handler.MessageUtil;
import br.com.wakax.wakax_ecommerce.produto.api.response.ProdutoAtivoPaginadoResponse;
import br.com.wakax.wakax_ecommerce.produto.application.service.ProdutoService;
import br.com.wakax.wakax_ecommerce.produto.domain.ProdutoDisponivel;

@WebMvcTest(ProdutoController.class)
@Import(SecurityConfiguration.class)
class ProdutoAcessoPublicoTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ProdutoService produtoService;
  @MockBean private TokenService tokenService;
  @MockBean private CredencialService credencialService;
  @MockBean private AutenticacaoSecurityService autenticacaoSecurityService;
  @MockBean private MessageUtil messageUtil;

  @Test
  void devePermitirListagemDeProdutosAtivosSemToken() throws Exception {
    ProdutoAtivoPaginadoResponse response =
        new ProdutoAtivoPaginadoResponse(
            new PageImpl<ProdutoDisponivel>(List.of(), PageRequest.of(0, 10), 0));
    when(produtoService.listarProdutosAtivos(0, 10)).thenReturn(response);

    mockMvc
        .perform(get("/produto/ativos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.produtos").isArray())
        .andExpect(jsonPath("$.total").value(0))
        .andExpect(jsonPath("$.pagina").value(0))
        .andExpect(jsonPath("$.totalPaginas").value(0));
  }
}
