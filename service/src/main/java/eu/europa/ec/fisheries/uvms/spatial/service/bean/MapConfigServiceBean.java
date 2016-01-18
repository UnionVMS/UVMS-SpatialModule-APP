package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.*;
import eu.europa.ec.fisheries.uvms.spatial.entity.mapper.ReportConnectSpatialMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.*;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.MapConfigMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.ProjectionMapper;
import eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.validator.SpatialValidator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.spatial.service.mapper.ConfigurationMapper.*;

@Stateless
@Local(MapConfigService.class)
@Transactional
@Slf4j
public class MapConfigServiceBean implements MapConfigService {

    private static final String SCALE = "scale";

    private static final String MOUSECOORDS = "mousecoords";

    private static final String NAME = "name";

    private static final String GEO_SERVER = "geo_server_url";

    private static final String BING_API_KEY = "bing_api_key";

    private static final String PROVIDER_FORMAT_BING = "BING";

    private static final Integer DEFAULT_EPSG = 3857;

    @EJB
    private SpatialRepository repository;

    @Override
    @SneakyThrows
    public List<ProjectionDto> getAllProjections() {

        List<ProjectionEntity> projections = repository.findAllEntity(ProjectionEntity.class); // TODO projectionDAO

        return ProjectionMapper.INSTANCE.projectionEntityListToProjectionDtoList(projections);

    }

    @Override
    @SneakyThrows
    @Transactional(Transactional.TxType.REQUIRES_NEW) // annotation required to send error response
    public void handleDeleteMapConfiguration(SpatialDeleteMapConfigurationRQ request) throws ServiceException {
        SpatialValidator.validate(request);

        repository.deleteBy(request.getSpatialConnectIds());
    }

    @Override
    public MapConfigurationType getMapConfigurationType(final Long reportId) throws ServiceException {
        SpatialValidator.validate(reportId);

        ReportConnectSpatialEntity entity = repository.findReportConnectSpatialBy(reportId);

        return ReportConnectSpatialMapper.INSTANCE.reportConnectSpatialEntityToMapConfigurationType(entity);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public SpatialGetMapConfigurationRS getMapConfiguration(SpatialGetMapConfigurationRQ mapConfigurationRQ) throws ServiceException {
        long reportId = mapConfigurationRQ.getReportId();

        return new SpatialGetMapConfigurationRS(getMapConfigurationType(reportId));

    }

    @Override
    @SneakyThrows
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public SpatialSaveOrUpdateMapConfigurationRS handleSaveOrUpdateSpatialMapConfiguration(final SpatialSaveOrUpdateMapConfigurationRQ request) {
        SpatialValidator.validate(request);

        ReportConnectSpatialEntity entity =
                ReportConnectSpatialMapper.INSTANCE.mapConfigurationTypeToReportConnectSpatialEntity(request.getMapConfiguration());

        repository.saveOrUpdateMapConfiguration(entity);
        return createSaveOrUpdateMapConfigurationResponse();

    }

    private SpatialSaveOrUpdateMapConfigurationRS createSaveOrUpdateMapConfigurationResponse() {
        return new SpatialSaveOrUpdateMapConfigurationRS();
    }

    @Override
    @SneakyThrows
    public ConfigurationDto retrieveAdminConfiguration(String config) {
        ConfigurationDto configurationDto = getAdminConfiguration(config);
        updateLayerSettings(configurationDto.getLayerSettings(), null, false);
        configurationDto.setSystemSettings(getConfigSystemSettings());
        return configurationDto;
    }

    @Override
    @SneakyThrows
    public ConfigurationDto retrieveUserConfiguration(String config, String defaultConfig, String userName) {
        ConfigurationDto userConfig = getUserConfiguration(config);
        ConfigurationDto adminConfig = getAdminConfiguration(defaultConfig);
        ConfigurationDto mergedConfig = mergeUserConfiguration(adminConfig, userConfig);
        updateLayerSettings(mergedConfig.getLayerSettings(), userName, true);
        return mergedConfig;
    }

    @Override
    @SneakyThrows
    public String saveAdminJson(ConfigurationDto configurationDto, String defaultConfig) {
        ConfigurationDto defaultConfigurationDto = retrieveAdminConfiguration(defaultConfig);
        setConfigSystemSettings(configurationDto.getSystemSettings(), defaultConfigurationDto.getSystemSettings()); // Update system config in spatial DB
        configurationDto.setSystemSettings(null); // Not saving system settings in USM
        return getJson(configurationDto);
    }

    @Override
    @SneakyThrows
    public String saveUserJson(ConfigurationDto configurationDto, String userPref) {
        if(configurationDto == null) {
            throw new ServiceException("Invalid JSON");
        }
        ConfigurationDto userConfig = getUserConfiguration(userPref);
        return getJson(mergeConfiguration(configurationDto, userConfig));
    }

    @Override
    @SneakyThrows
    public String resetUserJson(ConfigurationDto configurationDto, String userPref) {
        if(configurationDto == null) {
            throw new ServiceException("Invalid JSON");
        }
        ConfigurationDto userConfig = getUserConfiguration(userPref);
        ConfigurationDto resetConfig = resetUserConfiguration(configurationDto, userConfig);
        return getJson(resetConfig);
    }

    @Override
    @SneakyThrows
    public ConfigurationDto getNodeDefaultValue(ConfigurationDto configurationDto, String adminConfig, String userName) {
        if(configurationDto == null || adminConfig == null) {
            throw new ServiceException("Invalid JSON");
        }
        ConfigurationDto defaultNodeConfig = getDefaultNodeConfiguration(configurationDto, getAdminConfiguration(adminConfig));
        if (configurationDto.getLayerSettings() != null) {
            updateLayerSettings(defaultNodeConfig.getLayerSettings(), userName, true);
        }
        return defaultNodeConfig;
    }

    @Override
    @SneakyThrows
    public ConfigDto getReportConfigWithoutMap(String userPref, String adminPref) {
        ConfigDto configDto = mergeNoMapConfiguration(getUserConfiguration(userPref), getAdminConfiguration(adminPref)); //Returns merged config object between Admin and User configuration from USM
        return configDto;
    }

    @Override
    @SneakyThrows
    public MapConfigDto getReportConfig(int reportId, String userPreferences, String adminPreferences, String userName) {
        ConfigurationDto configurationDto = mergeConfiguration(getUserConfiguration(userPreferences), getAdminConfiguration(adminPreferences)); //Returns merged config object between Admin and User configuration from USM
        return new MapConfigDto(getMap(configurationDto, reportId, userName), getVectorStyles(configurationDto), getVisibilitySettings(configurationDto));
    }

    private void updateLayerSettings(LayerSettingsDto layerSettingsDto, String userName, boolean includeUserArea) throws ServiceException {
        String bingApiKey = getBingApiKey();
        List<Long> ids =  new ArrayList<Long>(); // Get All the Ids to query for Service layer all together
        ids.addAll(getServiceLayerIds(layerSettingsDto.getAdditionalLayers()));
        ids.addAll(getServiceLayerIds(layerSettingsDto.getBaseLayers()));
        ids.addAll(getServiceLayerIds(layerSettingsDto.getPortLayers()));

        if (layerSettingsDto.getAreaLayers() != null) {
            ids.addAll(getServiceLayerIds(layerSettingsDto.getAreaLayers().getSysAreas()));
            ids.addAll(getServiceLayerIds(layerSettingsDto.getAreaLayers().getUserAreas() != null ? Arrays.asList(layerSettingsDto.getAreaLayers().getUserAreas()) : null));
        }

        if (ids.isEmpty()) {
            return; // There is no Areas in the LayersSettings. Returning the call
        }

        List<ServiceLayerEntity> serviceLayers = repository.findServiceLayerEntityByIds(ids); // Get Service layers by all the ids

        //Update the layers
        updateLayer(layerSettingsDto.getAdditionalLayers(), serviceLayers, bingApiKey);
        updateLayer(layerSettingsDto.getBaseLayers(), serviceLayers, bingApiKey);
        updateLayer(layerSettingsDto.getPortLayers(), serviceLayers, bingApiKey);

        if (layerSettingsDto.getAreaLayers() != null) { // Extra null check for AreaLayers before updating system area and user area
            updateLayer(layerSettingsDto.getAreaLayers().getSysAreas(), serviceLayers, bingApiKey);
            updateLayer(layerSettingsDto.getAreaLayers().getUserAreas() != null ? Arrays.asList(layerSettingsDto.getAreaLayers().getUserAreas()) : null, serviceLayers, bingApiKey);

            if (userName != null && layerSettingsDto.getAreaLayers().getUserAreas() != null && includeUserArea) {
                updateAreas(layerSettingsDto.getAreaLayers().getUserAreas(), SpatialTypeEnum.USERAREA, userName);
            } else if (!includeUserArea) {
                layerSettingsDto.getAreaLayers().setUserAreas(null);
            }
        }
    }

    private void updateLayer(List<? extends LayersDto> layers, List<ServiceLayerEntity> serviceLayers, String bingApiKey) {
        if (layers != null) {
            List<LayersDto> layersToExclude = new ArrayList<>();
            for (LayersDto layersDto : layers) {
                for (ServiceLayerEntity serviceLayerEntity : serviceLayers) {
                    if (Long.parseLong(layersDto.getServiceLayerId()) == serviceLayerEntity.getId()) {
                        if (serviceLayerEntity.getProviderFormat().getServiceType().equalsIgnoreCase(PROVIDER_FORMAT_BING) && bingApiKey == null) {
                            layersToExclude.add(layersDto);
                        } else {
                            layersDto.setName(serviceLayerEntity.getName());
                            layersDto.setSubType(serviceLayerEntity.getSubType());
                        }
                        break;
                    }
                }
            }
            layers.removeAll(layersToExclude);
        }
    }

    private void updateAreas(LayerAreaDto layerAreaDto, SpatialTypeEnum spatialTypeEnum, String userName) {
        List<AreaDto> areas = layerAreaDto.getAreaDtos();
        switch (spatialTypeEnum) {
            case USERAREA:
                layerAreaDto.setAreaDtos(getUserAreaDtos(areas, userName));
                break;
        }
    }

    private List<AreaDto> getUserAreaDtos(List<AreaDto> areas, String userName) {
        if (areas != null && !areas.isEmpty()) {
            return repository.findAllUserAreasByGids(getAreaIds(areas));
        } else {
            return repository.getAllUserAreas(userName);
        }
    }

    private List<Long> getAreaIds(List<AreaDto> areas) {
        List<Long> ids = new ArrayList<Long>();
        for (AreaDto areaDto : areas) {
            ids.add(areaDto.getGid());
        }
        return ids;
    }

    private MapDto getMap(ConfigurationDto configurationDto, int reportId, String userName) throws ServiceException {
        ProjectionDto projection = getMapProjection(reportId, configurationDto);
        List<ControlDto> controls = getControls(reportId, configurationDto);
        List<TbControlDto> tbControls = getTbControls(configurationDto);
        ServiceLayersDto layers = getServiceAreaLayer(reportId, configurationDto, projection, userName);
        RefreshDto refreshDto = getRefreshDto(configurationDto);
        return new MapDto(projection, controls, tbControls, layers, refreshDto);
    }

    private ProjectionDto getMapProjection(int reportId, ConfigurationDto configurationDto) {
        List<ProjectionDto> projectionDtoList = repository.findProjectionByMap(reportId);
        if (projectionDtoList != null && !projectionDtoList.isEmpty()) { // Get Map Projection for report
            return projectionDtoList.get(0);
        } else { // If not available use Map Projection configured in USM
            int projectionId = configurationDto.getMapSettings().getMapProjectionId();
            return getProjection(projectionId);
        }
    }

    private List<ControlDto> getControls(int reportId, ConfigurationDto configurationDto) throws ServiceException {
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

    private ServiceLayersDto getServiceAreaLayer(int reportId, ConfigurationDto configurationDto, ProjectionDto projection, String userName) throws ServiceException {
        String geoServerUrl = getGeoServerUrl();
        String bingApiKey = getBingApiKey();
        List<ReportConnectServiceAreasEntity> reportConnectServiceAreas = getReportConnectServiceAreas(reportId, projection, bingApiKey);
        if (reportConnectServiceAreas != null && !reportConnectServiceAreas.isEmpty()) { // If report is having service layer then return it
            List<LayerDto> layerDtos = new ArrayList<LayerDto>();
            for (ReportConnectServiceAreasEntity reportConnectServiceArea : reportConnectServiceAreas) {
                layerDtos.add(reportConnectServiceArea.convertToServiceLayer(geoServerUrl, bingApiKey));
            }
            // TODO fix ServiceLayer Dto return type if the report is configured with service layers
            return null;
        } else { // otherwise get the default layer configuration from USM
           return getServiceAreaLayersFromConfig(configurationDto, geoServerUrl, bingApiKey, projection, userName);

        }
    }

    private VectorStylesDto getVectorStyles(ConfigurationDto configurationDto) {
        return MapConfigMapper.INSTANCE.getStyleDtos(configurationDto.getStylesSettings());
    }

    private VisibilitySettingsDto getVisibilitySettings(ConfigurationDto configurationDto) {
        return configurationDto.getVisibilitySettings();
    }

    private RefreshDto getRefreshDto(ConfigurationDto configurationDto) {
        return new RefreshDto(configurationDto.getMapSettings().getRefreshStatus(), configurationDto.getMapSettings().getRefreshRate());
    }

    private ServiceLayersDto getServiceAreaLayersFromConfig(ConfigurationDto configurationDto, String geoServerUrl, String bingApiKey, ProjectionDto projection, String userName) throws ServiceException {
        ServiceLayersDto serviceLayersDto = new ServiceLayersDto();
        serviceLayersDto.setPortLayers(getLayerDtoList(configurationDto.getLayerSettings().getPortLayers(), geoServerUrl, bingApiKey, projection, false)); // Get Service Layers for Port layers

        if (configurationDto.getLayerSettings().getAreaLayers() != null) {
            serviceLayersDto.setSystemLayers(getLayerDtoList(configurationDto.getLayerSettings().getAreaLayers().getSysAreas(), geoServerUrl, bingApiKey, projection, false)); // Get Service Layers for system layers
            serviceLayersDto.setUserLayer(getUserLayer(configurationDto.getLayerSettings().getAreaLayers().getUserAreas(), configurationDto, geoServerUrl, bingApiKey, projection, userName)); // Get Service layer and areas for user area
        }
        serviceLayersDto.setAdditionalLayers(getLayerDtoList(configurationDto.getLayerSettings().getAdditionalLayers(), geoServerUrl, bingApiKey, projection, false)); // Get Service Layers for Additional layers
        serviceLayersDto.setBaseLayers(getLayerDtoList(configurationDto.getLayerSettings().getBaseLayers(), geoServerUrl, bingApiKey, projection, true)); // Get Service Layers for base layers
        return serviceLayersDto;
    }

    private LayerDto getUserLayer(LayerAreaDto userArea, ConfigurationDto configurationDto, String geoServerUrl, String bingApiKey, ProjectionDto projection, String userName) {
        LayerDto userLayer = null;
        if (userArea != null) {
            List<LayerDto> userLayerDtos = getLayerDtoList(Arrays.asList(userArea), geoServerUrl, bingApiKey, projection, false);
            if (userLayerDtos != null && !userLayerDtos.isEmpty()) {
                userLayer = userLayerDtos.get(0);
                List<AreaDto> areaDtos = getUserAreaDtos(configurationDto.getLayerSettings().getAreaLayers().getUserAreas().getAreaDtos(), userName);
                for(AreaDto areaDto : areaDtos) {
                    areaDto.setDesc(null);
                }
                userLayer.setAreaDto(areaDtos);
            }
        }
        return userLayer;
    }

    private List<LayerDto> getLayerDtoList(List<? extends LayersDto> layersDtos, String geoServerUrl, String bingApiKey, ProjectionDto projection, boolean isBackground) {
        if (layersDtos == null || layersDtos.isEmpty()) {
            return null;
        }
        List<Long> serviceLayerIds = getServiceLayerIds(layersDtos);
        List<ServiceLayerEntity> serviceLayerEntities = sort(getServiceLayers(serviceLayerIds, projection, bingApiKey), serviceLayerIds);
        return getLayerDtos(serviceLayerEntities, geoServerUrl, bingApiKey, isBackground);
    }

    private List<ServiceLayerEntity> sort(List<ServiceLayerEntity> overlayServiceLayerEntities, List<Long> ids) {
        List<ServiceLayerEntity> tempList = new ArrayList<ServiceLayerEntity>();
        for(Long id : ids) {
            for(ServiceLayerEntity serviceLayerEntity : overlayServiceLayerEntities) {
                if (id.equals(serviceLayerEntity.getId())) {
                    tempList.add(serviceLayerEntity);
                }
            }
        }
        return tempList;
    }

    private List<LayerDto> getLayerDtos(List<ServiceLayerEntity> serviceLayerEntities, String geoserverUrl, String bingApiKey, boolean isBaseLayer) {
        List<LayerDto> layerDtos = new ArrayList<LayerDto>();
        for (ServiceLayerEntity serviceLayerEntity : serviceLayerEntities) {
            layerDtos.add(serviceLayerEntity.convertToServiceLayer(geoserverUrl, bingApiKey, isBaseLayer));
        }
        return layerDtos;
    }

    private String getGeoServerUrl() throws ServiceException {
        Map<String, String> parameters = ImmutableMap.<String, String>builder().put(NAME, GEO_SERVER).build();
        return repository.findSystemConfigByName(parameters);
    }

    private String getBingApiKey() throws ServiceException {
        Map<String, String> parameters = ImmutableMap.<String, String>builder().put(NAME, BING_API_KEY).build();
        return repository.findSystemConfigByName(parameters);
    }

    private List<Long> getServiceLayerIds(List<? extends LayersDto> layers) {
        if(layers == null || layers.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> ids = new ArrayList<>();
        for (LayersDto layer : layers) {
            ids.add(Long.parseLong(layer.getServiceLayerId()));
        }
        return ids;
    }

    private List<ReportConnectServiceAreasEntity> getReportConnectServiceAreas(int reportId, ProjectionDto projection, String bingApiKey) {
        List<ReportConnectServiceAreasEntity> reportConnectServiceAreas = repository.findReportConnectServiceAreas(reportId);
        if (reportConnectServiceAreas != null) {
            Iterator<ReportConnectServiceAreasEntity> areaIterator = reportConnectServiceAreas.iterator();
            while (areaIterator.hasNext()) {
                ReportConnectServiceAreasEntity reportConnectServiceArea = areaIterator.next();
                if (isRemoveLayer(projection, reportConnectServiceArea.getServiceLayer(), bingApiKey)) {
                    areaIterator.remove();
                }
            }
            Collections.sort(reportConnectServiceAreas);
        }
        return reportConnectServiceAreas;
    }

    private List<ServiceLayerEntity> getServiceLayers(List<Long> ids, ProjectionDto projection, String bingApiKey) {
        List<ServiceLayerEntity> serviceLayers = repository.findServiceLayerEntityByIds(ids);
        Iterator<ServiceLayerEntity> layerIterator = serviceLayers.iterator();
        while(layerIterator.hasNext()) {
            ServiceLayerEntity serviceLayer = layerIterator.next();
            if (isRemoveLayer(projection, serviceLayer, bingApiKey)) {
                layerIterator.remove();
            }
        }
        return serviceLayers;
    }

    private boolean isRemoveLayer(ProjectionDto projection, ServiceLayerEntity serviceLayer, String bingApiKey) {
        if(!projection.getEpsgCode().equals(DEFAULT_EPSG) && DEFAULT_EPSG.equals(serviceLayer.getSrsCode())) {
            return true;
        }
        if (serviceLayer.getProviderFormat().getServiceType().equalsIgnoreCase(PROVIDER_FORMAT_BING) && bingApiKey == null) {
            return true;
        }
        return false;
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
        ReportConnectSpatialEntity entity = repository.findReportConnectSpatialBy(reportId.longValue());

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

    private ConfigurationDto getAdminConfiguration(String adminPreference) throws IOException {
        return (adminPreference == null || adminPreference.isEmpty()) ? new ConfigurationDto() : getConfiguration(adminPreference);
    }

    private ConfigurationDto getUserConfiguration(String userPreference) throws IOException {
        return (userPreference == null || userPreference.isEmpty()) ? new ConfigurationDto() : getConfiguration(userPreference);
    }

    private ConfigurationDto getConfiguration(String configString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper.readValue(configString, ConfigurationDto.class);
    }

    private String getJson(ConfigurationDto config) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(config);
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
        if (geoServerUrl != null && geoServerUrl != defaultGeoServerUrl) {
            Map<String, String> parameters = ImmutableMap.<String, String>builder().put(NAME, GEO_SERVER).build();
            repository.updateSystemConfig(parameters, geoServerUrl);
        }

        // Update Bing API Key
        String bingApiKey = systemSettingsDto.getBingApiKey();
        if (bingApiKey != null) {
            Map<String, String> parameters = ImmutableMap.<String, String>builder().put(NAME, BING_API_KEY).build();
            repository.updateSystemConfig(parameters, bingApiKey);
        }
    }
}
