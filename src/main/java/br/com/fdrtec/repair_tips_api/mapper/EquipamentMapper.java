package br.com.fdrtec.repair_tips_api.mapper;

import br.com.fdrtec.repair_tips_api.dto.EquipamentDto;
import br.com.fdrtec.repair_tips_api.entity.Equipament;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = PartMapper.class, builder = @Builder(disableBuilder = true))
public interface EquipamentMapper {

    @Mapping(source = "parts", target = "parts")
    @Mapping(target = "partIds", ignore = true)
    EquipamentDto toDto(Equipament equipament);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "parts", ignore = true)
    Equipament toEntity(EquipamentDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "parts", ignore = true)
    void updateFromDto(EquipamentDto dto, @MappingTarget Equipament equipament);
}