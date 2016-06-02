package eu.europa.ec.fisheries.uvms.spatial.entity.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi", imports = ProjectionEntity.class)
public abstract class ReportConnectSpatialMapper {

    public static ReportConnectSpatialMapper INSTANCE = Mappers.getMapper(ReportConnectSpatialMapper.class);

    @Mappings({
            @Mapping(source = "scaleBarUnits", target = "scaleBarType"),
            @Mapping(source = "coordinatesFormat", target = "displayFormatType"),
            @Mapping(target = "styleSettings", ignore = true),
            @Mapping(target = "visibilitySettings", ignore = true),
            @Mapping(target = "referenceData", ignore = true)
    })
    public abstract ReportConnectSpatialEntity mapConfigurationTypeToReportConnectSpatialEntity(MapConfigurationType map);

    @Mappings({
            @Mapping(source = "id", target = "spatialConnectId"),
            @Mapping(source = "projectionByMapProjId.id", target = "mapProjectionId"),
            @Mapping(source = "projectionByDisplayProjId.id", target = "displayProjectionId"),
            @Mapping(source = "displayFormatType", target = "coordinatesFormat"),
            @Mapping(source = "scaleBarType", target = "scaleBarUnits"),
            @Mapping(target = "styleSettings", ignore = true),
            @Mapping(target = "visibilitySettings", ignore = true),
            @Mapping(target = "layerSettings", ignore = true),
            @Mapping(target = "referenceDatas", ignore = true)
    })
    public abstract MapConfigurationType reportConnectSpatialEntityToMapConfigurationType(ReportConnectSpatialEntity entity);

}
