# 01 - Part management

## Objective
Implement the complete creation, retrieval, update, and deletion flow for the "Part" object, including a paginated query.

## Scope
- Create the "Part" entity model with the following main attributes:
  - name
  - number
- Implement the entity's complete CRUD.
- Expose the endpoints through a REST API.
- Persist data in an H2 database for the development environment.

## Functional requirements
- [ ] Create a new part.
- [ ] Retrieve a part by identifier.
- [ ] List parts with pagination.
- [ ] Update an existing part.
- [ ] Delete a part.

## Technical requirements
- Structure the API in layers: controller, service, and repository.
- Follow an API-first workflow with a clear input and output contract.
- Use OpenAPI/Swagger compatible with the project.
- Use records for DTOs.
- Use MapStruct to convert between DTOs and entities.
- Prefer a reflection-based approach to associate fields more generically with the JPA entity.
- Use Lombok to reduce boilerplate.
- Implement a global Spring exception handling layer using @ControllerAdvice and/or @RestControllerAdvice.
- Return standardized Problem Details responses, including status, title, detail, and relevant extensions.
- Handle, at a minimum, resource-not-found, input-validation, and unexpected-internal-error exceptions.
- Avoid comments in code.

## Acceptance criteria
- The CRUD endpoints are available and respond correctly.
- The parts query returns paginated results.
- The application persists data correctly in the H2 database.
- The project structure follows the proposed layers and keeps the code organized.

## Deliverables
- API controller.
- Service with business rules.
- JPA repository.
- Entity and DTOs.
- Basic H2 database configuration.
- File with cURL request examples for the endpoints.

## Request examples file
- Create a file named requests-parts.http or requests-parts.sh with cURL examples for all CRUD endpoints.
- The file must include examples for:
  - part creation
  - lookup by identifier
  - paginated listing
  - part update
  - part deletion
