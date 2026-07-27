package br.com.wakax.wakax_ecommerce.handler;

public enum ErrorCode {
  PEDIDO_NAO_ENCONTRADO("pedido.nao.encontrado"),
  RASTREAMENTO_NAO_ENCONTRADO("rastreamento.nao.encontrado"),
  RASTREAMENTO_JA_EXISTE("rastreamento.ja.existe"),
  ACESSO_NEGADO("acesso.negado"),
  PRODUTO_NAO_ENCONTRADO("produto.nao.encontrado"),
  PRODUTO_DUPLICADO("produto.duplicado"),
  FORNECEDOR_NAO_ENCONTRADO("fornecedor.nao.encontrado"),
  FORNECEDOR_DUPLICADO("fornecedor.duplicado"),
  CLIENTE_NAO_ENCONTRADO("cliente.nao.encontrado"),
  CARRINHO_NAO_ENCONTRADO("carrinho.nao.encontrado"),
  CARRINHO_ATIVO_NAO_EXISTE("carrinho.ativo.nao_existe"),
  CARRINHO_NAO_ATIVO("carrinho.nao.ativo"),
  ITEM_CARRINHO_NAO_ENCONTRADO("item-carrinho.nao.encontrado"),
  ITEM_CARRINHO_QUANTIDADE_MENOR_QUE_UM("item-carrinho.quantidade.menor.que.um"),
  PAGAMENTO_NAO_ENCONTRADO("pagamento.nao.encontrado"),
  PEDIDO_JA_POSSUI_PAGAMENTO("pedido.ja.possui.pagamento"),
  FORMA_PAGAMENTO_NAO_SUPORTADA("forma.pagamento.nao.suportada"),
  ESTOQUE_NAO_ENCONTRADO("estoque.nao.encontrado"),
  ESTOQUE_JA_EXISTE("estoque.ja.existe"),
  ESTOQUE_FILTROS_MUTUAMENTE_EXCLUSIVOS("estoque.filtros.mutuamente.exclusivos"),
  QUANTIDADE_INSUFICIENTE_ESTOQUE("quantidade.insuficiente.estoque"),
  QUANTIDADE_INVALIDA("quantidade.invalida"),
  CUSTO_INVALIDO("custo.invalido"),
  ESTOQUE_INVALIDO("estoque.invalido");

  private final String code;

  ErrorCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
