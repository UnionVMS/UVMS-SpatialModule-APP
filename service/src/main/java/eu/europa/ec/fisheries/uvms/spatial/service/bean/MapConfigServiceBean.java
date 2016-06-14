package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportGetStartAndEndDateRS;
import eu.europa.ec.fisheries.uvms.spatial.entity.*;
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
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.*;
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
import java.util.*;

import static eu.europa.ec.fisheries.uvms.spatial.service.mapper.ConfigurationMapper.*;

@Stateless
@Transactional
@Slf4j
public class MapConfigServiceBean implements MapConfigService {

    private static final String SCALE = "scale";
    private static final String MOUSECOORDS = "mousecoords";
    private static final String GEO_SERVER = "geo_server_url";
    private static final String BING_API_KEY = "bing_api_key";
    private static final String PROVIDER_FORMAT_BING = "BING";

    private @EJB SpatialRepository repository;
    @Override
    @SneakyThrows
    public List<ProjectionDto> getAllProjections() {
        List<ProjectionEntity> projections = repository.findProjection();
        return ProjectionMapper.INSTANCE.projectionEntityListToProjectionDtoList(projections);
    }

    private @EJB ReportingService reportingService;

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
    public MapConfigurationType getMapConfigurationType(final Long reportId, Collection<String> permittedServiceLayers) throws ServiceException {
        SpatialValidator.validate(reportId);
        ReportConnectSpatialEntity entity = repository.findReportConnectSpatialByReportId(reportId);
        if (entity == null) {
            return null;
        }
        MapConfigurationType mapConfigurationType = ReportConnectSpatialMapper.INSTANCE.reportConnectSpatialEntityToMapConfigurationType(entity);
        VisibilitySettingsDto visibilitySettings = MapConfigHelper.getVisibilitySettings(entity.getVisibilitySettings());
        mapConfigurationType.setVisibilitySettings(MapConfigMapper.INSTANCE.getVisibilitySettingsType(visibilitySettings));
        StyleSettingsDto styleSettingsDto = MapConfigHelper.getStyleSettings(entity.getStyleSettings());
        mapConfigurationType.setStyleSettings(MapConfigMapper.INSTANCE.getStyleSettingsType(styleSettingsDto));
        LayerSettingsDto layerSettingsDto = MapConfigHelper.getLayerSettingsForMap(entity.getReportConnectServiceAreases());
        updateLayerSettings(layerSettingsDto, null, false, permittedServiceLayers);
        mapConfigurationType.setLayerSettings(MapConfigMapper.INSTANCE.getLayerSettingsType(layerSettingsDto));
        Map<String, ReferenceDataPropertiesDto> referenceData = MapConfigHelper.getReferenceDataSettings(entity.getReferenceData());
        mapConfigurationType.setReferenceDatas(MapConfigMapper.INSTANCE.getReferenceDataType(referenceData));
        return mapConfigurationType;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public SpatialGetMapConfigurationRS getMapConfiguration(SpatialGetMapConfigurationRQ mapConfigurationRQ) throws ServiceException {
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
        updateReportConnectServiceAreasEntity(request, entity, layerSettingsDto);
        Map<String, ReferenceDataPropertiesDto> referenceData = MapConfigMapper.INSTANCE.getReferenceData(request.getMapConfiguration().getReferenceDatas());
        entity.setReferenceData(MapConfigHelper.getReferenceDataSettingsJson(referenceData));
        repository.saveOrUpdateMapConfiguration(entity);
        return createSaveOrUpdateMapConfigurationResponse();
    }

    @Override
    @SneakyThrows
    public ConfigurationDto retrieveAdminConfiguration(String config, Collection<String> permittedServiceLayers) {
        ConfigurationDto configurationDto = MapConfigHelper.getAdminConfiguration(config);
        updateLayerSettings(configurationDto.getLayerSettings(), null,  false, permittedServiceLayers);
        configurationDto.setSystemSettings(getConfigSystemSettings());
        return configurationDto;
    }

    @Override
    @SneakyThrows
    public ConfigurationDto retrieveUserConfiguration(String config, String defaultConfig, String userName, Collection<String> permittedServiceLayers) {
        ConfigurationDto userConfig = MapConfigHelper.getUserConfiguration(config);
        ConfigurationDto adminConfig = MapConfigHelper.getAdminConfiguration(defaultConfig);
        ConfigurationDto mergedConfig = mergeUserConfiguration(adminConfig, userConfig);
        updateLayerSettings(mergedConfig.getLayerSettings(), userName, false, permittedServiceLayers);
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
            updateLayerSettings(defaultNodeConfig.getLayerSettings(), userName, false, permittedServiceLayers);
        }
        return defaultNodeConfig;
    }

    @Override
    @SneakyThrows
    public ConfigDto getReportConfigWithoutMap(String userPref, String adminPref) {
        return mergeNoMapConfiguration(MapConfigHelper.getUserConfiguration(userPref), MapConfigHelper.getAdminConfiguration(adminPref));
    }

    /**
     * returns Report configuration, which includes map configurations, vector style configurations and visibility settings.
     * The method merges all settings taking into account the different available levels of settings - global, user specified and report specific ones.
     * @param reportId
     * @param userPreferences
     * @param adminPreferences
     * @param userName
     * @param scopeName
     * @param timeStamp
     * @param permittedServiceLayer
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
     *  returns Report configuration, which includes map configurations, vector style configurations and visibility settings.
     * The method merges all settings taking into account the different available levels of settings - global, user specified and report specific ones.
     * @param configurationDto
     * @param userName
     * @param scopeName
     * @param permittedServiceLayer
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
        ServiceLayersDto serviceLayersDto = new ServiceLayersDto();
        serviceLayersDto.setBaseLayers(getLayerDtoList(configurationDto.getLayerSettings().getBaseLayers(), projection, true, configurationDto.getReferenceData()));
        return new MapConfigDto(new MapDto(projection, null, null, serviceLayersDto, null), null, null);
    }

    private ReportConnectSpatialEntity getReportConnectSpatialEntity(final SpatialSaveOrUpdateMapConfigurationRQ request) throws ServiceException {
        ReportConnectSpatialEntity entity = repository.findReportConnectSpatialByReportIdAndConnectId(request.getMapConfiguration().getReportId(), request.getMapConfiguration().getSpatialConnectId());

        if (entity != null) {
            entity.setScaleBarType(request.getMapConfiguration().getScaleBarUnits());
            entity.setDisplayFormatType(request.getMapConfiguration().getCoordinatesFormat());
            repository.deleteReportConnectServiceAreas(entity.getReportConnectServiceAreases());
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

    private void updateLayerSettings(LayerSettingsDto layerSettingsDto, String userName, boolean includeUserArea, Collection<String> permittedServiceLayers) throws ServiceException {

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

        if (permittedServiceLayers != null) {
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
        sortLayer(layers);
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
        sortLayer(layers);
    }

    private MapDto getMap(ConfigurationDto configurationDto, Integer reportId, String userName, String scopeName, String timeStamp, Collection<String> permittedServiceLayer) throws ServiceException {
        ProjectionDto projection = getMapProjection(reportId, configurationDto);
        List<ControlDto> controls = getControls(reportId, configurationDto);
        List<TbControlDto> tbControls = getTbControls(configurationDto);
        ServiceLayersDto layers = getServiceAreaLayer(reportId, configurationDto, projection, userName, scopeName, timeStamp, permittedServiceLayer);
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

    private ServiceLayersDto getServiceAreaLayer(Integer reportId, ConfigurationDto configurationDto, ProjectionDto projection, String userName, String scopeName, String timeStamp, Collection<String> permittedServiceLayers) throws ServiceException {

        ReportConnectSpatialEntity entity = null;
        if (reportId != null) {
            entity = repository.findReportConnectSpatialByReportId((long) reportId);
        }
        LayerSettingsDto layerSettingsDto = null;
        Map<String, ReferenceDataPropertiesDto> referenceData = null;

        if (entity != null) {
            layerSettingsDto = (entity.getReportConnectServiceAreases() != null && !entity.getReportConnectServiceAreases().isEmpty()) ?
                    MapConfigHelper.getLayerSettingsForMap(entity.getReportConnectServiceAreases()) : configurationDto.getLayerSettings();
            referenceData = entity.getReferenceData() != null ?
                    MapConfigHelper.getReferenceDataSettings(entity.getReferenceData()) : configurationDto.getReferenceData();
        } else {
            layerSettingsDto = configurationDto.getLayerSettings();
            referenceData = configurationDto.getReferenceData();
        }

        return getServiceAreaLayers(layerSettingsDto, projection, userName, scopeName, timeStamp, reportId, referenceData, configurationDto.getReportProperties(), permittedServiceLayers);
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

    private ServiceLayersDto getServiceAreaLayers(LayerSettingsDto layerSettingsDto, ProjectionDto projection,
                                                  String userName, String scopeName, String timeStamp,
                                                  Integer reportId, Map<String, ReferenceDataPropertiesDto> referenceData,
                                                  ReportProperties reportProperties, Collection<String> allowedServiceLayers) throws ServiceException {
        if (allowedServiceLayers != null) {
            filterAllForbiddenLayers(layerSettingsDto, allowedServiceLayers);
        }

        ServiceLayersDto serviceLayersDto = new ServiceLayersDto();
        serviceLayersDto.setPortLayers(getLayerDtoList(layerSettingsDto.getPortLayers(), projection, false, referenceData)); // Get Service Layers for Port layers
        serviceLayersDto.setSystemLayers(getAreaLayerDtoList(layerSettingsDto.getAreaLayers(), projection, false, userName, scopeName, timeStamp, reportId, referenceData, reportProperties)); // // Get Service Layers for system layers and User Layers
        serviceLayersDto.setAdditionalLayers(getLayerDtoList(layerSettingsDto.getAdditionalLayers(), projection, false, referenceData)); // Get Service Layers for Additional layers
        serviceLayersDto.setBaseLayers(getLayerDtoList(layerSettingsDto.getBaseLayers(), projection, true, referenceData)); // Get Service Layers for base layers
        return serviceLayersDto;
    }

    private void filterAllForbiddenLayers(LayerSettingsDto layerSettingsDto, final Collection<String> permittedLayersNames) {
        filterList(layerSettingsDto.getPortLayers(), permittedLayersNames);
        filterList(layerSettingsDto.getAreaLayers(), permittedLayersNames);
        filterList(layerSettingsDto.getAdditionalLayers(), permittedLayersNames);
        filterList(layerSettingsDto.getBaseLayers(), permittedLayersNames);
    }

    private void filterList(List<? extends LayersDto> layers, final Collection<String> permittedLayersNames) {
        if (layers != null) {
            Iterator<? extends LayersDto> iterator = layers.iterator();
            while (iterator.hasNext()) {
                LayersDto layer = iterator.next();

                if (!permittedLayersNames.contains(layer.getName()) ) {
                    iterator.remove();
                }
            }
        }
    }

    private List<LayerDto> getAreaLayerDtoList(List<LayerAreaDto> layersDtos, ProjectionDto projection,
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
            List<ServiceLayerEntity> serviceLayers = getServiceLayers(Arrays.asList(Long.parseLong(layerAreaDto.getServiceLayerId())), projection, getBingApiKey());
            if (serviceLayers != null && !serviceLayers.isEmpty()) {
                ServiceLayerEntity serviceLayer = serviceLayers.get(0);
                List<LayerDto> layerDtos = getLayerDtos(Arrays.asList(serviceLayer), isBackground, referenceData);
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

    private List<LayerDto> getLayerDtoList(List<? extends LayersDto> layersDtos, ProjectionDto projection,
                                           boolean isBackground, Map<String, ReferenceDataPropertiesDto> referenceData) throws ServiceException {
        if (layersDtos == null || layersDtos.isEmpty()) {
            return null;
        }
        Collections.sort(layersDtos);
        List<Long> serviceLayerIds = MapConfigHelper.getServiceLayerIds(layersDtos);
        List<ServiceLayerEntity> serviceLayerEntities = getServiceLayers(serviceLayerIds, projection, getBingApiKey());
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
        if (bingApiKey != null) {
            repository.updateSystemConfig(BING_API_KEY, bingApiKey);
        }
    }
}