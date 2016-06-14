package eu.europa.ec.fisheries.uvms.spatial.service;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.ConfigurationDto;

import java.util.Collection;
import java.util.List;
import javax.ejb.Local;

@Local
public interface MapConfigService {

    MapConfigDto getReportConfig(int reportId, String userPreferences, String adminPreferences, String userName, String scopeName, String timeStamp, Collection<String> permittedServiceLayer);

    MapConfigDto getReportConfigWithoutSave(ConfigurationDto configurationDto, String userName, String scopeName, Collection<String> permittedServiceLayer);

    MapConfigurationType getMapConfigurationType(final Long reportId, Collection<String> permittedServiceLayers) throws ServiceException;

    MapConfigDto getBasicReportConfig(String userPreferences, String adminPreferences);

    SpatialGetMapConfigurationRS getMapConfiguration(SpatialGetMapConfigurationRQ mapConfigurationRQ) throws ServiceException;

    SpatialSaveOrUpdateMapConfigurationRS handleSaveOrUpdateSpatialMapConfiguration(SpatialSaveOrUpdateMapConfigurationRQ spatialSaveMapConfigurationRQ);

    List<ProjectionDto> getAllProjections();

    void handleDeleteMapConfiguration(SpatialDeleteMapConfigurationRQ spatialDeleteMapConfigurationRQ) throws ServiceException;

    ConfigurationDto retrieveAdminConfiguration(String config, Collection<String> permittedServiceLayers);

    String saveAdminJson(ConfigurationDto configurationDto, String defaultConfig, Collection<String> permittedServiceLayers);

    ConfigurationDto retrieveUserConfiguration(String config, String defaultConfig, String userName, Collection<String> permittedServiceLayers);

    String saveUserJson(ConfigurationDto configurationDto, String userPref);

    String resetUserJson(ConfigurationDto configurationDto, String userPref);

    ConfigurationDto getNodeDefaultValue(ConfigurationDto configurationDto, String adminConfig, String userName, Collection<String> permittedServiceLayers);

    ConfigDto getReportConfigWithoutMap(String userPref, String adminPref);
}
