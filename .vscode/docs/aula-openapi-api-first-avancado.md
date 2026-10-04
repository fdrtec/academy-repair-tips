# Aula Avançada: Desenvolvimento API-First com OpenAPI

> Objetivo: sair desta aula sabendo **projetar, validar, mockar, versionar e governar** uma API usando OpenAPI 3.1 como contrato único de verdade — antes de escrever uma linha de implementação.

---

## 1. O que "API-First" realmente significa

Existem três posturas comuns no desenvolvimento de APIs:

| Abordagem | Ordem das coisas | Problema típico |
|---|---|---|
| **Code-first** | Escreve o código → gera a doc a partir de annotations | Contrato reflete decisões de implementação, quebra silenciosamente |
| **Doc-first / afterthought** | Escreve o código → documenta manualmente depois | Documentação desatualizada em semanas |
| **API-First (design-first)** | Escreve o **contrato OpenAPI** → valida com stakeholders/mock → **depois** implementa | Exige disciplina de revisão, mas contrato nunca mente |

No API-First, o arquivo `openapi.yaml` (ou `.json`) é o **contrato**: front-end, backend, QA, parceiros externos e — cada vez mais relevante — **agentes de IA que geram código** trabalham a partir dele. Um agente de codificação com um contrato OpenAPI bem definido gera rotas, validação e tipos corretos de primeira; sem ele, ele "chuta" formatos de resposta e nomes de endpoint.

**Fluxo de trabalho típico:**

```
1. Design       → escrever/editar o contrato OpenAPI (YAML)
2. Lint         → validar estilo e regras de governança (Spectral)
3. Mock         → subir um servidor fake a partir do contrato (Prism)
4. Revisão      → times de consumo (frontend, parceiros) testam contra o mock
5. Codegen      → gerar SDKs / stubs de servidor (OpenAPI Generator)
6. Implementação→ preencher a lógica de negócio nos stubs gerados
7. Contract test→ garantir que a implementação real bate com o contrato
8. Publicação   → gerar docs (Redoc/Scalar/Stoplight) + versionamento
```

---

## 2. Anatomia do documento OpenAPI (nível avançado)

A versão atual estável é a **OpenAPI 3.1.1**, que alinhou o `components.schemas` ao **JSON Schema 2020-12** (diferença importante em relação à 3.0, que usava um subconjunto próprio de JSON Schema).

```yaml
openapi: 3.1.0
info:
  title: API de Pedidos
  version: 1.4.0
  summary: Gerencia pedidos, itens e pagamentos
servers:
  - url: https://api.exemplo.com/v1
tags:
  - name: pedidos
paths:
  /pedidos/{pedidoId}:
    get:
      operationId: obterPedido
      tags: [pedidos]
      parameters:
        - $ref: '#/components/parameters/PedidoId'
      responses:
        '200':
          description: Pedido encontrado
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Pedido'
        '404':
          $ref: '#/components/responses/NaoEncontrado'
components:
  parameters:
    PedidoId:
      name: pedidoId
      in: path
      required: true
      schema:
        type: string
        format: uuid
  schemas:
    Pedido:
      type: object
      required: [id, status]
      properties:
        id: { type: string, format: uuid }
        status:
          type: string
          enum: [criado, pago, enviado, cancelado]
  responses:
    NaoEncontrado:
      description: Recurso não encontrado
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/Erro'
  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
security:
  - bearerAuth: []
```

### Pontos que separam o iniciante do avançado:

1. **Reuso via `$ref`** — nunca duplique schemas de erro, parâmetros de paginação ou headers de auth. Centralize em `components/` e referencie.
2. **Composição de schemas** — `allOf` (herança/mixin), `oneOf` (union type, exige `discriminator` para o cliente saber qual variante é), `anyOf` (validação flexível). Exemplo de polimorfismo real:

```yaml
Pagamento:
  oneOf:
    - $ref: '#/components/schemas/PagamentoCartao'
    - $ref: '#/components/schemas/PagamentoPix'
  discriminator:
    propertyName: tipo
    mapping:
      cartao: '#/components/schemas/PagamentoCartao'
      pix: '#/components/schemas/PagamentoPix'
```

3. **`callbacks`** — descreve webhooks que a *sua* API dispara para o consumidor (ex.: notificação de pagamento aprovado), dentro do próprio contrato.
4. **`links`** — conecta a resposta de uma operação a parâmetros de outra (ex.: `POST /pedidos` retorna um `id` que pode alimentar automaticamente `GET /pedidos/{id}`), permitindo que ferramentas montem fluxos de teste automaticamente.
5. **`webhooks`** (novo na 3.1, no nível raiz) — para APIs orientadas a eventos assíncronos que não seguem o padrão request/response.
6. **Múltiplos `servers` com variáveis** — útil para ambientes (sandbox, produção) sem duplicar o contrato.
7. **Content negotiation avançada** — múltiplos `content` types por resposta (`application/json`, `application/vnd.api+json`, `text/csv`) descritos separadamente.

---

## 3. Governança: linting como CI gate

Um contrato sem regras vira uma bagunça em poucos sprints. A ferramenta padrão de mercado é o **Spectral** (Stoplight), que permite escrever regras customizadas (ruleset) e falhar o pipeline se alguém quebrar convenções.

```yaml
# .spectral.yaml
extends: spectral:oas
rules:
  operation-id-kebab-case:
    given: "$.paths[*][*].operationId"
    then:
      function: casing
      functionOptions: { type: kebab }
  must-have-rate-limit-headers:
    given: "$.paths[*][*].responses[*].headers"
    then:
      field: X-RateLimit-Remaining
      function: truthy
```

Rode `spectral lint openapi.yaml` no CI antes de qualquer merge.

---

## 4. Ecossistema de ferramentas (o "framework" na prática)

Não existe *um* framework único chamado "OpenAPI" além da especificação — o valor está no conjunto de ferramentas que a consomem. Panorama atual:

| Categoria | Ferramentas líderes | Papel |
|---|---|---|
| Design visual / governança | **Stoplight**, **SwaggerHub** | Editor visual, style guides, revisão colaborativa |
| Linting | **Spectral** | Regras de estilo/segurança como código, gate de CI |
| Mock server | **Prism** (Stoplight), **Microcks** | Sobe uma API fake a partir do contrato para o frontend testar antes do backend existir |
| Geração de código | **OpenAPI Generator**, **openapi-typescript**, **Fern** | Gera SDKs de cliente e stubs de servidor em dezenas de linguagens |
| Documentação | **Redoc / Redocly**, **Scalar**, **Stoplight Elements** | Renderiza o contrato em portal de docs navegável e interativo |
| Contract testing | **Dredd**, **Schemathesis**, **openapi_first** (Ruby) | Testa se a implementação real bate com o contrato (inclusive fuzzing de schema) |
| Gateway/runtime | **Zuplo**, **Kong** | Aplica o contrato em runtime (validação de request/response ao vivo) |

> Dica prática: comece pequeno — **Spectral (lint) + Prism (mock) + Redoc (docs)** já cobre 80% do ciclo API-First sem custo de licença.

---

## 5. Exercício guiado (faça isso agora)

1. Crie um arquivo `openapi.yaml` com uma API de "Biblioteca" (`/livros`, `/livros/{id}`).
2. Modele `Livro` com `allOf` combinando um schema base `RecursoBase` (`id`, `criadoEm`) + campos próprios (`titulo`, `autor`, `disponivel`).
3. Adicione `securitySchemes` com `bearerAuth`.
4. Rode `npx @stoplight/spectral-cli lint openapi.yaml`.
5. Suba um mock: `npx @stoplight/prism-cli mock openapi.yaml` e faça `curl` nos endpoints — perceba que funciona **sem nenhum backend real**.
6. Gere um cliente TypeScript: `npx openapi-typescript openapi.yaml -o api.d.ts`.

Se você completou os 6 passos, você praticou o ciclo completo design → lint → mock → codegen.

---

## 6. Vídeos recomendados para complementar

(links completos na seção de recursos da resposta)

- **"Complete API Tutorial (from design to implementation with OpenAPI)"** — percorre o ciclo inteiro design-first até a implementação.
- **"Adopting an API First Approach with OpenAPI 3.0"** — palestra sobre por que adotar API-First como cultura de engenharia, não só ferramenta.
- **"OpenAPI: Beginner to Guru"** — série mais longa, boa para quem quer aprofundar sintaxe e edge cases da spec.

---

## 7. Checklist de fixação (revise sem olhar o material acima)

- [ ] Explico a diferença entre code-first e design-first em uma frase.
- [ ] Sei quando usar `allOf` vs `oneOf` vs `anyOf`.
- [ ] Sei por que `discriminator` é necessário em `oneOf` para polimorfismo.
- [ ] Sei nomear 3 ferramentas e o papel de cada uma no ciclo (lint, mock, codegen).
- [ ] Sei explicar `callbacks` vs `webhooks` (raiz do documento) na 3.1.
- [ ] Sei por que linting deve rodar no CI, não só localmente.

Se marcou tudo, siga para o quiz interativo abaixo.
