# 01 - Gestão de peças

## Objetivo
Implementar o fluxo completo de cadastro, consulta, atualização e remoção do objeto "Peça", incluindo uma consulta paginada.

## Escopo
- Criar a modelagem da entidade "Peça" com os atributos principais:
  - nome
  - numero
- Implementar o CRUD completo da entidade.
- Expor os endpoints via API REST.
- Persistir os dados em banco H2 para o ambiente de desenvolvimento.

## Requisitos funcionais
- [ ] Criar uma nova peça.
- [ ] Consultar uma peça por identificador.
- [ ] Listar peças com paginação.
- [ ] Atualizar uma peça existente.
- [ ] Remover uma peça.

## Requisitos técnicos
- Estruturar a API em camadas: controller, service e repository.
- Seguir o fluxo API-first, com definição clara de contrato de entrada e saída.
- Utilizar OpenAPI/Swagger compatível com o projeto.
- Usar records para os DTOs.
- Utilizar MapStruct para conversão entre DTOs e entidade.
- Preferir uma abordagem com reflection para associar os campos de forma mais genérica, considerando a entidade JPA.
- Utilizar Lombok para reduzir boilerplate.
- Implementar uma camada global de tratamento de exceções no Spring usando @ControllerAdvice e/ou @RestControllerAdvice.
- Retornar respostas padronizadas com Problem Details, incluindo status, título, detalhe e possíveis extensões relevantes.
- Tratar, no mínimo, exceções de recurso não encontrado, validação de entrada e erro interno inesperado.
- Evitar comentários no código.

## Critérios de aceite
- Os endpoints do CRUD estão disponíveis e respondem corretamente.
- A consulta de peças retorna os resultados paginados.
- A aplicação persiste os dados corretamente no banco H2.
- A estrutura do projeto segue as camadas propostas e mantém o código organizado.

## Entregáveis
- Controller da API.
- Service com regras de negócio.
- Repository JPA.
- Entidade e DTOs.
- Configuração básica para banco H2.
- Arquivo com exemplos de requisições cURL para os endpoints.

## Arquivo de exemplos de requisições
- Criar um arquivo chamado requests-pecas.http ou requests-pecas.sh com exemplos de cURL para todos os endpoints do CRUD.
- O arquivo deve incluir exemplos para:
  - criação de peça
  - consulta por identificador
  - listagem paginada
  - atualização de peça
  - remoção de peça
