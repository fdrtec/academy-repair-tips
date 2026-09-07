package br.com.fdrtec.repair_tips_api.mapper;

import br.com.fdrtec.repair_tips_api.dto.EquipamentRequest;
import br.com.fdrtec.repair_tips_api.dto.EquipamentResponse;
import br.com.fdrtec.repair_tips_api.entity.Equipament;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = PartMapper.class)
public interface EquipamentMapper {

    @Mapping(source = "parts", target = "parts")
    EquipamentResponse toResponse(Equipament equipament);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parts", ignore = true)
    Equipament toEntity(EquipamentRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parts", ignore = true)
    void updateFromRequest(EquipamentRequest request, @MappingTarget Equipament equipament);
}