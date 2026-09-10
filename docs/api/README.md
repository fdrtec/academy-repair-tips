# Contrato OpenAPI

O arquivo `openapi.yaml` é a fonte de verdade da API. Os records Java e as annotations dos controllers devem refletir esse contrato, nunca redefinir silenciosamente suas rotas ou payloads.

Cada recurso usa um único record compartilhado entre entrada e saída: `PartDto` e `EquipamentDto`. Campos exclusivos de resposta são marcados como somente leitura (`id` e `parts`); campos exclusivos de entrada são marcados como somente escrita (`partIds`).

## Fluxo para uma nova operação

1. Modele ou altere primeiro o endpoint em `openapi.yaml`.
2. Reutilize schemas, parâmetros e respostas em `components` com `$ref`.
3. Revise o contrato com os consumidores e, quando necessário, gere um mock a partir dele.
4. Implemente ou ajuste o record de request/response e o service.
5. Adicione no controller `@Operation`, `@ApiResponse` e `@Parameter` correspondentes.
6. Crie ou atualize o teste de comportamento e o teste do documento em `/v3/api-docs`.
7. Execute `./mvnw test` e valide o YAML com Spectral no CI.

## Lint local

Execute `npx --yes @stoplight/spectral-cli lint docs/api/openapi.yaml`. As regras do projeto ficam em `.spectral.yaml`.

## Documentação local

Com a aplicação em execução:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Documento gerado: `http://localhost:8080/v3/api-docs`
- Contrato versionado: `docs/api/openapi.yaml`

O contrato usa OpenAPI 3.1.0, alinhado à especificação recomendada na aula e à linha 3.1.x do Springdoc configurada no projeto.
