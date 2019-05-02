/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ReportConnectSpatialEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(imports = ProjectionEntity.class)
public interface ReportConnectSpatialMapper {

    ReportConnectSpatialMapper INSTANCE = Mappers.getMapper(ReportConnectSpatialMapper.class);

    @Mappings({
            @Mapping(source = "scaleBarUnits", target = "scaleBarType"),
            @Mapping(source = "coordinatesFormat", target = "displayFormatType"),
            @Mapping(target = "styleSettings", ignore = true),
            @Mapping(target = "visibilitySettings", ignore = true),
            @Mapping(target = "referenceData", ignore = true)
    })
    ReportConnectSpatialEntity mapConfigurationTypeToReportConnectSpatialEntity(MapConfigurationType map);

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
    MapConfigurationType reportConnectSpatialEntityToMapConfigurationType(ReportConnectSpatialEntity entity);

}