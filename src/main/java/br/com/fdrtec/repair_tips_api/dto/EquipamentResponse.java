package br.com.fdrtec.repair_tips_api.dto;

import java.util.List;

public record EquipamentResponse(
    Long id,
    String name,
    String brand,
    String category,
    String type,
    List<PartResponse> parts
) {
}