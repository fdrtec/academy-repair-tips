package br.com.fdrtec.repair_tips_api.mapper;

import br.com.fdrtec.repair_tips_api.dto.PecaRequest;
import br.com.fdrtec.repair_tips_api.dto.PecaResponse;
import br.com.fdrtec.repair_tips_api.entity.Peca;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PecaMapper {

    PecaMapper INSTANCE = Mappers.getMapper(PecaMapper.class);

    @Mapping(source = "id", target = "id")
    PecaResponse toResponse(Peca peca);

    @Mapping(target = "id", ignore = true)
    Peca toEntity(PecaRequest request);

    @Mapping(target = "id", ignore = true)
    void updateFromRequest(PecaRequest request, @MappingTarget Peca peca);
}
