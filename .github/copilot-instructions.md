# Project Guidelines

## Language
- Always respond and interact in Brazilian Portuguese.

## Architecture
- Treat the project as a Spring backend using Java 25.
- Maintain a clear layered structure: controller, service, and repository.
- Prefer small, focused classes and methods.
- Consistently apply SOLID principles and clean-code practices.
- Use design patterns only when they simplify the domain or improve maintainability.

## API Design
- Work in API-first mode: define the endpoint contract, request, and response DTOs before implementing the service and repository flow.
- Keep controllers lean and delegate business rules to services.
- Expose stable, explicit REST endpoints with clear input and output models.

## DTOs and Mapping
- Use records for DTOs when they fit the use case.
- Use MapStruct to map between DTOs and entities.
- Avoid manual mapping when MapStruct can express the transformation.
- Keep entity models separate from transport models.

## Code Style
- Prefer readable code that reveals intent over overly clever code.
- Keep business logic out of controllers and repositories.
- Name classes and methods after domain intent, not technical shortcuts.
- When adding code, remain consistent with the Spring conventions already present in the repository.

## Implementation Bias
- Prefer direct solutions that are easy to test.
- Introduce abstractions only when there is a real use case for them.
- When choosing a pattern, make the reason clear in the structure, not in comments.
