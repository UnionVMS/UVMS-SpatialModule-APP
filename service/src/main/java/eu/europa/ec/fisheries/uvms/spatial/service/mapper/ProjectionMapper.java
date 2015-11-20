package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.Projection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface ProjectionMapper {
    ProjectionMapper INSTANCE = Mappers.getMapper(ProjectionMapper.class);

    @Mappings({
            @Mapping(source = "isSystemWide", target = "global"),
            @Mapping(source = "srsCode", target = "epsgCode"),
    })
    Projection projectionEntityToProjectionDto(ProjectionEntity projectionEntity);
}
