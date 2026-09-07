package br.com.fdrtec.repair_tips_api.dto;

import jakarta.validation.constraints.NotBlank;

public record PartRequest(
    @NotBlank(message = "Name is required") String name,
    @NotBlank(message = "Number is required") String number
) {
}
