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
            @Mapping(source = "spatialConnectId", target = "id"),
            @Mapping(source = "scaleBarUnits", target = "scaleBarType"),
            @Mapping(source = "coordinatesFormat", target = "displayFormatType"),
            @Mapping(target = "projectionByMapProjId", expression = "java(createProjection(map.getMapProjectionId()))"),
            @Mapping(target = "projectionByDisplayProjId", expression = "java(createProjection(map.getDisplayProjectionId()))")
    })
    public abstract ReportConnectSpatialEntity mapConfigurationTypeToReportConnectSpatialEntity(MapConfigurationType map);

    @Mappings({
            @Mapping(source = "id", target = "spatialConnectId"),
            @Mapping(source = "projectionByMapProjId.id", target = "mapProjectionId"),
            @Mapping(source = "projectionByDisplayProjId.id", target = "displayProjectionId"),
            @Mapping(source = "displayFormatType", target = "coordinatesFormat"),
            @Mapping(source = "scaleBarType", target = "scaleBarUnits"),
    })
    public abstract MapConfigurationType reportConnectSpatialEntityToMapConfigurationType(ReportConnectSpatialEntity entity);

    ProjectionEntity createProjection(Long id) {

        ProjectionEntity entity = null;

        if (id != null) {
            entity = new ProjectionEntity(id);
        }

        return entity;
    }
}