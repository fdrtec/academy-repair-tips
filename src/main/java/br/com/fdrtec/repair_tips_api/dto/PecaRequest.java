package br.com.fdrtec.repair_tips_api.dto;

import jakarta.validation.constraints.NotBlank;

public record PecaRequest(
    @NotBlank(message = "Nome é obrigatório") String nome,
    @NotBlank(message = "Número é obrigatório") String numero
) {
}
