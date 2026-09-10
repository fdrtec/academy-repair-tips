package br.com.fdrtec.repair_tips_api.mapper;

import br.com.fdrtec.repair_tips_api.dto.EquipamentDto;
import br.com.fdrtec.repair_tips_api.entity.Equipament;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = PartMapper.class)
public interface EquipamentMapper {

    @Mapping(source = "parts", target = "parts")
    EquipamentDto toDto(Equipament equipament);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parts", ignore = true)
    Equipament toEntity(EquipamentDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parts", ignore = true)
    void updateFromDto(EquipamentDto dto, @MappingTarget Equipament equipament);
}