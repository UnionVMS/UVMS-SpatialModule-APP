/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.bean.impl;

import static eu.europa.ec.fisheries.uvms.spatial.service.mapper.ConfigurationMapper.getDefaultNodeConfiguration;
import static eu.europa.ec.fisheries.uvms.spatial.service.mapper.ConfigurationMapper.mergeConfiguration;
import static eu.europa.ec.fisheries.uvms.spatial.service.mapper.ConfigurationMapper.mergeNoMapConfiguration;
import static eu.europa.ec.fisheries.uvms.spatial.service.mapper.ConfigurationMapper.mergeUserConfiguration;
import static eu.europa.ec.fisheries.uvms.spatial.service.mapper.ConfigurationMapper.resetUserConfiguration;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportGetStartAndEndDateRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.CoordinatesFormat;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScaleBarUnits;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.MapConfigService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.ReportingService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.ConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.ControlDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.DisplayProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.LayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.MapDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.RefreshDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.ServiceLayersDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.TbControlDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.VectorStylesDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.ConfigurationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.LayerAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.LayerSettingsDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.LayersDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.ReferenceDataPropertiesDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.ReportProperties;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.StyleSettingsDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.SystemSettingsDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.VisibilitySettingsDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.enums.AreaTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.service.enums.LayerTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.MapConfigMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.ProjectionMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.ReportConnectSpatialMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.util.MapConfigHelper;
import eu.europa.ec.fisheries.uvms.spatial.service.util.SpatialValidator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

@Stateless
@Transactional
@Slf4j
public class MapConfigServiceBean implements MapConfigService {

    private static final String SCALE = "scale";
    private static final String MOUSECOORDS = "mousecoords";
    private static final String GEO_SERVER = "geo_server_url";
    private static final String BING_API_KEY = "bing_api_key";
    private static final String PROVIDER_FORMAT_BING = "BING";

    @EJB
    private SpatialRepository repository;

    @EJB
    private ReportingService reportingService;

    @Override
    @SneakyThrows
    public List<ProjectionDto> getAllProjections() {
        List<ProjectionEntity> projections = repository.findProjection();
        return ProjectionMapper.INSTANCE.projectionEntityListToProjectionDtoList(projections);
    }

    @Override
    @SneakyThrows
    @Transactional(Transactional.TxType.REQUIRES_NEW) // TODO check this // annotation required to send error response
    public void handleDeleteMapConfiguration(SpatialDeleteMapConfigurationRQ request) throws ServiceException {

        if (request == null) {
            throw new IllegalArgumentException("ARGUMENT CAN NOT BE NULL");
        }

        List<Long> spatialConnectIds = request.getSpatialConnectIds();

        if (!CollectionUtils.isEmpty(spatialConnectIds)) {
            repository.deleteReportConnectServiceAreas(spatialConnectIds);
        }
    }

    @Override
    public MapConfigurationType getMapConfigurationType(final Long reportId, Collection<String> permittedServiceLayers) throws ServiceException {

        if (reportId == null) {
            throw new IllegalArgumentException("ARGUMENT CAN NOT BE NULL");
        }

        ReportConnectSpatialEntity entity = repository.findReportConnectSpatialByReportId(reportId);

        if (entity == null) {
            return null;
        }

        LayerSettingsDto layerSettingsDto = MapConfigHelper.getLayerSettingsForMap(entity.getReportConnectServiceAreas());

        MapConfigurationType result = ReportConnectSpatialMapper.INSTANCE.reportConnectSpatialEntityToMapConfigurationType(entity);
        VisibilitySettingsDto visibilitySettings = MapConfigHelper.getVisibilitySettings(entity.getVisibilitySettings());
        result.setVisibilitySettings(MapConfigMapper.INSTANCE.getVisibilitySettingsType(visibilitySettings));
        StyleSettingsDto styleSettingsDto = MapConfigHelper.getStyleSettings(entity.getStyleSettings());
        result.setStyleSettings(MapConfigMapper.INSTANCE.getStyleSettingsType(styleSettingsDto));

        updateLayerSettings(layerSettingsDto, permittedServiceLayers);

        result.setLayerSettings(MapConfigMapper.INSTANCE.getLayerSettingsType(layerSettingsDto));
        Map<String, ReferenceDataPropertiesDto> referenceData = MapConfigHelper.getReferenceDataSettings(entity.getReferenceData());

        updateReferenceDataSettings(referenceData, permittedServiceLayers);

        result.setReferenceDatas(MapConfigMapper.INSTANCE.getReferenceDataType(referenceData));

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public SpatialGetMapConfigurationRS getMapConfiguration(SpatialGetMapConfigurationRQ mapConfigurationRQ) throws ServiceException {

        if (mapConfigurationRQ == null) {
            throw new IllegalArgumentException("MAP CONFIGURATION CAN NOT BE NULL");
        }

        long reportId = mapConfigurationRQ.getReportId();
        Collection<String> permittedServiceLayers = mapConfigurationRQ.getPermittedServiceLayers();
        return new SpatialGetMapConfigurationRS(getMapConfigurationType(reportId, permittedServiceLayers));
    }

    @Override
    @SneakyThrows
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public SpatialSaveOrUpdateMapConfigurationRS handleSaveOrUpdateSpatialMapConfiguration(final SpatialSaveOrUpdateMapConfigurationRQ request) {
        SpatialValidator.validate(request);
        ReportConnectSpatialEntity entity = getReportConnectSpatialEntity(request);

        if (entity == null) {
            throw new IllegalArgumentException("MAP CONFIGURATION CAN NOT BE NULL");
        }

        VisibilitySettingsDto visibilitySettings = MapConfigMapper.INSTANCE.getVisibilitySettingsDto(request.getMapConfiguration().getVisibilitySettings());
        entity.setVisibilitySettings(MapConfigHelper.getVisibilitySettingsJson(visibilitySettings));
        StyleSettingsDto styleSettings = MapConfigMapper.INSTANCE.getStyleSettingsDto(request.getMapConfiguration().getStyleSettings());
        entity.setStyleSettings(MapConfigHelper.getStyleSettingsJson(styleSettings));
        LayerSettingsDto layerSettingsDto = MapConfigMapper.INSTANCE.getLayerSettingsDto(request.getMapConfiguration().getLayerSettings());
        updateReportConnectServiceAreasEntity(entity, layerSettingsDto);
        Map<String, ReferenceDataPropertiesDto> referenceData = MapConfigMapper.INSTANCE.getReferenceData(request.getMapConfiguration().getReferenceDatas());
        entity.setReferenceData(MapConfigHelper.getReferenceDataSettingsJson(referenceData));
        repository.saveOrUpdateMapConfiguration(entity);
        return createSaveOrUpdateMapConfigurationResponse();
    }

    @Override
    @SneakyThrows
    public ConfigurationDto retrieveAdminConfiguration(String config, Collection<String> permittedServiceLayers) {
        ConfigurationDto configurationDto = MapConfigHelper.getAdminConfiguration(config);
        updateLayerSettings(configurationDto.getLayerSettings(), permittedServiceLayers);
        updateReferenceDataSettings(configurationDto.getReferenceData(), permittedServiceLayers);
        configurationDto.setSystemSettings(getConfigSystemSettings());
        return configurationDto;
    }



    @Override
    @SneakyThrows
    public ConfigurationDto retrieveUserConfiguration(String config, String defaultConfig, String userName, Collection<String> permittedServiceLayers) {
        ConfigurationDto userConfig = MapConfigHelper.getUserConfiguration(config);
        ConfigurationDto adminConfig = MapConfigHelper.getAdminConfiguration(defaultConfig);
        ConfigurationDto mergedConfig = mergeUserConfiguration(adminConfig, userConfig);
        updateLayerSettings(mergedConfig.getLayerSettings(), permittedServiceLayers);
        updateReferenceDataSettings(mergedConfig.getReferenceData(), permittedServiceLayers);
        return mergedConfig;
    }

    @Override
    @SneakyThrows
    public String saveAdminJson(ConfigurationDto configurationDto, String defaultConfig, Collection<String> permittedServiceLayers) {
        ConfigurationDto defaultConfigurationDto = retrieveAdminConfiguration(defaultConfig, permittedServiceLayers);
        setConfigSystemSettings(configurationDto.getSystemSettings(), defaultConfigurationDto.getSystemSettings()); // Update system config in spatial DB
        configurationDto.setSystemSettings(null); // Not saving system settings in USM
        return MapConfigHelper.getJson(configurationDto);
    }

    @Override
    @SneakyThrows
    public String saveUserJson(ConfigurationDto configurationDto, String userPref) {
        if(configurationDto == null) {
            throw new ServiceException("Invalid JSON");
        }
        ConfigurationDto userConfig = MapConfigHelper.getUserConfiguration(userPref);
        return MapConfigHelper.getJson(mergeConfiguration(configurationDto, userConfig));
    }

    @Override
    @SneakyThrows
    public String resetUserJson(ConfigurationDto configurationDto, String userPref) {
        if(configurationDto == null) {
            throw new ServiceException("Invalid JSON");
        }
        ConfigurationDto userConfig = MapConfigHelper.getUserConfiguration(userPref);
        ConfigurationDto resetConfig = resetUserConfiguration(configurationDto, userConfig);
        return MapConfigHelper.getJson(resetConfig);
    }

    @Override
    @SneakyThrows
    public ConfigurationDto getNodeDefaultValue(ConfigurationDto configurationDto, String adminConfig, String userName, Collection<String> permittedServiceLayers) {
        if(configurationDto == null || adminConfig == null) {
            throw new ServiceException("Invalid JSON");
        }
        ConfigurationDto defaultNodeConfig = getDefaultNodeConfiguration(configurationDto, MapConfigHelper.getAdminConfiguration(adminConfig));
        if (configurationDto.getLayerSettings() != null) {
            updateLayerSettings(defaultNodeConfig.getLayerSettings(), permittedServiceLayers);
        }
        if (configurationDto.getReferenceData() != null) {
            updateReferenceDataSettings(defaultNodeConfig.getReferenceData(), permittedServiceLayers);
        }
        return defaultNodeConfig;
    }

    @Override
    @SneakyThrows
    public ConfigDto getReportConfigWithoutMap(String userPref, String adminPref) {
        return mergeNoMapConfiguration(MapConfigHelper.getUserConfiguration(userPref), MapConfigHelper.getAdminConfiguration(adminPref));
    }

    /**
     * returns Report configuration, which includes mapDefaultSRIDToEPSG configurations, vector style configurations and visibility settings.
     * The method merges all settings taking into account the different available levels of settings - global, user specified and report specific ones.
     * @param reportId
     * @param userPreferences
     * @param adminPreferences
     * @param userName
     * @param scopeName
     * @param timeStamp
     * @param permittedServiceLayers
     * @return
     */
    @Override
    @SneakyThrows
    public MapConfigDto getReportConfig(int reportId, String userPreferences, String adminPreferences, String userName, String scopeName, String timeStamp, Collection<String> permittedServiceLayers) {
        ConfigurationDto configurationDto = mergeConfiguration(MapConfigHelper.getUserConfiguration(userPreferences), MapConfigHelper.getAdminConfiguration(adminPreferences)); //Returns merged config object between Admin and User configuration from USM
        return new MapConfigDto(getMap(configurationDto, reportId, userName, scopeName, timeStamp, permittedServiceLayers),
                getVectorStyles(configurationDto, reportId),
                getVisibilitySettings(configurationDto, reportId));
    }

    /**
     *  returns Report configuration, which includes mapDefaultSRIDToEPSG configurations, vector style configurations and visibility settings.
     * The method merges all settings taking into account the different available levels of settings - global, user specified and report specific ones.
     * @param configurationDto
     * @param userName
     * @param scopeName
     * @param permittedServiceLayers
     * @return
     */
    @Override
    @SneakyThrows
    public MapConfigDto getReportConfigWithoutSave(ConfigurationDto configurationDto, String userName, String scopeName, Collection<String> permittedServiceLayers) {
        return new MapConfigDto(getMap(configurationDto, null, userName, scopeName, null, permittedServiceLayers),
                getVectorStyles(configurationDto, null),
                getVisibilitySettings(configurationDto, null));
    }

    @Override
    @SneakyThrows
    public MapConfigDto getBasicReportConfig(String userPreferences, String adminPreferences) {
        ConfigurationDto configurationDto = mergeConfiguration(MapConfigHelper.getUserConfiguration(userPreferences), MapConfigHelper.getAdminConfiguration(adminPreferences)); //Returns merged config object between Admin and User configuration from USM
        ProjectionDto projection = getMapProjection(null, configurationDto);
        List<ControlDto> controls = getControls(null, configurationDto);
        ServiceLayersDto serviceLayersDto = new ServiceLayersDto();
        serviceLayersDto.setBaseLayers(getLayerDtoList(configurationDto.getLayerSettings().getBaseLayers(), true, configurationDto.getReferenceData()));
        return new MapConfigDto(new MapDto(projection, controls, null, serviceLayersDto, null), null, null);
    }

    private ReportConnectSpatialEntity getReportConnectSpatialEntity(final SpatialSaveOrUpdateMapConfigurationRQ request) throws ServiceException {
        ReportConnectSpatialEntity entity = repository.findReportConnectSpatialByReportIdAndConnectId(request.getMapConfiguration().getReportId(), request.getMapConfiguration().getSpatialConnectId());

        if (entity != null) {
            entity.setScaleBarType(request.getMapConfiguration().getScaleBarUnits());
            entity.setDisplayFormatType(request.getMapConfiguration().getCoordinatesFormat());

            Set<ReportConnectServiceAreasEntity> reportConnectServiceAreas = entity.getReportConnectServiceAreas();
            List<Long> ids = new ArrayList();
            for (ReportConnectServiceAreasEntity r : reportConnectServiceAreas) {
                ids.add(r.getId());
            }
            repository.deleteReportConnectServiceAreas(ids);
        } else {
            entity = ReportConnectSpatialMapper.INSTANCE.mapConfigurationTypeToReportConnectSpatialEntity(request.getMapConfiguration());
        }

        Long mapProjectionId = request.getMapConfiguration().getMapProjectionId();
        if (mapProjectionId != null){
            ProjectionEntity mapProjection = repository.findProjectionEntityById(mapProjectionId);
            entity.setProjectionByMapProjId(mapProjection);
        }
        Long displayProjectionId = request.getMapConfiguration().getDisplayProjectionId();
        if (displayProjectionId != null){
            ProjectionEntity displayProjection = repository.findProjectionEntityById(displayProjectionId);
            entity.setProjectionByDisplayProjId(displayProjection);
        }

        return entity;
    }

    private void updateReportConnectServiceAreasEntity(ReportConnectSpatialEntity entity, LayerSettingsDto layerSettingsDto) throws ServiceException {
        if(layerSettingsDto == null) {
            clearReportConectServiceAreas(entity);
            return;
        }
        Set<ReportConnectServiceAreasEntity> serviceAreas = createReportConnectServiceAreas(entity, layerSettingsDto);
        if (serviceAreas != null && !serviceAreas.isEmpty()) {
            Set<ReportConnectServiceAreasEntity> areas = entity.getReportConnectServiceAreas();
            if (areas == null) {
                entity.setReportConnectServiceAreas(serviceAreas);
            } else {
                areas.clear();
                areas.addAll(serviceAreas);
                entity.setReportConnectServiceAreas(areas);
            }
        } else {
            clearReportConectServiceAreas(entity);
        }
    }

    private void clearReportConectServiceAreas(ReportConnectSpatialEntity entity) {
        Set<ReportConnectServiceAreasEntity> areas = entity.getReportConnectServiceAreas();
        if(areas != null) {
            areas.clear();
            entity.setReportConnectServiceAreas(areas);
        }
    }

    private Set<ReportConnectServiceAreasEntity> createReportConnectServiceAreas(ReportConnectSpatialEntity reportConnectSpatialEntity, LayerSettingsDto layerSettingsDto) throws ServiceException {
        Set<ReportConnectServiceAreasEntity> reportConnectServiceAreasEntities = createReportConnectServiceAreasPerLayer(layerSettingsDto.getAreaLayers(), reportConnectSpatialEntity, LayerTypeEnum.AREA);
        reportConnectServiceAreasEntities.addAll(createReportConnectServiceAreasPerLayer(layerSettingsDto.getPortLayers(), reportConnectSpatialEntity, LayerTypeEnum.PORT));
        reportConnectServiceAreasEntities.addAll(createReportConnectServiceAreasPerLayer(layerSettingsDto.getAdditionalLayers(), reportConnectSpatialEntity, LayerTypeEnum.ADDITIONAL));
        reportConnectServiceAreasEntities.addAll(createReportConnectServiceAreasPerLayer(layerSettingsDto.getBaseLayers(), reportConnectSpatialEntity, LayerTypeEnum.BASE));
        return reportConnectServiceAreasEntities;
    }

    private Set<ReportConnectServiceAreasEntity> createReportConnectServiceAreasPerLayer(List<? extends LayersDto> layers, ReportConnectSpatialEntity reportConnectSpatialEntity, LayerTypeEnum layerTypeEnum) {
        Set<ReportConnectServiceAreasEntity> reportConnectServiceAreasEntities = new HashSet<>();
        for (LayersDto layer : layers) {
            ReportConnectServiceAreasEntity reportConnectServiceAreasEntity = new ReportConnectServiceAreasEntity();
            reportConnectServiceAreasEntity.setReportConnectSpatial(reportConnectSpatialEntity);
            List<ServiceLayerEntity> serviceLayerEntities = repository.findServiceLayerEntityByIds(Arrays.asList(Long.parseLong(layer.getServiceLayerId())));
            reportConnectServiceAreasEntity.setServiceLayer((serviceLayerEntities != null && !serviceLayerEntities.isEmpty()) ? serviceLayerEntities.get(0) : null);
            reportConnectServiceAreasEntity.setLayerOrder(layer.getOrder().intValue());
            reportConnectServiceAreasEntity.setLayerType(layerTypeEnum.getType());
            if (layer instanceof LayerAreaDto) {
                reportConnectServiceAreasEntity.setAreaType(((LayerAreaDto) layer).getAreaType().getType());
                if (((LayerAreaDto)layer).getAreaType().equals(AreaTypeEnum.userarea)) {
                    reportConnectServiceAreasEntity.setSqlFilter(String.valueOf(((LayerAreaDto) layer).getGid()));
                } else if (((LayerAreaDto)layer).getAreaType().equals(AreaTypeEnum.areagroup)) {
                    reportConnectServiceAreasEntity.setSqlFilter(((LayerAreaDto) layer).getAreaGroupName());
                }
            }
            reportConnectServiceAreasEntities.add(reportConnectServiceAreasEntity);
        }
        return reportConnectServiceAreasEntities;
    }

    private SpatialSaveOrUpdateMapConfigurationRS createSaveOrUpdateMapConfigurationResponse() {
        return new SpatialSaveOrUpdateMapConfigurationRS();
    }

    private void updateReferenceDataSettings(Map<String, ReferenceDataPropertiesDto> referenceData, Collection<String> permittedServiceLayers) {
        if (referenceData != null) {
            Iterator<Map.Entry<String, ReferenceDataPropertiesDto>> iterator = referenceData.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, ReferenceDataPropertiesDto> referenceDataEntry = iterator.next();
                if (!MapConfigHelper.isServiceLayerPermitted(referenceDataEntry.getKey(), permittedServiceLayers)) {
                    iterator.remove();
                }
            }
        }
    }

    private void updateLayerSettings(LayerSettingsDto layerSettingsDto, Collection<String> permittedServiceLayers) throws ServiceException {

        String bingApiKey = getBingApiKey();
        List<Long> ids = new ArrayList<>(); // Get All the Ids to query for Service layer all together

        if(layerSettingsDto == null){
            return;
        }

        ids.addAll(MapConfigHelper.getServiceLayerIds(layerSettingsDto.getAdditionalLayers()));
        ids.addAll(MapConfigHelper.getServiceLayerIds(layerSettingsDto.getBaseLayers()));
        ids.addAll(MapConfigHelper.getServiceLayerIds(layerSettingsDto.getPortLayers()));
        ids.addAll(MapConfigHelper.getServiceLayerIds(layerSettingsDto.getAreaLayers()));

        if (ids.isEmpty()) {
            return; // There is no Areas in the LayersSettings. Returning the call
        }

        List<ServiceLayerEntity> serviceLayers = repository.findServiceLayerEntityByIds(ids); // Get Service layers by all the ids

        //Update the layers
        updateLayer(layerSettingsDto.getAdditionalLayers(), serviceLayers, bingApiKey);
        updateLayer(layerSettingsDto.getBaseLayers(), serviceLayers, bingApiKey);
        updateLayer(layerSettingsDto.getPortLayers(), serviceLayers, bingApiKey);
        updateLayer(layerSettingsDto.getAreaLayers(), serviceLayers, bingApiKey);
        updateAreaLayer(layerSettingsDto.getAreaLayers());

        if (permittedServiceLayers != null && !permittedServiceLayers.isEmpty()) {
            filterAllForbiddenLayers(layerSettingsDto, permittedServiceLayers);
        }
    }

    private void sortLayer(List<? extends LayersDto> layers) {
        if(layers != null) {
            Collections.sort(layers);
            for (LayersDto layerDto : layers) {
                layerDto.setOrder(null);
            }
        }
    }


    private void updateAreaLayer(List<LayerAreaDto> layers) throws ServiceException {
        List<Long> userAreaIds = MapConfigHelper.getUserAreaIds(layers);
        if (!userAreaIds.isEmpty()) {
            List<AreaDto> areaDtos = repository.findAllUserAreasByGids(userAreaIds);
            for (LayerAreaDto layerDto : layers) {
                for (AreaDto areaDto :  areaDtos) {
                    if (Objects.equals(layerDto.getGid(), areaDto.getGid())) {
                        layerDto.setAreaName(areaDto.getName());
                        layerDto.setAreaDesc(areaDto.getDesc());
                    }
                }
            }
        }
        sortLayer(layers);
    }

    private void updateLayer(List<? extends LayersDto> layers, List<ServiceLayerEntity> serviceLayers, String bingApiKey) {
        if (layers != null) {
            List<LayersDto> layersToExclude = new ArrayList<>();
            for (LayersDto layersDto : layers) {
                for (ServiceLayerEntity serviceLayerEntity : serviceLayers) {
                    if (Long.parseLong(layersDto.getServiceLayerId()) == serviceLayerEntity.getId()) {
                        if (serviceLayerEntity.getProviderFormat() != null && PROVIDER_FORMAT_BING.equalsIgnoreCase(serviceLayerEntity.getProviderFormat().getServiceType()) && bingApiKey == null) {
                            layersToExclude.add(layersDto);
                        } else {
                            layersDto.setName(serviceLayerEntity.getName());
                            layersDto.setSubType(serviceLayerEntity.getSubType());
                            layersDto.setAreaLocationTypeName(serviceLayerEntity.getAreaType().getTypeName());
                        }
                        break;
                    }
                }
            }
            layers.removeAll(layersToExclude);
        }
        sortLayer(layers);
    }

    private MapDto getMap(ConfigurationDto configurationDto, Integer reportId, String userName, String scopeName, String timeStamp, Collection<String> permittedServiceLayer) throws ServiceException {
        ProjectionDto projection = getMapProjection(reportId, configurationDto);
        List<ControlDto> controls = getControls(reportId, configurationDto);
        List<TbControlDto> tbControls = getTbControls(configurationDto);
        ServiceLayersDto layers = getServiceAreaLayer(reportId, configurationDto, userName, scopeName, timeStamp, permittedServiceLayer);
        RefreshDto refreshDto = getRefreshDto(configurationDto);
        return new MapDto(projection, controls, tbControls, layers, refreshDto);
    }

    private ProjectionDto getMapProjection(Integer reportId, ConfigurationDto configurationDto) {
        if (reportId != null) {
            List<ProjectionDto> projectionDtoList = repository.findProjectionByMap(reportId);
            if (projectionDtoList != null && !projectionDtoList.isEmpty()) { // Get Map Projection for report
                return projectionDtoList.get(0);
            }
        }
        if(configurationDto.getMapSettings() == null){
            return null;
        }
        return getProjection(configurationDto.getMapSettings().getMapProjectionId());
    }

    private List<ControlDto> getControls(Integer reportId, ConfigurationDto configurationDto) throws ServiceException {
        List<ControlDto> controls = MapConfigMapper.INSTANCE.getControls(configurationDto.getToolSettings().getControl());
        DisplayProjectionDto displayProjection = getDisplayProjection(reportId, configurationDto);
        return updateControls(controls, displayProjection.getUnits().value(),
                displayProjection.getEpsgCode(), displayProjection.getFormats().value());
    }

    private ProjectionDto getProjection(Integer id) {
        List<ProjectionDto> projectionDtoList = repository.findProjectionById(id.longValue());
        return (projectionDtoList != null && !projectionDtoList.isEmpty()) ? projectionDtoList.get(0) : null;
    }

    private List<TbControlDto> getTbControls(ConfigurationDto configurationDto) {
        return MapConfigMapper.INSTANCE.getTbControls(configurationDto.getToolSettings().getTbControl());
    }

    private ServiceLayersDto getServiceAreaLayer(Integer reportId, ConfigurationDto configurationDto, String userName, String scopeName, String timeStamp, Collection<String> permittedServiceLayers) throws ServiceException {

        ReportConnectSpatialEntity entity = null;
        if (reportId != null) {
            entity = repository.findReportConnectSpatialByReportId((long) reportId);
        }
        LayerSettingsDto layerSettingsDto;
        Map<String, ReferenceDataPropertiesDto> referenceData;

        if (entity != null) {
            layerSettingsDto = (entity.getReportConnectServiceAreas() != null && !entity.getReportConnectServiceAreas().isEmpty()) ?
                    MapConfigHelper.getLayerSettingsForMap(entity.getReportConnectServiceAreas()) : configurationDto.getLayerSettings();
            referenceData = entity.getReferenceData() != null ?
                    MapConfigHelper.getReferenceDataSettings(entity.getReferenceData()) : configurationDto.getReferenceData();
        } else {
            layerSettingsDto = configurationDto.getLayerSettings();
            referenceData = configurationDto.getReferenceData();
        }

        return getServiceAreaLayers(layerSettingsDto, userName, scopeName, timeStamp, reportId, referenceData, configurationDto.getReportProperties(), permittedServiceLayers);
    }

    private VectorStylesDto getVectorStyles(ConfigurationDto configurationDto, Integer reportId) throws ServiceException {
        if (reportId != null) {
            ReportConnectSpatialEntity entity = repository.findReportConnectSpatialByReportId(reportId.longValue());
            if (entity != null && entity.getStyleSettings() != null) {
                StyleSettingsDto styleSettingsDto = MapConfigHelper.getStyleSettings(entity.getStyleSettings());
                if ((styleSettingsDto.getPositions() != null && styleSettingsDto.getPositions().getStyle() != null) ||
                        (styleSettingsDto.getSegments() != null && styleSettingsDto.getSegments().getStyle() != null) ||
                        (styleSettingsDto.getAlarms() != null)) {
                    return MapConfigMapper.INSTANCE.getStyleDtos(styleSettingsDto); // Style Settings is overridden by Report. Return the report configured style settings
                }
            }
        }
        return MapConfigMapper.INSTANCE.getStyleDtos(configurationDto.getStylesSettings()); // Return merged style settings from Admin and User config
    }

    private VisibilitySettingsDto getVisibilitySettings(ConfigurationDto configurationDto, Integer reportId) throws ServiceException {
        if (reportId != null) {
            ReportConnectSpatialEntity entity = repository.findReportConnectSpatialByReportId(reportId.longValue());
            if (entity != null && entity.getVisibilitySettings() != null) {
                VisibilitySettingsDto visibilitySettingsDto = MapConfigHelper.getVisibilitySettings(entity.getVisibilitySettings());
                if (MapConfigHelper.isVisibilitySettingsOverriddenByReport(visibilitySettingsDto)) {
                    return visibilitySettingsDto; // VisibilitySettings is overridden by Report. Return the report configured visibility settings
                }
            }
        }
        return configurationDto.getVisibilitySettings(); // Return merged visibility settings from Admin and User Config
    }

    private RefreshDto getRefreshDto(ConfigurationDto configurationDto) {
        return new RefreshDto(configurationDto.getMapSettings().getRefreshStatus(), configurationDto.getMapSettings().getRefreshRate());
    }

    private ServiceLayersDto getServiceAreaLayers(LayerSettingsDto layerSettingsDto,
                                                  String userName, String scopeName, String timeStamp,
                                                  Integer reportId, Map<String, ReferenceDataPropertiesDto> referenceData,
                                                  ReportProperties reportProperties, Collection<String> allowedServiceLayers) throws ServiceException {

        ServiceLayersDto serviceLayersDto = new ServiceLayersDto();
        serviceLayersDto.setPortLayers(getLayerDtoList(layerSettingsDto.getPortLayers(), false, referenceData)); // Get Service Layers for Port layers
        serviceLayersDto.setSystemLayers(getAreaLayerDtoList(layerSettingsDto.getAreaLayers(), false, userName, scopeName, timeStamp, reportId, referenceData, reportProperties)); // // Get Service Layers for system layers and User Layers
        serviceLayersDto.setAdditionalLayers(getLayerDtoList(layerSettingsDto.getAdditionalLayers(), false, referenceData)); // Get Service Layers for Additional layers
        serviceLayersDto.setBaseLayers(getLayerDtoList(layerSettingsDto.getBaseLayers(), true, referenceData)); // Get Service Layers for base layers

        if (allowedServiceLayers != null) {
            filterAllForbiddenLayers(serviceLayersDto, allowedServiceLayers);
        }
        return serviceLayersDto;
    }

    private void filterAllForbiddenLayers(LayerSettingsDto layerSettingsDto, final Collection<String> permittedLayersNames) {
        filterList(layerSettingsDto.getPortLayers(), permittedLayersNames);
        filterList(layerSettingsDto.getAreaLayers(), permittedLayersNames);
        filterList(layerSettingsDto.getAdditionalLayers(), permittedLayersNames);
        filterList(layerSettingsDto.getBaseLayers(), permittedLayersNames);
    }

    private void filterAllForbiddenLayers(ServiceLayersDto layerSettingsDto, final Collection<String> permittedLayersNames) {
        filterLayerList(layerSettingsDto.getPortLayers(), permittedLayersNames);
        filterLayerList(layerSettingsDto.getSystemLayers(), permittedLayersNames);
        filterLayerList(layerSettingsDto.getAdditionalLayers(), permittedLayersNames);
        filterLayerList(layerSettingsDto.getBaseLayers(), permittedLayersNames);
    }

    private void filterList(List<? extends LayersDto> layers, final Collection<String> permittedLayersNames) {
        if (layers != null) {
            Iterator<? extends LayersDto> iterator = layers.iterator();
            while (iterator.hasNext()) {
                LayersDto layer = iterator.next();

                if (!permittedLayersNames.contains(layer.getAreaLocationTypeName()) ) {
                    iterator.remove();
                }
            }
        }
    }

    private void filterLayerList(List<? extends LayerDto> layers, final Collection<String> permittedLayersNames) {
        if (layers != null) {
            Iterator<? extends LayerDto> iterator = layers.iterator();
            while (iterator.hasNext()) {
                LayerDto layer = iterator.next();

                if (!permittedLayersNames.contains(layer.getAreaLocationTypeName()) ) {
                    iterator.remove();
                }
            }
        }
    }

    private List<LayerDto> getAreaLayerDtoList(List<LayerAreaDto> layersDtos,
                                               boolean isBackground, String userName, String scopeName,
                                               String timeStamp, Integer reportId,
                                               Map<String, ReferenceDataPropertiesDto> referenceData,
                                               ReportProperties reportProperties) throws ServiceException {
        if (layersDtos == null || layersDtos.isEmpty()) {
            return null;
        }
        Collections.sort(layersDtos);
        List<LayerDto> layerDtoList = new ArrayList<>();
        for (LayerAreaDto layerAreaDto : layersDtos) {
            List<ServiceLayerEntity> serviceLayers = getServiceLayers(Arrays.asList(Long.parseLong(layerAreaDto.getServiceLayerId())), getBingApiKey());
            if (serviceLayers != null && !serviceLayers.isEmpty()) {
                ServiceLayerEntity serviceLayer = serviceLayers.get(0);
                List<LayerDto> layerDtos = getLayerDtos(Arrays.asList(serviceLayer), isBackground, referenceData);
                if (layerDtos != null && !layerDtos.isEmpty()) {
                    LayerDto layerDto = layerDtos.get(0);
                    if (layerAreaDto.getAreaType().equals(AreaTypeEnum.userarea)) {
                        List<AreaDto> userAreas = repository.findAllUserAreasByGids(Arrays.asList(layerAreaDto.getGid()));
                        layerDto.setGid((userAreas != null && !userAreas.isEmpty()) ? userAreas.get(0).getGid() : null);
                        layerDto.setTitle((userAreas != null && !userAreas.isEmpty()) ? userAreas.get(0).getName() : null);
                        layerDto.setAreaType(AreaTypeEnum.userarea.getType().toUpperCase());
                    } else if (layerAreaDto.getAreaType().equals(AreaTypeEnum.areagroup)) {
                        layerDto.setCqlAll(MapConfigHelper.getAreaGroupCqlAll(userName, scopeName, layerAreaDto.getAreaGroupName()));
                        if (reportId != null) {
                            layerDto.setCqlActive(getAreaGroupCqlActive(reportId, userName, scopeName, timeStamp));
                        } else {
                            layerDto.setCqlActive(MapConfigHelper.getAreaGroupCqlActive(reportProperties.getStartDate(), reportProperties.getEndDate()));
                        }
                        layerDto.setAreaType(AreaTypeEnum.areagroup.getType().toUpperCase());
                        layerDto.setTitle(layerAreaDto.getAreaGroupName());
                    } else if (layerAreaDto.getAreaType().equals(AreaTypeEnum.sysarea)) {
                        layerDto.setAreaType(AreaTypeEnum.sysarea.getType().toUpperCase());
                    }
                    layerDtoList.add(layerDto);
                }
            }
        }
        return layerDtoList;
    }

    private String getAreaGroupCqlActive(int reportId, String userName, String scopeName, String timeStamp) throws ServiceException {
        ReportGetStartAndEndDateRS response = reportingService.getReportDates(reportId, userName, scopeName, timeStamp);
        if (response == null) {
            return null;
        }
        return MapConfigHelper.getAreaGroupCqlActive(response.getStartDate(), response.getEndDate());
    }

    private List<LayerDto> getLayerDtoList(List<? extends LayersDto> layersDtos,
                                           boolean isBackground, Map<String, ReferenceDataPropertiesDto> referenceData) throws ServiceException {
        if (layersDtos == null || layersDtos.isEmpty()) {
            return null;
        }
        Collections.sort(layersDtos);
        List<Long> serviceLayerIds = MapConfigHelper.getServiceLayerIds(layersDtos);
        List<ServiceLayerEntity> serviceLayerEntities = getServiceLayers(serviceLayerIds, getBingApiKey());
        return getLayerDtos(serviceLayerEntities, isBackground, referenceData);
    }

    private List<LayerDto> getLayerDtos(List<ServiceLayerEntity> serviceLayerEntities, boolean isBaseLayer,
                                        Map<String, ReferenceDataPropertiesDto> referenceData) throws ServiceException {
        List<LayerDto> layerDtos = new ArrayList<>();
        for (ServiceLayerEntity serviceLayerEntity : serviceLayerEntities) {
            layerDtos.add(MapConfigHelper.convertToServiceLayer(serviceLayerEntity, getGeoServerUrl(), getBingApiKey(), isBaseLayer, referenceData));
        }
        return layerDtos;
    }

    private String getGeoServerUrl() throws ServiceException {
        return repository.findSystemConfigByName(GEO_SERVER);
    }

    private String getBingApiKey() throws ServiceException {
        return repository.findSystemConfigByName(BING_API_KEY);
    }

    private List<ServiceLayerEntity> getServiceLayers(List<Long> ids, String bingApiKey) {
        List<ServiceLayerEntity> serviceLayers = MapConfigHelper.sortServiceLayers(repository.findServiceLayerEntityByIds(ids), ids);
        Iterator<ServiceLayerEntity> layerIterator = serviceLayers.iterator();
        while(layerIterator.hasNext()) {
            ServiceLayerEntity serviceLayer = layerIterator.next();
            if (MapConfigHelper.isRemoveLayer(serviceLayer, bingApiKey)) {
                layerIterator.remove();
            }
        }
        return serviceLayers;
    }

    private List<ControlDto> updateControls(List<ControlDto> controls, String scaleBarUnit, int epsgCode, String coordinateFormat) {
        for (ControlDto controlDto : controls) {
            if (controlDto.getType().equalsIgnoreCase(SCALE)) {
                controlDto.setUnits(scaleBarUnit);
            }
            if (controlDto.getType().equalsIgnoreCase(MOUSECOORDS)) {
                controlDto.setEpsgCode(epsgCode);
                controlDto.setFormat(coordinateFormat);
            }
        }
        return controls;
    }

    private DisplayProjectionDto getDisplayProjection(Integer reportId, ConfigurationDto configurationDto) throws ServiceException {
        DisplayProjectionDto displayProjectionDto = new DisplayProjectionDto();
        ReportConnectSpatialEntity entity = null;
        if(reportId != null) {
            entity = repository.findReportConnectSpatialByReportId(reportId.longValue());
        }

        if (entity != null && entity.getProjectionByDisplayProjId() != null) { // Check value in DB
            displayProjectionDto.setEpsgCode(entity.getProjectionByDisplayProjId().getSrsCode());
        } else { // If not get from config
            ProjectionDto projection = getProjection(configurationDto.getMapSettings().getDisplayProjectionId());
            displayProjectionDto.setEpsgCode(projection.getEpsgCode());
        }

        if (entity != null && entity.getScaleBarType() != null) { // Check value in DB
            displayProjectionDto.setUnits(entity.getScaleBarType());
        } else {  // If not get from config
            displayProjectionDto.setUnits(ScaleBarUnits.fromValue(configurationDto.getMapSettings().getScaleBarUnits()));
        }

        if (entity != null && entity.getDisplayFormatType() != null) { // Check value in DB
            displayProjectionDto.setFormats(entity.getDisplayFormatType());
        } else {  // If not get from config
            displayProjectionDto.setFormats(CoordinatesFormat.fromValue(configurationDto.getMapSettings().getCoordinatesFormat()));
        }

        return displayProjectionDto;
    }

    private SystemSettingsDto getConfigSystemSettings() throws ServiceException {
        SystemSettingsDto systemSettingsDto = new SystemSettingsDto();
        systemSettingsDto.setGeoserverUrl(getGeoServerUrl());
        systemSettingsDto.setBingApiKey(getBingApiKey());
        return systemSettingsDto;
    }

    private void setConfigSystemSettings(SystemSettingsDto systemSettingsDto, SystemSettingsDto defaultSystemSettingsDto) throws ServiceException {
        // Update Geo Server URL
        String geoServerUrl = systemSettingsDto.getGeoserverUrl();
        String defaultGeoServerUrl = defaultSystemSettingsDto.getGeoserverUrl();
        if (geoServerUrl != null && !geoServerUrl.equals(defaultGeoServerUrl)) {
            repository.updateSystemConfig(GEO_SERVER, geoServerUrl);
        }

        // Update Bing API Key
        String bingApiKey = systemSettingsDto.getBingApiKey();
        if (bingApiKey != null && "".equals(bingApiKey.trim())) {
            bingApiKey = null;
        }
        repository.updateSystemConfig(BING_API_KEY, bingApiKey);
    }
}