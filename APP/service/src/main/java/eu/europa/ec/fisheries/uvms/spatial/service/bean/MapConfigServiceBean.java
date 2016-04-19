package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportGetStartAndEndDateRS;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.mapper.ReportConnectSpatialMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.CoordinatesFormat;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScaleBarUnits;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.service.MapConfigService;
import eu.europa.ec.fisheries.uvms.spatial.service.ReportingService;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ControlDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.DisplayProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.LayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.RefreshDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ServiceLayersDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.TbControlDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.VectorStylesDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.ConfigurationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.LayerAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.LayerSettingsDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.LayersDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.ReportProperties;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.StyleSettingsDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.SystemSettingsDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.VisibilitySettingsDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.helper.MapConfigHelper;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.MapConfigMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.ProjectionMapper;
import eu.europa.ec.fisheries.uvms.spatial.util.AreaTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.util.LayerTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.validator.SpatialValidator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static eu.europa.ec.fisheries.uvms.spatial.service.mapper.ConfigurationMapper.*;

@Stateless
@Transactional
@Slf4j
public class MapConfigServiceBean implements MapConfigService {

    private static final String SCALE = "scale";
    private static final String MOUSECOORDS = "mousecoords";
    private static final String NAME = "name";
    private static final String GEO_SERVER = "geo_server_url";
    private static final String BING_API_KEY = "bing_api_key";
    private static final String PROVIDER_FORMAT_BING = "BING";

    private @EJB SpatialRepository repository;
    private @EJB
    ReportingService reportingService;

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

        for (Long id : request.getSpatialConnectIds()) {
            List<ReportConnectSpatialEntity> entityList = repository.findReportConnectSpatialByConnectId(id);

            ReportConnectSpatialEntity entity = null;

            if (entityList != null && !entityList.isEmpty()) {
                entity = entityList.get(0);
            }

            if (entity != null) {
                repository.deleteReportConnectServiceAreas(entity.getReportConnectServiceAreases());
                repository.deleteEntity(entity);
            }
        }
    }

    @Override
    public MapConfigurationType getMapConfigurationType(final Long reportId) throws ServiceException {
        SpatialValidator.validate(reportId);
        ReportConnectSpatialEntity entity = repository.findReportConnectSpatialBy(reportId);
        if (entity == null) {
            return null;
        }
        MapConfigurationType mapConfigurationType = ReportConnectSpatialMapper.INSTANCE.reportConnectSpatialEntityToMapConfigurationType(entity);
        VisibilitySettingsDto visibilitySettings = MapConfigHelper.getVisibilitySettings(entity.getVisibilitySettings());
        mapConfigurationType.setVisibilitySettings(MapConfigMapper.INSTANCE.getVisibilitySettingsType(visibilitySettings));
        StyleSettingsDto styleSettingsDto = MapConfigHelper.getStyleSettings(entity.getStyleSettings());
        mapConfigurationType.setStyleSettings(MapConfigMapper.INSTANCE.getStyleSettingsType(styleSettingsDto));
        LayerSettingsDto layerSettingsDto = MapConfigHelper.getLayerSettingsForMap(entity.getReportConnectServiceAreases());
        updateLayerSettings(layerSettingsDto, null, false);
        mapConfigurationType.setLayerSettings(MapConfigMapper.INSTANCE.getLayerSettingsType(layerSettingsDto));
        return mapConfigurationType;
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
        ReportConnectSpatialEntity entity = getReportConnectSpatialEntity(request);

        if (entity == null) {
            throw new IllegalArgumentException("MAP CONFIGURATION CAN NOT BE NULL");
        }

        VisibilitySettingsDto visibilitySettings = MapConfigMapper.INSTANCE.getVisibilitySettingsDto(request.getMapConfiguration().getVisibilitySettings());
        entity.setVisibilitySettings(MapConfigHelper.getVisibilitySettingsJson(visibilitySettings));
        StyleSettingsDto styleSettings = MapConfigMapper.INSTANCE.getStyleSettingsDto(request.getMapConfiguration().getStyleSettings());
        entity.setStyleSettings(MapConfigHelper.getStyleSettingsJson(styleSettings));
        LayerSettingsDto layerSettingsDto = MapConfigMapper.INSTANCE.getLayerSettingsDto(request.getMapConfiguration().getLayerSettings());
        updateReportConnectServiceAreasEntity(request, entity, layerSettingsDto);
        repository.saveOrUpdateMapConfiguration(entity);
        return createSaveOrUpdateMapConfigurationResponse();
    }

    @Override
    @SneakyThrows
    public ConfigurationDto retrieveAdminConfiguration(String config) {
        ConfigurationDto configurationDto = MapConfigHelper.getAdminConfiguration(config);
        updateLayerSettings(configurationDto.getLayerSettings(), null, false);
        configurationDto.setSystemSettings(getConfigSystemSettings());
        return configurationDto;
    }

    @Override
    @SneakyThrows
    public ConfigurationDto retrieveUserConfiguration(String config, String defaultConfig, String userName) {
        ConfigurationDto userConfig = MapConfigHelper.getUserConfiguration(config);
        ConfigurationDto adminConfig = MapConfigHelper.getAdminConfiguration(defaultConfig);
        ConfigurationDto mergedConfig = mergeUserConfiguration(adminConfig, userConfig);
        updateLayerSettings(mergedConfig.getLayerSettings(), userName, false);
        return mergedConfig;
    }

    @Override
    @SneakyThrows
    public String saveAdminJson(ConfigurationDto configurationDto, String defaultConfig) {
        ConfigurationDto defaultConfigurationDto = retrieveAdminConfiguration(defaultConfig);
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
    public ConfigurationDto getNodeDefaultValue(ConfigurationDto configurationDto, String adminConfig, String userName) {
        if(configurationDto == null || adminConfig == null) {
            throw new ServiceException("Invalid JSON");
        }
        ConfigurationDto defaultNodeConfig = getDefaultNodeConfiguration(configurationDto, MapConfigHelper.getAdminConfiguration(adminConfig));
        if (configurationDto.getLayerSettings() != null) {
            updateLayerSettings(defaultNodeConfig.getLayerSettings(), userName, false);
        }
        return defaultNodeConfig;
    }

    @Override
    @SneakyThrows
    public ConfigDto getReportConfigWithoutMap(String userPref, String adminPref) {
        return mergeNoMapConfiguration(MapConfigHelper.getUserConfiguration(userPref), MapConfigHelper.getAdminConfiguration(adminPref));
    }

    @Override
    @SneakyThrows
    public MapConfigDto getReportConfig(int reportId, String userPreferences, String adminPreferences, String userName, String scopeName, String timeStamp) {
        ConfigurationDto configurationDto = mergeConfiguration(MapConfigHelper.getUserConfiguration(userPreferences), MapConfigHelper.getAdminConfiguration(adminPreferences)); //Returns merged config object between Admin and User configuration from USM
        return new MapConfigDto(getMap(configurationDto, reportId, userName, scopeName, timeStamp),
                getVectorStyles(configurationDto, reportId),
                getVisibilitySettings(configurationDto, reportId));
    }

    @Override
    @SneakyThrows
    public MapConfigDto getReportConfigWithoutSave(ConfigurationDto configurationDto, String userName, String scopeName) {
        return new MapConfigDto(getMap(configurationDto, null, userName, scopeName, null),
                getVectorStyles(configurationDto, null),
                getVisibilitySettings(configurationDto, null));
    }

    private ReportConnectSpatialEntity getReportConnectSpatialEntity(final SpatialSaveOrUpdateMapConfigurationRQ request) throws ServiceException {
        ReportConnectSpatialEntity entity = repository.findReportConnectSpatialById(request.getMapConfiguration().getReportId(), request.getMapConfiguration().getSpatialConnectId());
        if (entity != null) {
            entity.setScaleBarType(request.getMapConfiguration().getScaleBarUnits());
            entity.setDisplayFormatType(request.getMapConfiguration().getCoordinatesFormat());
            entity.setProjectionByMapProjId(MapConfigHelper.createProjection(request.getMapConfiguration().getMapProjectionId()));
            entity.setProjectionByDisplayProjId(MapConfigHelper.createProjection(request.getMapConfiguration().getDisplayProjectionId()));
            repository.deleteReportConnectServiceAreas(entity.getReportConnectServiceAreases());
        } else {
            entity = ReportConnectSpatialMapper.INSTANCE.mapConfigurationTypeToReportConnectSpatialEntity(request.getMapConfiguration());
        }
        return entity;
    }

    private void updateReportConnectServiceAreasEntity(SpatialSaveOrUpdateMapConfigurationRQ request, ReportConnectSpatialEntity entity, LayerSettingsDto layerSettingsDto) throws ServiceException {
        if(layerSettingsDto == null) {
            clearReportConectServiceAreas(entity);
            return;
        }
        Set<ReportConnectServiceAreasEntity> serviceAreas = createReportConnectServiceAreas(request, entity, layerSettingsDto);
        if (serviceAreas != null && !serviceAreas.isEmpty()) {
            Set<ReportConnectServiceAreasEntity> areas = entity.getReportConnectServiceAreases();
            if (areas == null) {
                entity.setReportConnectServiceAreases(serviceAreas);
            } else {
                areas.clear();
                areas.addAll(serviceAreas);
                entity.setReportConnectServiceAreases(areas);
            }
        } else {
            clearReportConectServiceAreas(entity);
        }
    }

    private void clearReportConectServiceAreas(ReportConnectSpatialEntity entity) {
        Set<ReportConnectServiceAreasEntity> areas = entity.getReportConnectServiceAreases();
        if(areas != null) {
            areas.clear();
            entity.setReportConnectServiceAreases(areas);
        }
    }

    private Set<ReportConnectServiceAreasEntity> createReportConnectServiceAreas(SpatialSaveOrUpdateMapConfigurationRQ request, ReportConnectSpatialEntity reportConnectSpatialEntity, LayerSettingsDto layerSettingsDto) throws ServiceException {
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

    private void updateLayerSettings(LayerSettingsDto layerSettingsDto, String userName, boolean includeUserArea) throws ServiceException {
        String bingApiKey = getBingApiKey();
        List<Long> ids = new ArrayList<>(); // Get All the Ids to query for Service layer all together
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
        updateAreaLayer(layerSettingsDto.getAreaLayers(), serviceLayers, bingApiKey, userName, includeUserArea);

        sortLayer(layerSettingsDto.getAdditionalLayers());
        sortLayer(layerSettingsDto.getBaseLayers());
        sortLayer(layerSettingsDto.getPortLayers());
        sortLayer(layerSettingsDto.getAreaLayers());
    }

    private void sortLayer(List<? extends LayersDto> layers) {
        if(layers != null) {
            Collections.sort(layers);
            for (LayersDto layerDto : layers) {
                layerDto.setOrder(null);
            }
        }
    }


    private void updateAreaLayer(List<LayerAreaDto> layers, List<ServiceLayerEntity> serviceLayers, String bingApiKey, String userName, boolean includeUserArea) throws ServiceException {
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

    private MapDto getMap(ConfigurationDto configurationDto, Integer reportId, String userName, String scopeName, String timeStamp) throws ServiceException {
        ProjectionDto projection = getMapProjection(reportId, configurationDto);
        List<ControlDto> controls = getControls(reportId, configurationDto);
        List<TbControlDto> tbControls = getTbControls(configurationDto);
        ServiceLayersDto layers = getServiceAreaLayer(reportId, configurationDto, projection, userName, scopeName, timeStamp);
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

    private ServiceLayersDto getServiceAreaLayer(Integer reportId, ConfigurationDto configurationDto, ProjectionDto projection, String userName, String scopeName, String timeStamp) throws ServiceException {
        String geoServerUrl = getGeoServerUrl();
        String bingApiKey = getBingApiKey();

        ReportConnectSpatialEntity entity = null;
        if (reportId != null) {
            entity = repository.findReportConnectSpatialBy(new Long(reportId));
        }

        if (entity != null) {
            Set<ReportConnectServiceAreasEntity> reportConnectServiceAreas = entity.getReportConnectServiceAreases(); // Report is overridden with Layer Settings. Ignore USM layer configuration
            if (reportConnectServiceAreas != null && !reportConnectServiceAreas.isEmpty()) {
                LayerSettingsDto layerSettingsDto = MapConfigHelper.getLayerSettingsForMap(entity.getReportConnectServiceAreases());
                return getServiceAreaLayers(layerSettingsDto, geoServerUrl, bingApiKey, projection, userName, scopeName, timeStamp, reportId, configurationDto.getReportProperties());
            } else {
                return getServiceAreaLayers(configurationDto.getLayerSettings(), geoServerUrl, bingApiKey, projection, userName, scopeName, timeStamp, reportId, configurationDto.getReportProperties());
            }
        } else {
            return getServiceAreaLayers(configurationDto.getLayerSettings(), geoServerUrl, bingApiKey, projection, userName, scopeName, timeStamp, reportId, configurationDto.getReportProperties());
        }
    }

    private VectorStylesDto getVectorStyles(ConfigurationDto configurationDto, Integer reportId) throws ServiceException {
        if (reportId != null) {
            ReportConnectSpatialEntity entity = repository.findReportConnectSpatialBy(reportId.longValue());
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
            ReportConnectSpatialEntity entity = repository.findReportConnectSpatialBy(reportId.longValue());
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

    private ServiceLayersDto getServiceAreaLayers(LayerSettingsDto layerSettingsDto, String geoServerUrl, String bingApiKey, ProjectionDto projection, String userName, String scopeName, String timeStamp, Integer reportId, ReportProperties properties) throws ServiceException {
        ServiceLayersDto serviceLayersDto = new ServiceLayersDto();
        serviceLayersDto.setPortLayers(getLayerDtoList(layerSettingsDto.getPortLayers(), geoServerUrl, bingApiKey, projection, false)); // Get Service Layers for Port layers
        serviceLayersDto.setSystemLayers(getAreaLayerDtoList(layerSettingsDto.getAreaLayers(), geoServerUrl, bingApiKey, projection, false, userName, scopeName, timeStamp, reportId, properties)); // // Get Service Layers for system layers and User Layers
        serviceLayersDto.setAdditionalLayers(getLayerDtoList(layerSettingsDto.getAdditionalLayers(), geoServerUrl, bingApiKey, projection, false)); // Get Service Layers for Additional layers
        serviceLayersDto.setBaseLayers(getLayerDtoList(layerSettingsDto.getBaseLayers(), geoServerUrl, bingApiKey, projection, true)); // Get Service Layers for base layers
        return serviceLayersDto;
    }

    private List<LayerDto> getAreaLayerDtoList(List<LayerAreaDto> layersDtos, String geoServerUrl, String bingApiKey, ProjectionDto projection, boolean isBackground, String userName, String scopeName, String timeStamp, Integer reportId, ReportProperties properties) throws ServiceException {
        if (layersDtos == null || layersDtos.isEmpty()) {
            return null;
        }
        Collections.sort(layersDtos);
        List<LayerDto> layerDtoList = new ArrayList<>();
        for (LayerAreaDto layerAreaDto : layersDtos) {
            List<ServiceLayerEntity> serviceLayers = getServiceLayers(Arrays.asList(Long.parseLong(layerAreaDto.getServiceLayerId())), projection, bingApiKey);
            if (serviceLayers != null && !serviceLayers.isEmpty()) {
                ServiceLayerEntity serviceLayer = serviceLayers.get(0);
                List<LayerDto> layerDtos = getLayerDtos(Arrays.asList(serviceLayer), geoServerUrl, bingApiKey, isBackground);
                if (layerDtos != null && !layerDtos.isEmpty()) {
                    LayerDto layerDto = layerDtos.get(0);
                    if (layerAreaDto.getAreaType().equals(AreaTypeEnum.userarea)) {
                        List<AreaDto> userAreas = repository.findAllUserAreasByGids(Arrays.asList(layerAreaDto.getGid()));
                        layerDto.setGid((userAreas != null & !userAreas.isEmpty()) ? userAreas.get(0).getGid() : null);
                        layerDto.setTitle((userAreas != null & !userAreas.isEmpty()) ? userAreas.get(0).getName() : null);
                        layerDto.setAreaType(AreaTypeEnum.userarea.getType().toUpperCase());
                    } else if (layerAreaDto.getAreaType().equals(AreaTypeEnum.areagroup)) {
                        layerDto.setCqlAll(MapConfigHelper.getAreaGroupCqlAll(userName, scopeName, layerAreaDto.getAreaGroupName()));
                        if (reportId != null) {
                            layerDto.setCqlActive(getAreaGroupCqlActive(reportId, userName, scopeName, timeStamp));
                        } else {
                            layerDto.setCqlActive(MapConfigHelper.getAreaGroupCqlActive(properties.getStartDate(), properties.getEndDate()));
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

    private List<LayerDto> getLayerDtoList(List<? extends LayersDto> layersDtos, String geoServerUrl, String bingApiKey, ProjectionDto projection, boolean isBackground) {
        if (layersDtos == null || layersDtos.isEmpty()) {
            return null;
        }
        Collections.sort(layersDtos);
        List<Long> serviceLayerIds = MapConfigHelper.getServiceLayerIds(layersDtos);
        List<ServiceLayerEntity> serviceLayerEntities = getServiceLayers(serviceLayerIds, projection, bingApiKey);
        return getLayerDtos(serviceLayerEntities, geoServerUrl, bingApiKey, isBackground);
    }

    private List<LayerDto> getLayerDtos(List<ServiceLayerEntity> serviceLayerEntities, String geoserverUrl, String bingApiKey, boolean isBaseLayer) {
        List<LayerDto> layerDtos = new ArrayList<>();
        for (ServiceLayerEntity serviceLayerEntity : serviceLayerEntities) {
            layerDtos.add(serviceLayerEntity.convertToServiceLayer(geoserverUrl, bingApiKey, isBaseLayer));
        }
        return layerDtos;
    }

    private String getGeoServerUrl() throws ServiceException {
        return repository.findSystemConfigByName(GEO_SERVER);
    }

    private String getBingApiKey() throws ServiceException {
        return repository.findSystemConfigByName(BING_API_KEY);
    }

    private List<ServiceLayerEntity> getServiceLayers(List<Long> ids, ProjectionDto projection, String bingApiKey) {
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
            entity = repository.findReportConnectSpatialBy(reportId.longValue());
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
        if (bingApiKey != null) {
            repository.updateSystemConfig(BING_API_KEY, bingApiKey);
        }
    }
}