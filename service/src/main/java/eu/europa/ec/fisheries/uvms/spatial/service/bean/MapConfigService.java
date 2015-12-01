package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;

import java.util.List;

public interface MapConfigService {

    MapConfigDto getReportConfig(int reportId);

    MapConfigurationType getMapConfigurationType(final Long reportId) throws ServiceException;

    SpatialGetMapConfigurationRS getMapConfiguration(SpatialGetMapConfigurationRQ mapConfigurationRQ) throws ServiceException;

    SpatialSaveOrUpdateMapConfigurationRS handleSpatialMapConfiguration(SpatialSaveOrUpdateMapConfigurationRQ spatialSaveMapConfigurationRQ);

    List<ProjectionDto> getAllProjections();

    void handleDeleteMapConfiguration(SpatialDeleteMapConfigurationRQ spatialDeleteMapConfigurationRQ) throws ServiceException;
}
