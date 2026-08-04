# Diretrizes do Projeto

## Language
- Sempre responda e interaja em português-Brasil.

## Arquitetura
- Trate o projeto como um backend Spring em Java 25.
- Mantenha uma estrutura em camadas clara: controller, service e repository.
- Prefira classes e métodos pequenos e focados.
- Aplique consistentemente os princípios SOLID e boas práticas de clean code.
- Use padrões de projeto apenas quando eles simplificarem o domínio ou melhorarem a manutenção.

## Design da API
- Trabalhe em modo API-first: defina o contrato do endpoint, a request e os DTOs de response antes de implementar o fluxo de service e repository.
- Mantenha os controllers enxutos e delegue as regras de negócio para os services.
- Exponha endpoints REST estáveis e explícitos, com modelos de entrada e saída claros.

## DTOs e Mapeamento
- Use records para DTOs quando eles fizerem sentido para o caso de uso.
- Use MapStruct para mapear entre DTOs e entidades.
- Evite mapeamento manual quando o MapStruct conseguir expressar a transformação.
- Mantenha os modelos de entidade separados dos modelos de transporte.

## Estilo de Código
- Prefira código legível e que revele intenção em vez de código esperto demais.
- Mantenha a lógica de negócio fora de controllers e repositories.
- Nomeie classes e métodos pela intenção de domínio, não por atalhos técnicos.
- Ao adicionar código novo, mantenha consistência com as convenções Spring já existentes no repositório.

## Viés de Implementação
- Prefira soluções diretas e fáceis de testar.
- Introduza abstrações apenas quando houver um caso de uso real para elas.
- Quando um padrão for escolhido, deixe o motivo claro na estrutura do código, e não em comentários.
