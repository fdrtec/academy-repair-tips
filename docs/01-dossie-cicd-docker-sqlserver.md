# Dossiê de estudo: GitHub Actions, Docker, SQL Server e Kubernetes

## 1. Objetivo

Este documento orienta a evolução acadêmica do projeto `fdrtec/academy-repair-tips`, uma API Java/Spring Boot, para uma simulação profissional sem custo financeiro direto.

Fluxo desejado:

```text
Git → Pull Request → GitHub Actions → testes → imagem Docker → GHCR → Docker Compose → Kubernetes local
```

O projeto atualmente utiliza Maven, Java 25, Spring Boot, JPA, Actuator, MapStruct e H2 em memória.

> A estrutura e os resultados de buscas podem estar incompletos. Consulte também a busca de código do repositório no GitHub quando precisar confirmar a existência de arquivos adicionais.

---

## 2. Diagnóstico atual

### Já existe

- Projeto Maven com Maven Wrapper.
- Java 25 configurado no `pom.xml`.
- Spring Boot Web.
- Spring Data JPA.
- Bean Validation.
- Actuator.
- MapStruct.
- Testes de integração com MockMvc e JUnit.
- Controllers para peças e equipamentos.
- Arquivo de requisições HTTP e coleção Bruno.
- Branch principal `main`.

### Ainda falta implementar

- Driver JDBC do SQL Server.
- Configuração da aplicação por variáveis de ambiente.
- Perfil específico para Docker.
- `Dockerfile`.
- `.dockerignore`.
- `docker-compose.yml`.
- `.env.example`.
- Persistência do SQL Server por volume.
- Pipeline de CI com GitHub Actions.
- Pipeline para publicar imagem no GitHub Container Registry.
- Estratégia de migrations, preferencialmente Flyway ou Liquibase.
- Separação mais clara entre testes H2/Testcontainers e SQL Server.
- Configuração Kubernetes com Deployment, Service, ConfigMap e Secret.
- Política de tags imutáveis para imagens.
- Documentação de rollback e troubleshooting.

---

## 3. H2 versus SQL Server

O arquivo atual usa H2 em memória:

```properties
spring.datasource.url=jdbc:h2:mem:repairtipsdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

Isso é conveniente para começar, mas os dados desaparecem quando a aplicação termina. Para simular um ambiente mais realista, o SQL Server ficará em um container separado da API.

### Regra importante de rede

- Fora do Docker, normalmente o host é `localhost`.
- Dentro da rede do Compose, a API deve usar o nome do serviço: `sqlserver`.
- Dentro de Kubernetes, a API usará o nome de um Service, por exemplo `sqlserver` ou `sqlserver-service`.

---

## 4. Alteração no Maven

Adicione o driver JDBC ao `pom.xml`:

```xml
<dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <scope>runtime</scope>
</dependency>
```

A dependência H2 pode permanecer temporariamente para testes locais, mas o ambiente Docker deve usar SQL Server. Em uma etapa posterior, utilize Testcontainers ou um perfil de testes separado.

---

## 5. Configuração da aplicação

Substitua a configuração fixa do H2 por variáveis de ambiente em `src/main/resources/application.properties`:

```properties
spring.application.name=${SPRING_APPLICATION_NAME:repair-tips-api}

spring.datasource.url=${DB_URL:jdbc:sqlserver://localhost:1433;databaseName=repair_tips_db;encrypt=true;trustServerCertificate=true}
spring.datasource.driver-class-name=${DB_DRIVER_CLASS_NAME:com.microsoft.sqlserver.jdbc.SQLServerDriver}
spring.datasource.username=${DB_USERNAME:sa}
spring.datasource.password=${DB_PASSWORD:YourStrong!Passw0rd}

spring.jpa.hibernate.ddl-auto=${DDL_AUTO:update}
spring.jpa.show-sql=${SHOW_SQL:false}
spring.jpa.properties.hibernate.format_sql=${FORMAT_SQL:true}
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect

server.port=${SERVER_PORT:8080}

management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always
```

### Sobre `ddl-auto`

- `update`: adequado para laboratório, pois cria/atualiza tabelas automaticamente.
- `validate`: verifica o schema e falha se estiver diferente.
- `none`: não executa alterações.
- `create` e `create-drop`: destrutivos e não recomendados para dados persistentes.

Para produção, prefira migrations versionadas com Flyway ou Liquibase.

---

## 6. Arquivo de ambiente

Crie `.env.example` para documentar os valores necessários:

```env
SPRING_APPLICATION_NAME=repair-tips-api
DB_URL=jdbc:sqlserver://sqlserver:1433;databaseName=repair_tips_db;encrypt=true;trustServerCertificate=true
DB_USERNAME=sa
DB_PASSWORD=YourStrong!Passw0rd
DB_DRIVER_CLASS_NAME=com.microsoft.sqlserver.jdbc.SQLServerDriver
DDL_AUTO=update
SHOW_SQL=false
FORMAT_SQL=true
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=docker
```

Crie uma cópia local chamada `.env` e altere a senha:

```bash
cp .env.example .env
```

No Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

Nunca versione o `.env` real. No `.gitignore`, mantenha:

```gitignore
.env
.env.*
!.env.example
```

---

## 7. Dockerfile recomendado

Crie `Dockerfile` na raiz:

```dockerfile
FROM maven:3.9.9-eclipse-temurin-25 AS build

WORKDIR /workspace
COPY . .
RUN chmod +x mvnw && ./mvnw --batch-mode clean package -DskipTests

FROM eclipse-temurin:25-jre

WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Origem das partes

- `maven:...` é a imagem de build; contém Maven e JDK.
- `eclipse-temurin:25-jre` é a imagem final; contém somente o Java necessário para executar.
- `COPY --from=build` copia o JAR da etapa de compilação.
- O padrão multi-stage evita levar Maven e código-fonte desnecessário para a imagem final.

---

## 8. Arquivo `.dockerignore`

```dockerignore
.git
.github
.idea
.vscode
target
*.iml
*.log
.env
```

---

## 9. Docker Compose com API e SQL Server

Crie `docker-compose.yml`:

```yaml
services:
  sqlserver:
    image: mcr.microsoft.com/mssql/server:2022-latest
    container_name: repair-tips-sql
    environment:
      ACCEPT_EULA: "Y"
      MSSQL_SA_PASSWORD: ${DB_PASSWORD}
      MSSQL_PID: Developer
    ports:
      - "1433:1433"
    volumes:
      - sqlserver_data:/var/opt/mssql
    healthcheck:
      test: ["CMD-SHELL", "(/opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P \"$${MSSQL_SA_PASSWORD}\" -C -Q \"SELECT 1\" || /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P \"$${MSSQL_SA_PASSWORD}\" -Q \"SELECT 1\")"]
      interval: 10s
      timeout: 5s
      retries: 30
      start_period: 30s

  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: repair-tips-api
    depends_on:
      sqlserver:
        condition: service_healthy
    environment:
      SPRING_APPLICATION_NAME: ${SPRING_APPLICATION_NAME}
      DB_URL: ${DB_URL}
      DB_USERNAME: ${DB_USERNAME}
      DB_PASSWORD: ${DB_PASSWORD}
      DB_DRIVER_CLASS_NAME: ${DB_DRIVER_CLASS_NAME}
      DDL_AUTO: ${DDL_AUTO}
      SHOW_SQL: ${SHOW_SQL}
      FORMAT_SQL: ${FORMAT_SQL}
      SERVER_PORT: ${SERVER_PORT}
      SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE}
    ports:
      - "${SERVER_PORT}:8080"
    restart: unless-stopped

volumes:
  sqlserver_data:
```

### Montagens e comunicação

```text
sqlserver_data:/var/opt/mssql
```

É um volume nomeado. O lado esquerdo é o volume Docker; o lado direito é o diretório de dados dentro do SQL Server.

A API não usa `localhost` para acessar o banco. Ela usa:

```text
jdbc:sqlserver://sqlserver:1433;...
```

`sqlserver` é o nome do serviço e funciona por DNS interno da rede criada pelo Compose.

---

## 10. Comandos Docker Compose

Validar a configuração:

```bash
docker compose config
```

Construir e iniciar:

```bash
docker compose up --build
```

Executar em segundo plano:

```bash
docker compose up --build -d
```

Ver containers:

```bash
docker compose ps
```

Ver logs:

```bash
docker compose logs -f app
docker compose logs -f sqlserver
```

Testar a API:

```bash
curl http://localhost:8080/actuator/health
```

Parar sem apagar dados:

```bash
docker compose down
```

Parar e apagar o volume do banco:

```bash
docker compose down -v
```

> `down -v` apaga a persistência do SQL Server. Use somente quando quiser recriar o laboratório.

---

## 11. Criar o banco manualmente, se necessário

Conecte ao SQL Server com Azure Data Studio, DBeaver ou `sqlcmd` e execute:

```sql
CREATE DATABASE repair_tips_db;
GO
```

Em alguns cenários, a aplicação não cria o banco automaticamente; ela cria as tabelas depois que o banco já existe. Se o banco não existir, crie-o manualmente.

---

## 12. GitHub Actions: CI

Crie `.github/workflows/ci.yml`:

```yaml
name: CI

on:
  pull_request:
    branches:
      - main
  push:
    branches:
      - main

permissions:
  contents: read

jobs:
  build-and-test:
    name: Build and test
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Configurar Java 25
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '25'
          cache: maven

      - name: Dar permissão ao Maven Wrapper
        run: chmod +x mvnw

      - name: Compilar e testar
        run: ./mvnw --batch-mode clean verify

      - name: Guardar JAR
        uses: actions/upload-artifact@v4
        with:
          name: repair-tips-api
          path: target/*.jar
```

### Como testar o CI

```bash
git checkout -b feature/testar-ci
git add .
git commit -m "ci: adicionar pipeline de build e testes"
git push -u origin feature/testar-ci
```

Abra uma Pull Request para `main`. O workflow deve executar automaticamente.

---

## 13. GitHub Actions: publicar imagem no GHCR

Crie `.github/workflows/docker-publish.yml`:

```yaml
name: Publish Docker image

on:
  push:
    branches:
      - main

permissions:
  contents: read
  packages: write

env:
  IMAGE_NAME: ghcr.io/fdrtec/academy-repair-tips

jobs:
  publish:
    name: Build and publish image
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Login no GHCR
        uses: docker/login-action@v3
        with:
          registry: ghcr.io
          username: ${{ github.actor }}
          password: ${{ secrets.GITHUB_TOKEN }}

      - name: Configurar Buildx
        uses: docker/setup-buildx-action@v3

      - name: Build e push
        uses: docker/build-push-action@v6
        with:
          context: .
          push: true
          tags: |
            ${{ env.IMAGE_NAME }}:latest
            ${{ env.IMAGE_NAME }}:${{ github.sha }}
```

### De onde vem o token?

`secrets.GITHUB_TOKEN` é um token temporário fornecido pelo próprio GitHub Actions. A permissão:

```yaml
packages: write
```

permite publicar a imagem no GitHub Container Registry.

### Tags

- `latest`: conveniente para laboratório.
- `${{ github.sha }}`: identifica exatamente o commit que gerou a imagem.

Para maior segurança, use a tag do commit em deploys importantes, em vez de depender somente de `latest`.

---

## 14. Custos e limites

Para um projeto público de estudo, o fluxo acima normalmente pode ser usado sem cobrança direta:

- GitHub Actions para CI.
- GitHub Container Registry para publicar imagens.
- Docker local.
- SQL Server Developer em container local.
- kind, minikube ou k3d para Kubernetes local.

Verifique sempre os limites atuais da sua conta e do GitHub antes de uso intensivo. Não coloque credenciais reais no código ou nos workflows.

---

## 15. Git e fluxo de trabalho

Atualizar `main`:

```bash
git checkout main
git pull origin main
```

Criar uma branch:

```bash
git checkout -b feature/docker-sqlserver
```

Ver alterações:

```bash
git status
git diff
```

Salvar alterações:

```bash
git add .
git commit -m "feat: adicionar ambiente Docker com SQL Server"
```

Publicar branch:

```bash
git push -u origin feature/docker-sqlserver
```

Depois abra uma Pull Request e aguarde o CI.

---

## 16. Testes e banco

Os testes atuais carregam o contexto Spring e usam repositórios. Ao remover H2 completamente, os testes precisarão de um banco SQL Server disponível ou de uma estratégia de teste dedicada.

Opções didáticas:

### Opção A — manter H2 apenas para testes

- H2 continua com `scope=test`.
- Docker e execução real usam SQL Server.
- É simples, mas H2 não replica todas as características do SQL Server.

### Opção B — Testcontainers

- O teste sobe um SQL Server em container temporário.
- O teste usa o mesmo banco da aplicação real.
- É mais fiel, mas exige mais memória e configuração.

Recomendação inicial: usar H2 somente nos testes enquanto o ambiente Docker usa SQL Server; depois estudar Testcontainers.

---

## 17. Migrations

`ddl-auto=update` é aceitável para laboratório, mas não é a melhor prática profissional. O próximo passo recomendado é adicionar Flyway ou Liquibase:

```text
V1__create_initial_schema.sql
V2__add_new_column.sql
V3__create_index.sql
```

Benefícios:

- histórico versionado do banco;
- execução controlada;
- rollback planejado;
- ambientes reproduzíveis;
- menor risco de alterações automáticas inesperadas.

---

## 18. Kubernetes local

Quando Docker Compose estiver funcionando, instale `kubectl` e `kind`:

```bash
kind create cluster --name academy
kubectl get nodes
```

Estrutura inicial:

```text
k8s/
├── configmap.yml
├── secret.example.yml
├── deployment.yml
└── service.yml
```

Exemplo de `ConfigMap`:

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: repair-tips-api-config
data:
  SERVER_PORT: "8080"
  DDL_AUTO: "update"
```

Exemplo de `Secret` para laboratório:

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: repair-tips-api-secret
type: Opaque
stringData:
  DB_USERNAME: sa
  DB_PASSWORD: YourStrong!Passw0rd
```

Não versionar secrets reais. O exemplo deve conter somente valores fictícios.

---

## 19. Roadmap recomendado

### Fase 1 — aplicação

- [ ] manter a API funcionando localmente;
- [ ] executar `./mvnw clean verify`;
- [ ] revisar testes;
- [ ] verificar `/actuator/health`.

### Fase 2 — Docker

- [ ] adicionar driver SQL Server;
- [ ] criar `Dockerfile`;
- [ ] criar `.dockerignore`;
- [ ] criar `.env.example`;
- [ ] criar `docker-compose.yml`;
- [ ] confirmar persistência do volume.

### Fase 3 — CI

- [ ] criar `ci.yml`;
- [ ] abrir uma Pull Request;
- [ ] provocar uma falha intencional;
- [ ] corrigir e observar o pipeline passar.

### Fase 4 — imagem

- [ ] publicar no GHCR;
- [ ] usar tag com SHA;
- [ ] baixar e executar a imagem localmente.

### Fase 5 — Kubernetes

- [ ] criar cluster kind;
- [ ] criar Deployment;
- [ ] criar Service;
- [ ] usar ConfigMap;
- [ ] usar Secret;
- [ ] praticar rollout e rollback.

### Fase 6 — preparação para microservices

- [ ] separar domínios;
- [ ] separar bancos por serviço;
- [ ] criar imagens independentes;
- [ ] criar pipelines independentes;
- [ ] estudar comunicação síncrona e assíncrona;
- [ ] adicionar observabilidade.

---

## 20. Comandos de referência

```bash
# Java/Maven
./mvnw clean test
./mvnw clean verify
./mvnw clean package -DskipTests

# Docker
 docker build -t repair-tips-api:local .
 docker run --rm -p 8080:8080 repair-tips-api:local

# Compose
 docker compose config
 docker compose up --build
 docker compose up --build -d
 docker compose ps
 docker compose logs -f app
 docker compose down
 docker compose down -v

# Git
 git status
 git checkout -b feature/nome-da-feature
 git add .
 git commit -m "feat: descrever alteração"
 git push -u origin feature/nome-da-feature

# Kubernetes local
 kind create cluster --name academy
 kubectl get nodes
 kubectl get pods
 kubectl apply -f k8s/
 kubectl rollout status deployment/repair-tips-api
 kubectl logs deployment/repair-tips-api
```

---

## 21. Resultado esperado

Ao completar esta etapa, o projeto deverá ter o seguinte fluxo:

```text
1. Desenvolvedor cria branch
2. Desenvolvedor abre Pull Request
3. GitHub Actions compila e testa
4. Merge em main
5. GitHub Actions constrói a imagem
6. Imagem é publicada no GHCR
7. Docker Compose executa API e SQL Server
8. Volume mantém os dados do banco
9. Configurações vêm de variáveis de ambiente
10. Próxima etapa: deploy em Kubernetes local
```

Este fluxo é uma simulação estudantil realista de práticas usadas em times profissionais, mantendo a complexidade sob controle e sem exigir uma conta cloud paga.
