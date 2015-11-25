package eu.europa.ec.fisheries.uvms.spatial.entity.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * //TODO create test
 */
@Mapper(componentModel = "cdi", imports = ProjectionEntity.class)
public abstract class ReportConnectSpatialMapper {

    public static ReportConnectSpatialMapper INSTANCE = Mappers.getMapper(ReportConnectSpatialMapper.class);

    @Mappings({
            @Mapping(source = "scaleBarUnits", target = "scaleBarType"),
            @Mapping(source = "coordinatesFormat", target = "displayFormatType"),
            @Mapping(target = "projectionByMapProjId", expression = "java(createProjection(map.getMapProjectionId()))"),
            @Mapping(target = "projectionByDisplayProjId", expression = "java(createProjection(map.getDisplayProjectionId()))")
    })
    public abstract ReportConnectSpatialEntity mapConfigurationTypeToReportConnectSpatialEntity(MapConfigurationType map);

    ProjectionEntity createProjection(Long id) {

        ProjectionEntity entity = null;

        if (id != null) {
            entity = new ProjectionEntity(id);
        }

        return entity;
    }
}
