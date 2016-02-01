package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProjectionMapper {

    ProjectionMapper INSTANCE = Mappers.getMapper(ProjectionMapper.class);

    @Mappings({
            @Mapping(source = "srsCode", target = "epsgCode"),
            @Mapping(source = "isWorld", target = "global")
    })
    ProjectionDto projectionEntityToProjectionDto(ProjectionEntity projectionEntity);

    List<ProjectionDto> projectionEntityListToProjectionDtoList(List<ProjectionEntity> projectionEntityList);
}