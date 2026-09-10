package br.com.fdrtec.repair_tips_api.controller;

import br.com.fdrtec.repair_tips_api.dto.PartDto;
import br.com.fdrtec.repair_tips_api.service.PartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/parts")
@Tag(name = "Parts", description = "Operações de peças")
@RequiredArgsConstructor
public class PartController {

    private final PartService service;

    @PostMapping
    @Operation(operationId = "createPart", summary = "Cria uma peça")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Peça criada", headers = {
            @Header(name = "Location", description = "URI da peça criada", schema = @Schema(type = "string", format = "uri"))
        }),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<PartDto> create(@Valid @RequestBody PartDto dto) {
        PartDto response = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.id())
            .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @Operation(operationId = "findPartById", summary = "Busca uma peça pelo identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Peça encontrada"),
        @ApiResponse(responseCode = "404", description = "Peça não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<PartDto> findById(
        @Parameter(in = ParameterIn.PATH, description = "Identificador da peça", example = "1", required = true)
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @Operation(operationId = "listParts", summary = "Lista peças com paginação")
    @ApiResponse(responseCode = "200", description = "Página de peças")
    public ResponseEntity<Page<PartDto>> findAll(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PutMapping("/{id}")
    @Operation(operationId = "updatePart", summary = "Atualiza uma peça")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Peça atualizada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Peça não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<PartDto> update(
        @Parameter(in = ParameterIn.PATH, description = "Identificador da peça", example = "1", required = true)
        @PathVariable Long id,
        @Valid @RequestBody PartDto dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "deletePart", summary = "Remove uma peça")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Peça removida"),
        @ApiResponse(responseCode = "404", description = "Peça não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<Void> delete(
        @Parameter(in = ParameterIn.PATH, description = "Identificador da peça", example = "1", required = true)
        @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
