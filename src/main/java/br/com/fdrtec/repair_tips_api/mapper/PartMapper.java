package br.com.fdrtec.repair_tips_api.mapper;

import br.com.fdrtec.repair_tips_api.dto.PartRequest;
import br.com.fdrtec.repair_tips_api.dto.PartResponse;
import br.com.fdrtec.repair_tips_api.entity.Part;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PartMapper {

    PartMapper INSTANCE = Mappers.getMapper(PartMapper.class);

    @Mapping(source = "id", target = "id")
    PartResponse toResponse(Part part);

    @Mapping(target = "id", ignore = true)
    Part toEntity(PartRequest request);

    @Mapping(target = "id", ignore = true)
    void updateFromRequest(PartRequest request, @MappingTarget Part part);
}
