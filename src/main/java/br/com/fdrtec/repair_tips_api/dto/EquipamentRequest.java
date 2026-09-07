package br.com.fdrtec.repair_tips_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record EquipamentRequest(
    @NotBlank(message = "Name is required") String name,
    @NotBlank(message = "Brand is required") String brand,
    @NotBlank(message = "Category is required") String category,
    @NotBlank(message = "Type is required") String type,
    @NotNull(message = "Part ids are required") List<Long> partIds
) {
}