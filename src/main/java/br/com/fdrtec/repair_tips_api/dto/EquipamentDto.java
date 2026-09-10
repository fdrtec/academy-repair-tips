package br.com.fdrtec.repair_tips_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(name = "Equipament", description = "Dados e representação de um equipamento")
public record EquipamentDto(
    @Schema(description = "Identificador do equipamento; não informado na criação", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    Long id,
    @Schema(description = "Nome do equipamento", example = "HP LaserJet Pro M404dn", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Name is required") String name,
    @Schema(description = "Marca do equipamento", example = "HP", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Brand is required") String brand,
    @Schema(description = "Categoria do equipamento", example = "PRINTER", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Category is required") String category,
    @Schema(description = "Tipo do equipamento", example = "LASER", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Type is required") String type,
    @Schema(description = "Identificadores das peças compatíveis; usado na criação e atualização", example = "[1, 2]", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.WRITE_ONLY)
    @NotNull(message = "Part ids are required") List<Long> partIds,
    @Schema(description = "Peças compatíveis; preenchido nas respostas", accessMode = Schema.AccessMode.READ_ONLY)
    List<PartDto> parts
) {

    public EquipamentDto(String name, String brand, String category, String type, List<Long> partIds) {
        this(null, name, brand, category, type, partIds, null);
    }
}
