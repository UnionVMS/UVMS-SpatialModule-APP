package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.ConfigurationDto;

import java.util.List;

public interface MapConfigService {

    MapConfigDto getReportConfig(int reportId, String userPreferences, String adminPreferences);

    MapConfigurationType getMapConfigurationType(final Long reportId) throws ServiceException;

    SpatialGetMapConfigurationRS getMapConfiguration(SpatialGetMapConfigurationRQ mapConfigurationRQ) throws ServiceException;

    SpatialSaveOrUpdateMapConfigurationRS handleSaveOrUpdateSpatialMapConfiguration(SpatialSaveOrUpdateMapConfigurationRQ spatialSaveMapConfigurationRQ);

    List<ProjectionDto> getAllProjections();

    void handleDeleteMapConfiguration(SpatialDeleteMapConfigurationRQ spatialDeleteMapConfigurationRQ) throws ServiceException;

    ConfigurationDto retrieveAdminConfiguration(String config);

    String saveAdminJson(ConfigurationDto configurationDto, String defaultConfig);

    ConfigurationDto retrieveUserConfiguration(String config, String defaultConfig);

    String saveUserJson(ConfigurationDto configurationDto, String userPref);

    String resetUserJson(ConfigurationDto configurationDto, String userPref);
}
