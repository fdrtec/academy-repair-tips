package br.com.fdrtec.repair_tips_api.mapper;

import br.com.fdrtec.repair_tips_api.dto.PartDto;
import br.com.fdrtec.repair_tips_api.entity.Part;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface PartMapper {

    PartMapper INSTANCE = Mappers.getMapper(PartMapper.class);

    @Mapping(source = "id", target = "id")
    PartDto toDto(Part part);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    Part toEntity(PartDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateFromDto(PartDto dto, @MappingTarget Part part);
}
