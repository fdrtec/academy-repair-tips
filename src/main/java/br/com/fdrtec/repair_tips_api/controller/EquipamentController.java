package br.com.fdrtec.repair_tips_api.controller;

import br.com.fdrtec.repair_tips_api.dto.EquipamentDto;
import br.com.fdrtec.repair_tips_api.service.EquipamentService;
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
@RequestMapping("/api/equipaments")
@Tag(name = "Equipaments", description = "Operações de equipamentos")
@RequiredArgsConstructor
public class EquipamentController {

    private final EquipamentService service;

    @PostMapping
    @Operation(operationId = "createEquipament", summary = "Cria um equipamento")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Equipamento criado", headers = {
            @Header(name = "Location", description = "URI do equipamento criado", schema = @Schema(type = "string", format = "uri"))
        }),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Peça associada não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<EquipamentDto> create(@Valid @RequestBody EquipamentDto dto) {
        EquipamentDto response = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.id())
            .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @Operation(operationId = "findEquipamentById", summary = "Busca um equipamento pelo identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Equipamento encontrado"),
        @ApiResponse(responseCode = "404", description = "Equipamento não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<EquipamentDto> findById(
        @Parameter(in = ParameterIn.PATH, description = "Identificador do equipamento", example = "1", required = true)
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @Operation(operationId = "listEquipaments", summary = "Lista equipamentos com paginação")
    @ApiResponse(responseCode = "200", description = "Página de equipamentos")
    public ResponseEntity<Page<EquipamentDto>> findAll(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PutMapping("/{id}")
    @Operation(operationId = "updateEquipament", summary = "Atualiza um equipamento")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Equipamento atualizado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Equipamento ou peça associada não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<EquipamentDto> update(
        @Parameter(in = ParameterIn.PATH, description = "Identificador do equipamento", example = "1", required = true)
        @PathVariable Long id,
        @Valid @RequestBody EquipamentDto dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "deleteEquipament", summary = "Remove um equipamento")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Equipamento removido"),
        @ApiResponse(responseCode = "404", description = "Equipamento não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<Void> delete(
        @Parameter(in = ParameterIn.PATH, description = "Identificador do equipamento", example = "1", required = true)
        @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}