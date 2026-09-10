package br.com.fdrtec.repair_tips_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Part", description = "Dados e representação de uma peça")
public record PartDto(
    @Schema(description = "Identificador da peça; não informado na criação", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    Long id,
    @Schema(description = "Nome da peça", example = "Air filter", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Name is required") String name,
    @Schema(description = "Número ou código da peça", example = "12345", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Number is required") String number
) {

    public PartDto(String name, String number) {
        this(null, name, number);
    }
}
