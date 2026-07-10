# ATENÇÃO!
# Este arquivo é essencial para o deploy da aplicação,
# qualquer alteração feita aqui irá impactar diretamente no funcionamento do CI/CD e da aplicação em produção.

# syntax=docker/dockerfile:1             # habilita recursos modernos

###########################
# 1) STAGE : BUILD
###########################
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn -B dependency:go-offline          # baixa dependências

# Copia o restante do código e gera o artefato
COPY src ./src
# Desabilita Parameter Store durante os testes para evitar problemas em PRs
# Quando AWS_PARAMETER_STORE_IMPORT está vazio, o Spring não tenta carregar do Parameter Store
ENV AWS_PARAMETER_STORE_IMPORT=""
RUN mvn -B clean package                  # gera target/*.jar


###########################
# 2) STAGE : RUNTIME
###########################
FROM eclipse-temurin:17-jre-jammy AS runtime
ENV APP_HOME=/app
WORKDIR $APP_HOME

# Copia o jar gerado na stage anterior
COPY --from=build /workspace/target/*.jar app.jar

# (Boa prática) cria usuário não-root
RUN useradd --system --uid 10001 appuser
USER appuser

HEALTHCHECK --start-period=60s --interval=30s --timeout=5s --retries=5 \
  CMD curl -fs $HEALTH_URL || exit 1

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS} -jar ${APP_HOME}/app.jar --spring.profiles.active=${APP_PROFILE:-default}"]