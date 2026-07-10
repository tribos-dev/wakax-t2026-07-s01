## Sobre o Projeto

O **WakaX** é um ERP (Enterprise Resource Planning) modular para gestão operacional, financeira e comercial.
Ele centraliza dados e orquestra processos ponta a ponta, evitando retrabalho e garantindo rastreabilidade.

### Objetivo Principal
O WakaX foi projetado para organizar e controlar dados operacionais e financeiros em um único sistema confiável.
Mais do que registrar informações, ele coordena fluxos completos:
- Cadastro e gestão de pessoas (clientes, fornecedores, parceiros)
- Controle de produtos e serviços
- Gestão de estoque e custos
- Ciclo completo de pedidos e faturamento
- Controle financeiro e pagamentos
- Base para relatórios e tomada de decisão

### Visão Geral de Funcionamento
O WakaX funciona como um ecossistema integrado de módulos, com base de dados e regras compartilhadas.
Nada acontece isoladamente: toda ação deixa rastro, histórico e impacto controlado.

Fluxo principal:
Pessoa -> Produto/Servico -> Operacao (Pedido) -> Financeiro -> Estoque -> Inteligencia

### Modulos principais (visao tecnica)
- Pessoa / Cliente / Fornecedor
- Produto
- Estoque
- Carrinho
- Pedido
- Pagamento
- Autenticacao e Usuarios

### Requisitos

- [Git](https://git-scm.com/downloads)
- [Java 17](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html )
- [Lombok](https://projectlombok.org/download)
- [Maven](https://maven.apache.org/download.cgi)

### Instalação

1. No seu workspace, abra o terminal e clone o repositório do projeto:
    ```bash
    git clone <URL_DO_SEU_REPOSITORIO>
    ```

2. Navegue até o diretório do projeto:
    ```bash
    cd ./wakax-ecommerce
    ```

3. Compile o projeto com o Maven:
    ```bash
    ./mvnw clean install
    ```
    Ou, se estiver no Windows:
    ```bash
    mvnw.cmd clean install
    ```

4. Abra o projeto na IDE de sua preferência (sugestão: IntelliJ IDEA ou VS Code).

5. Execute a aplicação:
    ```bash
    ./mvnw spring-boot:run
    ```
    Ou, no Windows:
    ```bash
    mvnw.cmd spring-boot:run
    ```

### Banco de Dados (dev)
O ambiente de desenvolvimento usa PostgreSQL com Docker.
Suba o container com:
```bash
docker compose -f docker-compose.dev.yml up -d
```

Por padrao, a aplicacao usa:
- Host: localhost
- Porta: 5432
- Banco: wakaxdb
- Usuario: postgres
- Senha: postgres

### Swagger / OpenAPI
Com a aplicacao rodando:
- Swagger UI: http://localhost:8080/wakax-ecommerce/api/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/wakax-ecommerce/api/v3/api-docs

### Documentação Wiki
- Wiki: https://github.com/tribos-dev/wakax-sprint-conquista/wiki
