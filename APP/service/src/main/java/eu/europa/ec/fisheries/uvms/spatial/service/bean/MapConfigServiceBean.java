package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.mapper.ReportConnectSpatialMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.LayerRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.*;
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
import java.io.IOException;
import java.util.*;

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

    private static final Integer DEFAULT_EPSG = 3857;

    private static final String USER_AREA = "userarea";

    @EJB
    private SpatialRepository repository;

    @EJB
    private LayerRepository layer;

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
        if (entity == null) {
            return null;
        }
        MapConfigurationType mapConfigurationType = ReportConnectSpatialMapper.INSTANCE.reportConnectSpatialEntityToMapConfigurationType(entity);
        VisibilitySettingsDto visibilitySettings = getVisibilitySettings(entity.getVisibilitySettings());
        mapConfigurationType.setVisibilitySettings(MapConfigMapper.INSTANCE.getVisibilitySettingsType(visibilitySettings));
        StyleSettingsDto styleSettingsDto = getStyleSettings(entity.getStyleSettings());
        mapConfigurationType.setStyleSettings(MapConfigMapper.INSTANCE.getStyleSettingsType(styleSettingsDto));
        LayerSettingsDto layerSettingsDto = getLayerSettingsForMap(entity.getReportConnectServiceAreases());
        updateLayerSettings(layerSettingsDto, null, false);
        mapConfigurationType.setLayerSettings(MapConfigMapper.INSTANCE.getLayerSettingsType(layerSettingsDto));
        return mapConfigurationType;
    }

    private LayerSettingsDto getLayerSettingsForMap(Set<ReportConnectServiceAreasEntity> reportConnectServiceArea) {
        LayerSettingsDto layerSettingsDto = new LayerSettingsDto();
        for (ReportConnectServiceAreasEntity layer : reportConnectServiceArea) {
            LayerTypeEnum layerTypeEnum = LayerTypeEnum.getLayerType(layer.getLayerType());
            switch (layerTypeEnum) {
                case BASE:
                    LayersDto baseLayersDto = new LayersDto(String.valueOf(layer.getServiceLayer().getId()), Long.valueOf(layer.getLayerOrder()));
                    layerSettingsDto.addBaseLayer(baseLayersDto);
                    break;
                case ADDITIONAL:
                    LayersDto additionalLayersDto = new LayersDto(String.valueOf(layer.getServiceLayer().getId()), Long.valueOf(layer.getLayerOrder()));
                    layerSettingsDto.addAdditionalLayer(additionalLayersDto);
                    break;
                case PORT:
                    LayersDto portLayersDto = new LayersDto(String.valueOf(layer.getServiceLayer().getId()), Long.valueOf(layer.getLayerOrder()));
                    layerSettingsDto.addPortLayer(portLayersDto);
                    break;
                case AREA:
                    AreaTypeEnum areaTypeEnum = AreaTypeEnum.valueOf(layer.getAreaType());
                    LayerAreaDto areaLayersDto = new LayerAreaDto(areaTypeEnum, String.valueOf(layer.getServiceLayer().getId()), Long.valueOf(layer.getLayerOrder()));
                    if (areaTypeEnum.equals(AreaTypeEnum.userarea)) {
                        areaLayersDto.setGid(Long.parseLong(layer.getSqlFilter()));
                    }
                    if (areaTypeEnum.equals(AreaTypeEnum.areagroup)) {
                        areaLayersDto.setAreaGroupName(layer.getSqlFilter());
                    }
                    layerSettingsDto.addAreaLayer(areaLayersDto);
                    break;
            }
        }
        return layerSettingsDto;
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
        VisibilitySettingsDto visibilitySettings = MapConfigMapper.INSTANCE.getVisibilitySettingsDto(request.getMapConfiguration().getVisibilitySettings());
        entity.setVisibilitySettings(getVisibilitySettingsJson(visibilitySettings));
        StyleSettingsDto styleSettings = MapConfigMapper.INSTANCE.getStyleSettingsDto(request.getMapConfiguration().getStyleSettings());
        entity.setStyleSettings(getStyleSettingsJson(styleSettings));
        LayerSettingsDto layerSettingsDto = MapConfigMapper.INSTANCE.getLayerSettingsDto(request.getMapConfiguration().getLayerSettings());
        updateReportConnectServiceAreasEntity(request, entity, layerSettingsDto);
        repository.saveOrUpdateMapConfiguration(entity);
        return createSaveOrUpdateMapConfigurationResponse();
    }
    
    private ReportConnectSpatialEntity getReportConnectSpatialEntity(final SpatialSaveOrUpdateMapConfigurationRQ request) throws ServiceException {
        ReportConnectSpatialEntity entity = repository.findReportConnectSpatialById(request.getMapConfiguration().getReportId(), request.getMapConfiguration().getSpatialConnectId());
        if (entity != null) {
            entity.setScaleBarType(request.getMapConfiguration().getScaleBarUnits());
            entity.setDisplayFormatType(request.getMapConfiguration().getCoordinatesFormat());
            entity.setProjectionByMapProjId(createProjection(request.getMapConfiguration().getMapProjectionId()));
            entity.setProjectionByDisplayProjId(createProjection(request.getMapConfiguration().getDisplayProjectionId()));
            repository.deleteReportConnectServiceAreas(entity.getReportConnectServiceAreases());
        } else {
            entity = ReportConnectSpatialMapper.INSTANCE.mapConfigurationTypeToReportConnectSpatialEntity(request.getMapConfiguration());
        }
        return entity;
    }

    ProjectionEntity createProjection(Long id) {
        ProjectionEntity entity = null;
        if (id != null) {
            entity = new ProjectionEntity(id);
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
            } else if(areas != null) {
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
        boolean isLayersOverridden = false;
        if (userConfig.getLayerSettings() != null) {
            isLayersOverridden = true;
        }
        updateLayerSettings(mergedConfig.getLayerSettings(), userName, !isLayersOverridden);
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
        return new MapConfigDto(getMap(configurationDto, reportId, userName), getVectorStyles(configurationDto, reportId), getVisibilitySettings(configurationDto, reportId));
    }

    private void updateLayerSettings(LayerSettingsDto layerSettingsDto, String userName, boolean includeUserArea) throws ServiceException {
        String bingApiKey = getBingApiKey();
        List<Long> ids =  new ArrayList<Long>(); // Get All the Ids to query for Service layer all together
        ids.addAll(getServiceLayerIds(layerSettingsDto.getAdditionalLayers()));
        ids.addAll(getServiceLayerIds(layerSettingsDto.getBaseLayers()));
        ids.addAll(getServiceLayerIds(layerSettingsDto.getPortLayers()));
        ids.addAll(getServiceLayerIds(layerSettingsDto.getAreaLayers()));

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

    private List<Long> getUserAreaIds(List<LayerAreaDto> layers) {
        if (layers == null || !layers.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> userAreaIds = new ArrayList<>();
        for (LayerAreaDto layerDto : layers) {
            if (layerDto.getAreaType().getType().equalsIgnoreCase(USER_AREA)) {
                userAreaIds.add(layerDto.getGid());
            }
        }
        return userAreaIds;
    }

    private void updateAreaLayer(List<LayerAreaDto> layers, List<ServiceLayerEntity> serviceLayers, String bingApiKey, String userName, boolean includeUserArea) throws ServiceException {
        List<Long> userAreaIds = getUserAreaIds(layers);
        if (!userAreaIds.isEmpty()) {
            List<AreaDto> areaDtos = repository.findAllUserAreasByGids(userAreaIds);
            for (LayerAreaDto layerDto : layers) {
                for (AreaDto areaDto :  areaDtos) {
                    if (layerDto.getGid() == areaDto.getGid()) {
                        layerDto.setAreaName(areaDto.getName());
                        layerDto.setAreaDesc(areaDto.getDesc());
                    }
                }
            }
        } else if (userName != null && includeUserArea) {
            List<AreaDto> areaDtos = repository.getAllUserAreas(userName);
            ServiceLayerEntity serviceLayerEntity = layer.getServiceLayerBy(USER_AREA.toUpperCase());
            for (AreaDto areaDto :  areaDtos) {
                LayerAreaDto layerAreaDto = new LayerAreaDto();
                layerAreaDto.setGid(areaDto.getGid());
                layerAreaDto.setAreaName(areaDto.getName());
                layerAreaDto.setAreaDesc(areaDto.getDesc());
                layerAreaDto.setAreaType(AreaTypeEnum.userarea);
                layerAreaDto.setServiceLayerId(Long.toString(serviceLayerEntity.getId()));
                layerAreaDto.setName(serviceLayerEntity.getName());
                layerAreaDto.setSubType(serviceLayerEntity.getSubType());
                layers.add(layerAreaDto);
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

    private VectorStylesDto getVectorStyles(ConfigurationDto configurationDto, Integer reportId) throws ServiceException {
        ReportConnectSpatialEntity entity = repository.findReportConnectSpatialBy(reportId.longValue());
        if (entity != null && entity.getStyleSettings() != null) {
            StyleSettingsDto styleSettingsDto = getStyleSettings(entity.getStyleSettings());
            if ((styleSettingsDto.getPositions() != null && styleSettingsDto.getPositions().getStyle() != null) ||
                    (styleSettingsDto.getSegments() != null && styleSettingsDto.getSegments().getStyle() != null)) {
                return MapConfigMapper.INSTANCE.getStyleDtos(styleSettingsDto); // Style Settings is overridden by Report. Return the report configured style settings
            }
        }
        return MapConfigMapper.INSTANCE.getStyleDtos(configurationDto.getStylesSettings()); // Return merged style settings from Admin and User config
    }

    private VisibilitySettingsDto getVisibilitySettings(ConfigurationDto configurationDto, Integer reportId) throws ServiceException {
        ReportConnectSpatialEntity entity = repository.findReportConnectSpatialBy(reportId.longValue());
        if (entity != null && entity.getVisibilitySettings() != null) {
            VisibilitySettingsDto visibilitySettingsDto = getVisibilitySettings(entity.getVisibilitySettings());
            if (isVisibilitySettingsOverriddenByReport(visibilitySettingsDto)) {
                return visibilitySettingsDto; // VisibilitySettings is overridden by Report. Return the report configured visibility settings
            }
        }
        return configurationDto.getVisibilitySettings(); // Return merged visibility settings from Admin and User Config
    }

    private boolean isVisibilitySettingsOverriddenByReport(VisibilitySettingsDto visibilitySettingsDto) {
        boolean isOverridden = false;
        if (visibilitySettingsDto.getVisibilityPositionsDto() != null) {
            if (visibilitySettingsDto.getVisibilityPositionsDto().getLabels() != null && (visibilitySettingsDto.getVisibilityPositionsDto().getLabels().getOrder() != null || visibilitySettingsDto.getVisibilityPositionsDto().getLabels().getValues() != null)) {
                isOverridden = true;
            }
            if (visibilitySettingsDto.getVisibilityPositionsDto().getPopup() != null && (visibilitySettingsDto.getVisibilityPositionsDto().getPopup().getOrder() != null || visibilitySettingsDto.getVisibilityPositionsDto().getPopup().getValues() != null)) {
                isOverridden = true;
            }
            if (visibilitySettingsDto.getVisibilityPositionsDto().getTable() != null && (visibilitySettingsDto.getVisibilityPositionsDto().getTable().getOrder() != null || visibilitySettingsDto.getVisibilityPositionsDto().getTable().getValues() != null)) {
                isOverridden = true;
            }
        }
        if (visibilitySettingsDto.getVisibilitySegmentDto() != null) {
            if (visibilitySettingsDto.getVisibilitySegmentDto().getLabels() != null && (visibilitySettingsDto.getVisibilitySegmentDto().getLabels().getOrder() != null || visibilitySettingsDto.getVisibilitySegmentDto().getLabels().getValues() != null)) {
                isOverridden = true;
            }
            if (visibilitySettingsDto.getVisibilitySegmentDto().getPopup() != null && (visibilitySettingsDto.getVisibilitySegmentDto().getPopup().getOrder() != null || visibilitySettingsDto.getVisibilitySegmentDto().getPopup().getValues() != null)) {
                isOverridden = true;
            }
            if (visibilitySettingsDto.getVisibilitySegmentDto().getTable() != null && (visibilitySettingsDto.getVisibilitySegmentDto().getTable().getOrder() != null || visibilitySettingsDto.getVisibilitySegmentDto().getTable().getValues() != null)) {
                isOverridden = true;
            }
        }
        if (visibilitySettingsDto.getVisibilityTracksDto() != null) {
            if (visibilitySettingsDto.getVisibilityTracksDto().getTable() != null && (visibilitySettingsDto.getVisibilityTracksDto().getTable().getOrder() != null || visibilitySettingsDto.getVisibilityTracksDto().getTable().getValues() != null)) {
                isOverridden = true;
            }
        }
        return isOverridden;
    }

    private RefreshDto getRefreshDto(ConfigurationDto configurationDto) {
        return new RefreshDto(configurationDto.getMapSettings().getRefreshStatus(), configurationDto.getMapSettings().getRefreshRate());
    }

    private ServiceLayersDto getServiceAreaLayersFromConfig(ConfigurationDto configurationDto, String geoServerUrl, String bingApiKey, ProjectionDto projection, String userName) throws ServiceException {
        ServiceLayersDto serviceLayersDto = new ServiceLayersDto();
        serviceLayersDto.setPortLayers(getLayerDtoList(configurationDto.getLayerSettings().getPortLayers(), geoServerUrl, bingApiKey, projection, false)); // Get Service Layers for Port layers
        serviceLayersDto.setSystemLayers(getAreaLayerDtoList(configurationDto.getLayerSettings().getAreaLayers(), geoServerUrl, bingApiKey, projection, false)); // // Get Service Layers for system layers and User Layers
        serviceLayersDto.setAdditionalLayers(getLayerDtoList(configurationDto.getLayerSettings().getAdditionalLayers(), geoServerUrl, bingApiKey, projection, false)); // Get Service Layers for Additional layers
        serviceLayersDto.setBaseLayers(getLayerDtoList(configurationDto.getLayerSettings().getBaseLayers(), geoServerUrl, bingApiKey, projection, true)); // Get Service Layers for base layers
        return serviceLayersDto;
    }

    private List<LayerDto> getAreaLayerDtoList(List<LayerAreaDto> layersDtos, String geoServerUrl, String bingApiKey, ProjectionDto projection, boolean isBackground) {
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
                        layerDto.setName((userAreas != null & !userAreas.isEmpty()) ? userAreas.get(0).getName() : null);
                    } else if (layerAreaDto.getAreaType().equals(AreaTypeEnum.areagroup)) {
                        layerDto.setCql("type=" + layerAreaDto.getAreaGroupName());
                    }
                    layerDtoList.add(layerDto);
                }
            }
        }
        return layerDtoList;
    }

    private List<LayerDto> getLayerDtoList(List<? extends LayersDto> layersDtos, String geoServerUrl, String bingApiKey, ProjectionDto projection, boolean isBackground) {
        if (layersDtos == null || layersDtos.isEmpty()) {
            return null;
        }
        Collections.sort(layersDtos);
        List<Long> serviceLayerIds = getServiceLayerIds(layersDtos);
        List<ServiceLayerEntity> serviceLayerEntities = getServiceLayers(serviceLayerIds, projection, bingApiKey);
        return getLayerDtos(serviceLayerEntities, geoServerUrl, bingApiKey, isBackground);
    };

    private List<LayerDto> getLayerDtos(List<ServiceLayerEntity> serviceLayerEntities, String geoserverUrl, String bingApiKey, boolean isBaseLayer) {
        List<LayerDto> layerDtos = new ArrayList<>();
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
        List<ServiceLayerEntity> serviceLayers = sortServiceLayers(repository.findServiceLayerEntityByIds(ids), ids);
        Iterator<ServiceLayerEntity> layerIterator = serviceLayers.iterator();
        while(layerIterator.hasNext()) {
            ServiceLayerEntity serviceLayer = layerIterator.next();
            if (isRemoveLayer(projection, serviceLayer, bingApiKey)) {
                layerIterator.remove();
            }
        }
        return serviceLayers;
    }

    public List<ServiceLayerEntity> sortServiceLayers(List<ServiceLayerEntity> serviceLayers, List<Long> ids) {
        List<ServiceLayerEntity> sortedServiceLayers = new ArrayList<>();
        if (serviceLayers!= null && ids != null) {
            for (Long id : ids) {
                for (ServiceLayerEntity serviceLayerEntity : serviceLayers) {
                    if (id == serviceLayerEntity.getId()) {
                        sortedServiceLayers.add(serviceLayerEntity);
                    }
                }
            }
        }
        return sortedServiceLayers;
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

    private VisibilitySettingsDto getVisibilitySettings(String visibilitySettings) throws ServiceException {
        if (visibilitySettings == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            return mapper.readValue(visibilitySettings, VisibilitySettingsDto.class);
        } catch (IOException e) {
            throw new ServiceException("Parse Exception from Json to Object", e);
        }
    }

    private StyleSettingsDto getStyleSettings(String styleSettings) throws ServiceException {
        if (styleSettings == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            return mapper.readValue(styleSettings, StyleSettingsDto.class);
        } catch (IOException e) {
            throw new ServiceException("Parse Exception from Json to Object", e);
        }
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

    private String getVisibilitySettingsJson(VisibilitySettingsDto visibilitySettings) throws ServiceException {
        if (visibilitySettings == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(visibilitySettings);
        } catch (IOException e) {
            throw new ServiceException("Parse Exception from Object to json", e);
        }
    }

    private String getStyleSettingsJson(StyleSettingsDto styleSettings) throws ServiceException {
        if (styleSettings == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(styleSettings);
        } catch (IOException e) {
            throw new ServiceException("Parse Exception from Object to json", e);
        }
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
