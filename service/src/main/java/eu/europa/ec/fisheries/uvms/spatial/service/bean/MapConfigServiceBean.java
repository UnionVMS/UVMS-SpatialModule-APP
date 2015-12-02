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
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.ConfigurationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.LayersDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.VisibilitySettingsDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.MapConfigMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.ProjectionMapper;
import eu.europa.ec.fisheries.uvms.spatial.validator.SpatialValidator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.spatial.service.mapper.ConfigurationMapper.mergeConfiguration;

@Stateless
@Local(MapConfigService.class)
@Transactional
@Slf4j
public class MapConfigServiceBean implements MapConfigService {

    private static final String SCALE = "scale";

    private static final String MOUSECOORDS = "mousecoords";

    private static final String NAME = "name";

    private static final String GEO_SERVER = "geo_server_url";

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
    @Transactional(Transactional.TxType.REQUIRES_NEW)
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
    public MapConfigDto getReportConfig(int reportId) {
        ConfigurationDto configurationDto = mergeConfiguration(getUserConfiguration(), getAdminConfiguration()); //Returns merged config object between Admin and User configuration from USM
        return new MapConfigDto(getMap(configurationDto, reportId), getVectorStyles(configurationDto), getVisibilitySettings(configurationDto));
    }

    private MapDto getMap(ConfigurationDto configurationDto, int reportId) throws ServiceException {
        return new MapDto(getMapProjection(reportId, configurationDto),
                getControls(reportId, configurationDto),
                getTbControls(configurationDto),
                getServiceAreaLayer(reportId, configurationDto));
    }

    private ProjectionDto getMapProjection(int reportId, ConfigurationDto configurationDto) {
        List<ProjectionDto> projectionDtoList = repository.findProjectionByMap(reportId);
        if (projectionDtoList != null && !projectionDtoList.isEmpty()) { // Get Map Projection for report
            return projectionDtoList.get(0);
        } else { // If not available use Map Projection configured in USM
            String mapSrsCode = configurationDto.getMapSettings().getMapProjection();
            projectionDtoList = repository.findProjectionBySrsCode(Integer.parseInt(mapSrsCode));
            return (projectionDtoList != null && !projectionDtoList.isEmpty()) ? projectionDtoList.get(0) : null;
        }
    }

    private List<ControlDto> getControls(int reportId, ConfigurationDto configurationDto) {
        List<ControlDto> controls = MapConfigMapper.INSTANCE.getControls(configurationDto.getToolSettings().getControl());
        DisplayProjectionDto displayProjection = getDisplayProjection(reportId);
        if (displayProjection != null) {
            return updateControls(controls, displayProjection.getUnits().value(),
                    Integer.toString(displayProjection.getEpsgCode()), displayProjection.getFormats().value());
        } else {
            return updateControls(controls, configurationDto.getMapSettings().getScaleBarUnits(),
                    configurationDto.getMapSettings().getDisplayProjection(), configurationDto.getMapSettings().getCoordinatesFormat());
        }
    }

    private List<TbControlDto> getTbControls(ConfigurationDto configurationDto) {
        return MapConfigMapper.INSTANCE.getTbControls(configurationDto.getToolSettings().getTbControl());
    }

    private List<LayerDto> getServiceAreaLayer(int reportId, ConfigurationDto configurationDto) throws ServiceException {
        List<ReportConnectServiceAreasEntity> reportConnectServiceAreas = getReportConnectServiceAreas(reportId);
        String geoServerUrl = getGeoServerUrl(configurationDto);
        if (reportConnectServiceAreas != null && !reportConnectServiceAreas.isEmpty()) { // If report is having service layer then return it
            List<LayerDto> layerDtos = new ArrayList<LayerDto>();
            for (ReportConnectServiceAreasEntity reportConnectServiceArea : reportConnectServiceAreas) {
                layerDtos.add(reportConnectServiceArea.convertToServiceLayer(geoServerUrl));
            }
            return layerDtos;
        } else { // otherwise get the default layer configuration from USM
            return getServiceAreaLayersFromConfig(configurationDto, geoServerUrl);
        }
    }

    private VectorStylesDto getVectorStyles(ConfigurationDto configurationDto) {
        return MapConfigMapper.INSTANCE.getStyleDtos(configurationDto.getStylesSettings());
    }

    private VisibilitySettingsDto getVisibilitySettings(ConfigurationDto configurationDto) {
        return configurationDto.getVisibilitySettings();
    }

    private List<LayerDto> getServiceAreaLayersFromConfig(ConfigurationDto configurationDto, String geoServerUrl) throws ServiceException {
        List<LayersDto> overlayLayers = configurationDto.getLayerSettings().getOverlayLayers(); // Get Service Layers for Overlay layers
        List<Integer> overlayServiceLayerIds = getServiceLayerIds(overlayLayers);
        List<ServiceLayerEntity> overlayServiceLayerEntities = sort(repository.findServiceLayerEntityByIds(overlayServiceLayerIds), overlayServiceLayerIds);
        List<LayerDto> layerDtos = getLayerDtos(overlayServiceLayerEntities, geoServerUrl, false);

        List<LayersDto> baseLayers = configurationDto.getLayerSettings().getBaseLayers(); // Get Service Layers for base layers
        List<Integer> baseServiceLayerIds = getServiceLayerIds(baseLayers);
        List<ServiceLayerEntity> baseServiceLayerEntities = sort(repository.findServiceLayerEntityByIds(baseServiceLayerIds), baseServiceLayerIds);
        layerDtos.addAll(getLayerDtos(baseServiceLayerEntities, geoServerUrl, true));
        return layerDtos;
    }

    private List<ServiceLayerEntity> sort(List<ServiceLayerEntity> overlayServiceLayerEntities, List<Integer> ids) {
        List<ServiceLayerEntity> tempList = new ArrayList<ServiceLayerEntity>();
        for(Integer id : ids) {
            for(ServiceLayerEntity serviceLayerEntity : overlayServiceLayerEntities) {
                if (id.equals(serviceLayerEntity.getId())) {
                    tempList.add(serviceLayerEntity);
                }
            }
        }
        return tempList;
    }

    private List<LayerDto> getLayerDtos(List<ServiceLayerEntity> serviceLayerEntities, String geoserverUrl, boolean isBaseLayer) {
        List<LayerDto> layerDtos = new ArrayList<LayerDto>();
        for (ServiceLayerEntity serviceLayerEntity : serviceLayerEntities) {
            layerDtos.add(serviceLayerEntity.convertToServiceLayer(geoserverUrl, isBaseLayer));
        }
        return layerDtos;
    }

    private String getGeoServerUrl(ConfigurationDto configurationDto) throws ServiceException {
        Map<String, String> parameters = ImmutableMap.<String, String>builder().put(NAME, GEO_SERVER).build();
        String geoServerUrl = repository.findSystemConfigByName(parameters);
        return geoServerUrl != null ? geoServerUrl : configurationDto.getSystemSettings().getGeoserverUrl();
    }

    private List<Integer> getServiceLayerIds(List<LayersDto> layers) {
        List<Integer> ids = new ArrayList<>();
        for (LayersDto layer : layers) {
            ids.add(Integer.parseInt(layer.getServiceLayerId()));
        }
        return ids;
    }

    private List<ReportConnectServiceAreasEntity> getReportConnectServiceAreas(int reportId) {
        List<ReportConnectServiceAreasEntity> reportConnectServiceAreas = repository.findReportConnectServiceAreas(reportId);
        Collections.sort(reportConnectServiceAreas);
        return reportConnectServiceAreas;
    }

    private List<ControlDto> updateControls(List<ControlDto> controls, String scaleBarUnit, String epsgCode, String coordinateFormat) {
        for (ControlDto controlDto : controls) {
            if (controlDto.getType().equalsIgnoreCase(SCALE)) {
                controlDto.setUnits(scaleBarUnit);
            }
            if (controlDto.getType().equalsIgnoreCase(MOUSECOORDS)) {
                controlDto.setEpsgCode(Integer.parseInt(epsgCode));
                controlDto.setFormat(coordinateFormat);
            }
        }
        return controls;
    }

    private DisplayProjectionDto getDisplayProjection(int reportId) {
        List<DisplayProjectionDto> projectionDtoList = repository.findProjectionByDisplay(reportId);
        return (projectionDtoList != null && !projectionDtoList.isEmpty()) ? projectionDtoList.get(0) : null;
    }

    private ConfigurationDto getAdminConfiguration() throws IOException {
        // TODO call USM
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("Config.json");
        String jsonTxt = IOUtils.toString(is);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper.readValue(jsonTxt, ConfigurationDto.class);
    }

    private ConfigurationDto getUserConfiguration() throws IOException {
        //TODO call USM
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("UserConfig.json");
        String jsonTxt = IOUtils.toString(is);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper.readValue(jsonTxt, ConfigurationDto.class);
    }
}
