# Practical GitHub Copilot Guide

## How to use specs, instructions, prompts, skills, and agents effectively

This guide shows how to structure GitHub Copilot usage professionally, predictably, and efficiently in terms of context. The central idea is simple: AI quality depends less on "magic prompts" and more on good context architecture.

## Overview

To use Copilot's full power, think in five layers:

- `spec`: defines what needs to be built
- `instructions`: define how the work should be done
- `prompt`: starts a specific task
- `skill`: encapsulates a specialized workflow
- `agent`: defines a specialist with its own behavior and tools

When these five layers are well organized, Copilot becomes more precise, uses fewer tokens, and produces more consistent answers.

---

## 1. What each concept means

### Spec

`spec` is generally not an official Copilot primitive like `prompt`, `skill`, or `instructions`. It is an engineering artifact: a functional, technical, or business specification.

A spec answers:

- what must be done
- why it must be done
- which rules must be followed
- which acceptance criteria define success

Examples of specs:

- functional requirement
- RFC
- ADR
- OpenAPI contract
- acceptance checklist
- business workflow

Example structure:

```md
# Feature Spec: Customer Registration

## Objective

Allow individual customer registration with CPF validation and idempotency.

## Rules

- CPF must be valid
- Email must be unique
- A repeated request with the same idempotency key must not duplicate the record

## Acceptance Criteria

- Return 201 for the first registration
- Return 200 for an idempotent retry
- Return 409 when the email already belongs to another identity
```

### Instructions

`instructions` are work rules. They do not define the business requirement; they define how the agent should operate in that context.

They are useful for:

- architectural patterns
- code conventions
- testing rules
- documentation patterns
- build commands
- technical constraints

There are two main types:

- `copilot-instructions.md` or `AGENTS.md`: general project rules
- `*.instructions.md`: rules specific to a language, folder, stack, or task type

### Prompt

A `prompt` is a reusable task. It is used to perform something specific and recurring.

Examples:

- generate tests
- review code
- document a feature
- create a Mermaid diagram
- investigate a dependency conflict

A good prompt focuses on one task.

### Skill

A `skill` is a specialized workflow. It is better than a prompt when the task has several steps, supporting material, templates, scripts, or references.

Examples:

- architecture audit
- incident diagnosis
- dependency analysis
- release preparation
- standardized technical documentation

### Agent

An `agent` is a configured specialist. It defines:

- role
- boundaries
- allowed tools
- execution style
- possible handoffs

Use an agent when the problem involves not only the task, but also the desired behavior of its executor.

---

## 2. When to use each one

Use `spec` when AI needs to understand the domain and success criteria.

Use `instructions` when you want to reduce recurring ambiguity in the project.

Use `prompt` when the task is repeatable and has a clear beginning and end.

Use `skill` when there is a specialized process with more than one step.

Use `agent` when you want to isolate behavior, tools, and responsibility.

Quick summary:

- If it answers "what to build": `spec`
- If it answers "how we work here": `instructions`
- If it answers "perform this task now": `prompt`
- If it answers "follow this workflow": `skill`
- If it answers "which specialist executes": `agent`

---

## 3. How to name specs so Copilot understands them better

Copilot does not have an official file type called `spec`. It understands specifications as well-structured textual context. Therefore, the best strategy is to use clear and predictable names.

The most recognizable terms tend to be:

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

Good file-naming conventions:

- `docs/specs/customer-registration.md`
- `docs/specs/feature-customer-registration.md`
- `docs/requirements/customer-registration.md`
- `docs/adr/0001-architecture.md`
- `docs/rfc/rfc-new-flow.md`
- `docs/api/openapi.yaml`

If you need one standard name for the team, `Feature Spec` is usually the best balance between functional clarity and recognition by AI.

Recommended example:

```md
# Feature Spec: Customer Registration

## Objective
## Context
## Business Rules
## Technical Constraints
## Acceptance Criteria
## Out of Scope
```

To increase the chance that Copilot pulls this context into customizations, use these terms in prompt, instruction, and skill descriptions as well.

Example of an effective description:

```yaml
description: "Use when implementing a feature from a spec, requirements document, ADR, or acceptance criteria"
```

---

## 4. Ideal workspace structure

A mature structure for projects using GitHub Copilot can be:

```text
my-project/
|- .github/
|  |- copilot-instructions.md
|  |- instructions/
|  |  |- backend.instructions.md
|  |  |- frontend.instructions.md
|  |  `- testing.instructions.md
|  |- prompts/
|  |  |- generate-tests.prompt.md
|  |  |- review-code.prompt.md
|  |  `- document-feature.prompt.md
|  |- agents/
|  |  |- reviewer.agent.md
|  |  `- architect.agent.md
|  `- skills/
|     |- architecture-audit/
|     |  |- SKILL.md
|     |  |- references/
|     |  |  |- checklist.md
|     |  |  `- heuristics.md
|     |  `- assets/
|     |     `- topology-template.md
|     `- release-readiness/
|        |- SKILL.md
|        `- references/
|           `- gates.md
|- docs/
|  |- specs/
|  |  |- feature-x.md
|  |  `- billing-reconciliation.md
|  |- adr/
|  |  |- 0001-architecture.md
|  |  `- 0002-observability.md
|  `- api/
|     `- openapi.yaml
`- src/
```

This organization separates the requirement, rule, task, workflow, and specialist.

---

## 5. How these layers combine

Copilot works best when context is assembled in layers.

Conceptual order:

1. IDE and current-mode base context
2. global project instructions
3. file-, folder-, or stack-specific instructions
4. selected agent
5. executed prompt
6. on-demand skill
7. attached or referenced spec

In practice:

- the `spec` describes the problem
- `instructions` constrain the implementation approach
- the `prompt` says what to execute now
- the `skill` guides a procedure
- the `agent` controls executor behavior

---

## 6. Complete assembly example

Suppose you have a Java backend with REST APIs, tests, and documentation.

### 6.1 Spec

```md
# Feature Spec: Customer Registration

## Objective

Allow individual customer registration with CPF validation and idempotency.

## Rules

- CPF must be valid
- Email must be unique
- A repeated request with the same idempotency key must not duplicate the record

## Acceptance Criteria

- Return 201 for the first registration
- Return 200 with the same payload for an idempotent retry
- Return 409 when the email already belongs to another identity
```

### 6.2 Global project instruction

```md
# Project Guidelines

## Architecture

Use hexagonal architecture.
Adapters must not contain business rules.

## Build and Test

Before the full suite, prefer focused tests for the changed slice.

## Conventions

Prefer constructor injection.
Avoid logic in controllers.
```

### 6.3 Backend-specific instruction

```md
---
description: "Use when editing backend Java production code or unit tests"
applyTo: "src/main/java/**, src/test/java/**"
---
# Backend Java Rules

- Use AssertJ in tests
- Prefer descriptive test names
- Application services must not access infrastructure directly
- Handle nulls explicitly at boundaries
```

### 6.4 Reusable prompt

```md
---
description: "Generate unit tests for a selected backend class"
name: "Generate unit tests"
agent: "agent"
argument-hint: "Target class or file"
---
Generate unit tests for the selected code.

Requirements:

- cover the happy path
- cover errors and edge cases
- follow the project pattern
- avoid unnecessary mocks
```

### 6.5 Specialized skill

```md
---
name: architecture-audit
description: "Audit project architecture, identify boundaries, dependencies, anti-patterns, and documentation gaps"
---
# Architecture Audit

## When to Use

- Review project structure
- Validate architectural compliance
- Produce a component view

## Procedure

1. Read the relevant specs or ADRs
2. Identify layers and dependencies
3. Check for boundary violations
4. Generate a summary with risks and recommendations

## References

Use the files in ./references/ for checklists and heuristics.
```

### 6.6 Specialized agent

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
3. missing documentation
4. recommended next actions
```

---

## 7. How to save tokens and increase precision

This is the central efficiency point.

The rule of thumb is to separate content by usage frequency.

### Put in `spec`

- objective
- business rule
- acceptance criterion
- functional constraints

### Put in `copilot-instructions.md`

- universal project conventions
- high-level architecture
- essential commands
- patterns that apply almost always

### Put in `*.instructions.md`

- stack-specific rules
- folder-specific rules
- conventions for specific file types
- details that do not need to be loaded all the time

### Put in `*.prompt.md`

- specific operational task
- expected output format
- variable parameters

### Put in `SKILL.md`

- workflow
- checklist
- templates
- references
- supporting material

### Put in `*.agent.md`

- role
- boundaries
- tools
- desired behavior

The logic is:

- universal context: short and always relevant
- specific context: on demand
- complex procedure: skill
- business requirement: spec
- one-off task: prompt

---

## 8. Common mistakes

### Putting everything in one file

This dilutes important instructions and wastes context.

### Turning instructions into encyclopedic documentation

Instructions should guide action, not replace general documentation.

### Using global rules for specific topics

If a rule applies only to certain files, use `*.instructions.md` instead of a global rule.

### Creating a prompt for a complex workflow

If it has multiple steps, a checklist, and references, it should probably be a skill.

### Mixing business rules with technical patterns

Business rules belong in the spec.
Technical patterns belong in instructions.

### Creating an agent unnecessarily

If a prompt or skill solves the problem, do not complicate it with an agent.

---

## 9. What comes preconfigured when opening the IDE

When the IDE opens, Copilot already operates with a context base from the product itself:

- internal instructions for the current mode
- agent policies and behavior
- available tools
- selected model
- conversation history and context

In addition, if project or user-profile customizations exist, they are included according to their type:

- `copilot-instructions.md` or `AGENTS.md`: general rules
- `*.instructions.md`: included by relevance or file pattern
- `*.prompt.md`: appear as reusable tasks
- `SKILL.md`: can be discovered and loaded on demand
- `*.agent.md`: can be selected manually or triggered by other agents

---

## 10. Global scope vs. project scope

There are two major scopes:

### Project scope

It lives in the workspace, usually in `.github/`, and is shared with the team.

Use it for:

- project patterns
- architecture
- team stack
- useful prompts for everyone
- shared skills

### User global scope

It lives in the local VS Code profile or personal directories.

Use it for:

- personal preferences
- response style
- personal review patterns
- general-purpose prompts
- personal agents

Typical path examples on Windows:

```text
%APPDATA%\Code\User\instructions\
%APPDATA%\Code\User\prompts\
%APPDATA%\Code\User\agents\
%USERPROFILE%\.copilot\skills\
%USERPROFILE%\.agents\skills\
%USERPROFILE%\.claude\skills\
```

Summary:

- team-owned content: project scope
- personal preferences: global scope

---

## 11. Professional adoption strategy

To introduce mature Copilot usage to a team, follow this order:

1. create `docs/specs/`
2. create `.github/copilot-instructions.md`
3. create `*.instructions.md` for each main stack
4. create prompts for repeated tasks
5. create one or two skills for expensive workflows
6. create agents only when there is a real need for specialization

This order avoids unnecessary complexity at the beginning.

---

## 12. Recommended minimum kit

If you are starting the right way, a good initial kit would be:

```text
.github/
|- copilot-instructions.md
|- instructions/
|  |- backend.instructions.md
|  |- frontend.instructions.md
|  `- testing.instructions.md
|- prompts/
|  |- generate-tests.prompt.md
|  |- review-code.prompt.md
|  `- document-feature.prompt.md
`- skills/
   `- architecture-audit/
      |- SKILL.md
      `- references/
         `- checklist.md

docs/
|- specs/
|  `- feature-x.md
`- adr/
   `- 0001-architecture.md
```

This kit already provides a strong foundation for using Copilot with much higher quality.

---

## 13. Final decision heuristic

Use this mental guide:

- if the information defines the product: `spec`
- if the information defines how to work: `instructions`
- if the information defines a reusable action: `prompt`
- if the information defines a specialized procedure: `skill`
- if the information defines an operational specialist: `agent`

---

## Conclusion

The most powerful use of GitHub Copilot comes from a well-designed context architecture.

The `spec` provides business clarity.
`Instructions` reduce recurring ambiguity.
`Prompts` accelerate repeated tasks.
