package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public abstract class ProjectionMapper {
    private static final ProjectionMapper INSTANCE = Mappers.getMapper(ProjectionMapper.class);

    @Mappings({
            @Mapping(source = "srsCode", target = "epsgCode")
    })
    public abstract ProjectionDto projectionEntityToProjectionDto(ProjectionEntity projectionEntity);

    public static ProjectionMapper mapper() {
        return INSTANCE;
    }
}
