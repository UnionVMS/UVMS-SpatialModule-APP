/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import javax.ejb.Local;
import java.util.Collection;
import java.util.List;

import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.ConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.ConfigurationDto;

@Local
public interface MapConfigService {

    MapConfigDto getReportConfig(int reportId, String userPreferences, String adminPreferences, String userName, String scopeName, String timeStamp, Collection<String> permittedServiceLayer);

    MapConfigDto getReportConfigWithoutSave(ConfigurationDto configurationDto, String userName, String scopeName, Collection<String> permittedServiceLayer);

    MapConfigurationType getMapConfigurationType(final Long reportId, Collection<String> permittedServiceLayers) throws ServiceException;

    MapConfigDto getBasicReportConfig(String userPreferences, String adminPreferences);

    /**
     * On the run report the method fetches the map configurations and enriches with values from
     * different tables from the spatial database.
     * The method is also responsible for filtering out layer settings based on restriction from USM.
     *
     * @param mapConfigurationRQ
     * @return
     * @throws ServiceException
     */
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