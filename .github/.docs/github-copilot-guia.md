# Guia Pratico de GitHub Copilot

## Como usar spec, instructions, prompt, skill e agent com eficiencia

Este guia mostra como estruturar o uso do GitHub Copilot de forma profissional, previsivel e economica em contexto. A ideia central e simples: a qualidade da IA depende menos de "prompts magicos" e mais de uma boa arquitetura de contexto.

## Visao geral

Para usar todo o poder do Copilot, pense em cinco camadas:

- `spec`: define o que precisa ser construido
- `instructions`: definem como o trabalho deve ser feito
- `prompt`: dispara uma tarefa especifica
- `skill`: encapsula um workflow especializado
- `agent`: define um especialista com comportamento e ferramentas proprios

Se essas cinco camadas estiverem bem organizadas, o Copilot fica mais preciso, gasta menos tokens e produz respostas mais consistentes.

---

## 1. O que e cada conceito

### Spec

`spec` nao e, em geral, um primitivo oficial do Copilot como `prompt`, `skill` ou `instructions`. Ele e um artefato de engenharia: uma especificacao funcional, tecnica ou de negocio.

A spec responde:

- o que deve ser feito
- por que deve ser feito
- quais regras precisam ser respeitadas
- quais criterios de aceite definem sucesso

Exemplos de spec:

- requisito funcional
- RFC
- ADR
- contrato OpenAPI
- checklist de aceite
- fluxo de negocio

Exemplo de estrutura:

```md
# Feature Spec: Cadastro de Cliente

## Objetivo

Permitir cadastro de cliente PF com validacao de CPF e idempotencia.

## Regras

- CPF deve ser valido
- E-mail deve ser unico
- Requisicao repetida com a mesma chave de idempotencia nao pode duplicar registro

## Criterios de aceite

- Retornar 201 no primeiro cadastro
- Retornar 200 em repeticao idempotente
- Retornar 409 quando e-mail ja existir com outra identidade
```

### Instructions

`instructions` sao regras de trabalho. Elas nao definem o requisito do negocio; elas definem como o agente deve operar naquele contexto.

Elas servem para:

- padroes arquiteturais
- convencoes de codigo
- regras de teste
- padroes de documentacao
- comandos de build
- restricoes tecnicas

Ha dois tipos principais:

- `copilot-instructions.md` ou `AGENTS.md`: regras gerais do projeto
- `*.instructions.md`: regras especificas por linguagem, pasta, stack ou tipo de tarefa

### Prompt

`prompt` e uma tarefa reutilizavel. Ele serve para executar algo especifico e recorrente.

Exemplos:

- gerar testes
- revisar codigo
- documentar feature
- criar diagrama Mermaid
- investigar conflito de dependencia

Prompt bom e focado em uma tarefa so.

### Skill

`skill` e um workflow especializado. E melhor que um prompt quando a tarefa tem varias etapas, material de apoio, templates, scripts ou referencias.

Exemplos:

- auditoria de arquitetura
- diagnostico de incidentes
- analise de dependencias
- preparacao de release
- documentacao tecnica padronizada

### Agent

`agent` e um especialista configurado. Ele define:

- papel
- limites
- ferramentas permitidas
- estilo de execucao
- possiveis handoffs

Use agent quando o problema nao e so a tarefa, mas tambem o comportamento desejado de quem executa.

---

## 2. Quando usar cada um

Use `spec` quando a IA precisa entender o dominio e os criterios de sucesso.

Use `instructions` quando voce quer reduzir ambiguidade recorrente no projeto.

Use `prompt` quando a tarefa e repetivel e tem inicio e fim claros.

Use `skill` quando existe um processo especializado com mais de uma etapa.

Use `agent` quando voce quer isolar comportamento, ferramentas e responsabilidade.

Resumo rapido:

- Se responde "o que construir": `spec`
- Se responde "como trabalhamos aqui": `instructions`
- Se responde "faca esta tarefa agora": `prompt`
- Se responde "siga este workflow": `skill`
- Se responde "qual especialista executa": `agent`

---

## 3. Como nomear specs para o Copilot entender melhor

O Copilot nao tem um tipo oficial de arquivo chamado `spec`. Ele entende especificacoes como contexto textual bem estruturado. Por isso, a melhor estrategia e usar nomes claros e previsiveis.

Os termos mais reconheciveis costumam ser:

- `spec`
- `specification`
- `requirements`
- `feature spec`
- `design doc`
- `technical design`
- `ADR`
- `RFC`
- `acceptance criteria`
- `OpenAPI contract`

Boas convencoes de nome de arquivo:

- `docs/specs/cadastro-cliente.md`
- `docs/specs/feature-cadastro-cliente.md`
- `docs/requirements/cadastro-cliente.md`
- `docs/adr/0001-arquitetura.md`
- `docs/rfc/rfc-novo-fluxo.md`
- `docs/api/openapi.yaml`

Se voce precisa escolher um nome padrao unico para o time, `Feature Spec` costuma ser o melhor equilibrio entre clareza funcional e reconhecimento por IA.

Exemplo recomendado:

```md
# Feature Spec: Cadastro de Cliente

## Objetivo
## Contexto
## Regras de Negocio
## Restricoes Tecnicas
## Criterios de Aceite
## Fora de Escopo
```

Se voce quiser aumentar a chance de o Copilot puxar esse contexto em customizacoes, use esses termos tambem nas descricoes de prompts, instructions e skills.

Exemplo de descricao eficaz:

```yaml
description: "Use when implementing a feature from a spec, requirements document, ADR, or acceptance criteria"
```

---

## 4. Estrutura ideal de um workspace

Uma estrutura madura para projetos com GitHub Copilot pode ser:

```text
my-project/
├─ .github/
│  ├─ copilot-instructions.md
│  ├─ instructions/
│  │  ├─ backend.instructions.md
│  │  ├─ frontend.instructions.md
│  │  └─ testing.instructions.md
│  ├─ prompts/
│  │  ├─ gerar-testes.prompt.md
│  │  ├─ revisar-codigo.prompt.md
│  │  └─ documentar-feature.prompt.md
│  ├─ agents/
│  │  ├─ reviewer.agent.md
│  │  └─ architect.agent.md
│  └─ skills/
│     ├─ architecture-audit/
│     │  ├─ SKILL.md
│     │  ├─ references/
│     │  │  ├─ checklist.md
│     │  │  └─ heuristics.md
│     │  └─ assets/
│     │     └─ topology-template.md
│     └─ release-readiness/
│        ├─ SKILL.md
│        └─ references/
│           └─ gates.md
├─ docs/
│  ├─ specs/
│  │  ├─ feature-x.md
│  │  └─ billing-reconciliation.md
│  ├─ adr/
│  │  ├─ 0001-architecture.md
│  │  └─ 0002-observability.md
│  └─ api/
│     └─ openapi.yaml
└─ src/
```

Essa organizacao separa o requisito, a regra, a tarefa, o workflow e o especialista.

---

## 5. Como essas camadas se combinam

O Copilot funciona melhor quando o contexto e montado em camadas.

Ordem conceitual:

1. contexto base da IDE e do modo atual
2. instrucoes globais do projeto
3. instrucoes especificas por arquivo, pasta ou stack
4. agente selecionado
5. prompt executado
6. skill carregada sob demanda
7. spec anexada ou referenciada

Em termos praticos:

- a `spec` informa o problema
- as `instructions` restringem o modo de implementacao
- o `prompt` diz o que executar agora
- a `skill` guia um procedimento
- o `agent` controla o comportamento do executor

---

## 6. Exemplo completo de montagem

Suponha um backend Java com APIs REST, testes e documentacao.

### 6.1 Spec

```md
# Feature Spec: Cadastro de Cliente

## Objetivo

Permitir cadastro de cliente PF com validacao de CPF e idempotencia.

## Regras

- CPF deve ser valido
- E-mail deve ser unico
- Requisicao repetida com mesma chave de idempotencia nao pode duplicar registro

## Criterios de aceite

- Retornar 201 no primeiro cadastro
- Retornar 200 com mesmo payload em repeticao idempotente
- Retornar 409 quando e-mail ja existir com outra identidade
```

### 6.2 Instrucao global do projeto

```md
# Project Guidelines

## Architecture

Use arquitetura hexagonal.
Adapters nao podem conter regra de negocio.

## Build and Test

Antes da suite completa, prefira testes focados no slice alterado.

## Conventions

Favor injecao por construtor.
Evite logica em controllers.
```

### 6.3 Instrucao especifica para backend

```md
---
description: "Use when editing backend Java production code or unit tests"
applyTo: "src/main/java/**, src/test/java/**"
---
# Backend Java Rules

- Use AssertJ nos testes
- Prefira nomes de teste descritivos
- Servicos de aplicacao nao devem acessar infraestrutura diretamente
- Trate nulls explicitamente nas bordas
```

### 6.4 Prompt reutilizavel

```md
---
description: "Generate unit tests for a selected backend class"
name: "Gerar testes unitarios"
agent: "agent"
argument-hint: "Classe ou arquivo alvo"
---
Gere testes unitarios para o codigo selecionado.

Requisitos:

- cobrir fluxo feliz
- cobrir erros e bordas
- seguir padrao do projeto
- evitar mocks desnecessarios
```

### 6.5 Skill especializada

```md
---
name: architecture-audit
description: "Audit project architecture, identify boundaries, dependencies, anti-patterns, and documentation gaps"
---
# Architecture Audit

## When to Use

- Revisar estrutura do projeto
- Validar aderencia arquitetural
- Produzir visao de componentes

## Procedure

1. Ler a spec ou ADRs relevantes
2. Identificar camadas e dependencias
3. Verificar violacoes de boundary
4. Gerar resumo com riscos e recomendacoes

## References

Use os arquivos em ./references/ para checklist e heuristicas.
```

### 6.6 Agent especializado

```md
---
description: "Use for architecture reviews, dependency boundaries, technical design analysis, and documentation structure"
tools: [read, search]
user-invocable: true
---
You are an architecture specialist.

## Constraints

- Do not edit files
- Do not propose generic advice without evidence
- Focus on boundaries, coupling, and maintainability

## Output

Return:

1. architecture summary
2. risks
3. missing docs
4. recommended next actions
```

---

## 7. Como economizar tokens e aumentar precisao

Esse e o ponto central de eficiencia.

A regra de ouro e separar por frequencia de uso.

### Coloque em `spec`

- objetivo
- regra de negocio
- criterio de aceite
- restricoes funcionais

### Coloque em `copilot-instructions.md`

- convencoes universais do projeto
- arquitetura de alto nivel
- comandos essenciais
- padroes que valem quase sempre

### Coloque em `*.instructions.md`

- regras por stack
- regras por pasta
- convencoes especificas por tipo de arquivo
- detalhes que nao precisam estar sempre carregados

### Coloque em `*.prompt.md`

- tarefa operacional especifica
- formato de saida esperado
- parametros variaveis

### Coloque em `SKILL.md`

- workflow
- checklist
- templates
- referencias
- materiais auxiliares

### Coloque em `*.agent.md`

- papel
- limites
- ferramentas
- comportamento desejado

A logica e:

- contexto universal: curto e sempre relevante
- contexto especifico: sob demanda
- procedimento complexo: skill
- requisito de negocio: spec
- tarefa pontual: prompt

---

## 8. Erros mais comuns

### Colocar tudo em um arquivo so

Isso dilui as instrucoes importantes e desperdica contexto.

### Transformar instructions em documentacao enciclopedica

Instructions devem orientar acao, nao substituir documentacao geral.

### Usar regras globais para temas especificos

Se so vale para certos arquivos, use `*.instructions.md` em vez de regra global.

### Criar prompt para workflow complexo

Se tem multiplas etapas, checklist e referencias, provavelmente deveria ser skill.

### Misturar regra de negocio com padrao tecnico

Regra de negocio vai para a spec.
Padrao tecnico vai para instructions.

### Criar agent sem necessidade

Se um prompt ou skill resolve, nao complique com agent.

---

## 9. O que ja vem "pre-configurado" ao abrir a IDE

Ao abrir a IDE, o Copilot ja opera com uma base de contexto do proprio produto:

- instrucoes internas do modo atual
- politicas e comportamento do agente
- ferramentas disponiveis
- modelo selecionado
- historico e contexto da conversa

Alem disso, se existirem customizacoes no projeto ou no perfil do usuario, elas entram conforme o tipo:

- `copilot-instructions.md` ou `AGENTS.md`: regras gerais
- `*.instructions.md`: entram por relevancia ou por padrao de arquivo
- `*.prompt.md`: aparecem como tarefas reutilizaveis
- `SKILL.md`: podem ser descobertas e carregadas sob demanda
- `*.agent.md`: podem ser escolhidos manualmente ou acionados por outros agentes

---

## 10. Escopo global vs escopo do projeto

Existem dois grandes escopos:

### Escopo do projeto

Fica no workspace, geralmente em `.github/`, e e compartilhado com o time.

Use para:

- padroes do projeto
- arquitetura
- stack do time
- prompts uteis para todos
- skills compartilhadas

### Escopo global do usuario

Fica no perfil local do VS Code ou em diretorios pessoais.

Use para:

- preferencias pessoais
- estilo de resposta
- padroes pessoais de revisao
- prompts de uso geral
- agents pessoais

Exemplos tipicos de caminhos em Windows:

```text
%APPDATA%\Code\User\instructions\
%APPDATA%\Code\User\prompts\
%APPDATA%\Code\User\agents\
%USERPROFILE%\.copilot\skills\
%USERPROFILE%\.agents\skills\
%USERPROFILE%\.claude\skills\
```

Resumo:

- o que e do time: projeto
- o que e sua preferencia pessoal: global

---

## 11. Estrategia profissional de adocao

Se voce quiser implantar uso maduro do Copilot em um time, siga esta ordem:

1. criar `docs/specs/`
2. criar `.github/copilot-instructions.md`
3. criar `*.instructions.md` por stack principal
4. criar prompts para tarefas repetidas
5. criar 1 ou 2 skills para workflows caros
6. criar agents so quando houver necessidade real de especializacao

Essa ordem evita exagero de complexidade logo no inicio.

---

## 12. Kit minimo recomendado

Se voce for comecar do jeito certo, um bom kit inicial seria:

```text
.github/
├─ copilot-instructions.md
├─ instructions/
│  ├─ backend.instructions.md
│  ├─ frontend.instructions.md
│  └─ testing.instructions.md
├─ prompts/
│  ├─ gerar-testes.prompt.md
│  ├─ revisar-codigo.prompt.md
│  └─ documentar-feature.prompt.md
└─ skills/
   └─ architecture-audit/
      ├─ SKILL.md
      └─ references/
         └─ checklist.md

docs/
├─ specs/
│  └─ feature-x.md
└─ adr/
   └─ 0001-architecture.md
```

Esse kit ja oferece uma base forte para usar o Copilot com muito mais qualidade.

---

## 13. Heuristica final de decisao

Use esta regua mental:

- se a informacao define o produto: `spec`
- se a informacao define a forma de trabalhar: `instructions`
- se a informacao define uma acao reutilizavel: `prompt`
- se a informacao define um procedimento especializado: `skill`
- se a informacao define um especialista operacional: `agent`

---

## Conclusao

O uso mais poderoso do GitHub Copilot vem de uma arquitetura de contexto bem desenhada.

A `spec` da clareza de negocio.
As `instructions` reduzem ambiguidade recorrente.
Os `prompts` aceleram tarefas repetidas.
As `skills` transformam workflows complexos em ativos reutilizaveis.
Os `agents` especializam comportamento e controle.

Se esses elementos forem bem estruturados, o Copilot deixa de ser apenas um assistente de autocomplete e passa a funcionar como uma camada real de aceleracao de engenharia.
