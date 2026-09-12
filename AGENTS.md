# Football Manager — Project Agent Guide

## 1. Project Overview

This is a Football Manager simulation game built with Spring Boot 3.3.4, Kotlin 2.1.21, Java 21. The codebase is organized into multiple independent domain modules that evolve together.

## 2. Project Modules

The codebase is organized into multiple independent domain modules, each with its own bounded context. Module boundaries are defined by business requirements and approved during design review.

Each module lives under `docs/modules/<module>/` with:
- `<module>.md` — module overview, bounded context, aggregates, repository interfaces, design decisions
- `<feature>.md` — feature-level design under the module

Cross-module architecture, conventions, and agreed decisions live in `docs/architecture.md`.

Module names and boundaries are blank until business requirements are provided, designed, and approved.

## 3. Workflow

The development workflow for every feature is strictly:

```
STEP 1. Business Requirements
        → Developer provides business requirements (one or a batch)
        ↓
STEP 2. Design Proposal
        → Agent analyzes and proposes:
          - System design (entities, boundaries, dependencies)
          - API contracts (if applicable)
          - Architectural alternatives with trade-offs
        ↓
STEP 3. Approval / Iteration
        → Developer approves or requests adjustments
        → Iteration repeats until approved
        ↓
STEP 4. Documentation
        → Agent writes final design into `docs/modules/<module>/<feature>.md`
        ↓
STEP 5. Implementation
        → Agent implements according to documentation:
          - Domain layer (entities, value objects, domain events, repository interfaces)
          - Application layer (use cases, DTOs, application services)
          - Infrastructure layer (JPA repositories, external adapters, configs)
          - Tests (unit + integration as appropriate)
        → Fail-fast Halts if compilation fails
```

## 4. Code Quality Standards

All code must meet FAANG-level production bar:

### 4.1 After Clean Architecture
```
Domain (entities, value objects, domain events, repository interfaces)
  └── Application (use cases, DTOs, application services, ports)
       └── Infrastructure (JPA, cache, external API adapters)
            └── Presentation (controllers, views, serializers)
```
- Domain layer has ZERO external dependencies. No frameworks, no ORM, no HTTP
- Application layer depends only on Domain (via interfaces/ports)
- Infrastructure implements ports defined in Application or Domain
- Presentation depends only on Application (via DTOs/interfaces)

### 4.2 Function Standards
- Single responsibility: each function does EXACTLY one thing
- Max ~30 lines per function (shorter is better)
- Max 3 parameters (use an options object for more)
- Pure functions where possible (no side effects, same input → same output)
- No boolean parameters — they violate SRP. Split into two methods

### 4.3 Naming & Structure
- Classes are nouns, methods are verbs, booleans are predicates (`isActive`, `hasAccess`)
- One file = one primary concept (class/interface/type)
- Folders map to bounded contexts or modules, not technical layers
- Avoid abbreviations except universally known ones (HTTP, DB, ID)

### 4.4 Error Handling
- NEVER return `null` or `undefined` from public APIs. Use `Optional<T>`
- NEVER silently catch exceptions. Every catch must log and/or rethrow
- Use Result type (`Ok<T>` / `Err<E>`) for expected failures
- Use exceptions only for unexpected failures (programming errors)

### 4.5 Design Patterns
- Use **Ports & Adapters** (Hexagonal Architecture) for all I/O boundaries
- Use **Aggregate** pattern with clear consistency boundaries
- Use **Value Objects** for concepts with structural equality
- Use **Domain Events** for cross-aggregate communication
- **Repositories** per Aggregate Root, never per database table
- Prefer **composition over inheritance** in all cases
- Prefer **immutability** by default. Mutate state only when necessary and explicit

## 5. Documentation Rules

- Module documentation lives in `docs/modules/<module>/`
- Each feature gets its own file: `<feature>.md`
- Module overview file (`<module>.md`) contains:
  - Module bounded context and responsibilities
  - Domain entities and aggregates
  - Repository interfaces (ports)
  - API contracts (if public)
  - Design decisions and trade-offs
- Architectural decisions, cross-module agreements, and conventions live in `docs/architecture.md`
- Documentation MUST be updated before implementation starts (Step 4 of the workflow)
- ALL agents and developers MUST read `AGENTS.md` on first access. This is the single source of truth for project conventions.

## 6. Agent Requirements (FAANG Bar)

Before writing code, you MUST:
1. Read relevant existing code — understand architecture and conventions
2. Challenge the proposal — does it degrade the architecture? Introduce tech debt?
3. Propose 2+ alternatives with explicit trade-offs (never a single answer)
4. Would you approve this PR if a peer submitted it at Google/Amazon/Meta?
5. If no — reject with specific file/line references and suggestions

Every code delivery must include:
`Risks: ... | Alternatives: ... | Trade-offs: ...`
