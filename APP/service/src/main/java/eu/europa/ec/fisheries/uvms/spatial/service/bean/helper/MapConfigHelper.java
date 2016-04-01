package eu.europa.ec.fisheries.uvms.spatial.service.bean.helper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.*;
import eu.europa.ec.fisheries.uvms.spatial.util.AreaTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.util.LayerTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by padhyad on 3/31/2016.
 */
public class MapConfigHelper {

    private static final String USER_AREA = "userarea";

    private static final String PROVIDER_FORMAT_BING = "BING";

    private static final Integer DEFAULT_EPSG = 3857;

    private static Logger LOGGER =  LoggerFactory.getLogger(LoggerFactory.class);

    public static boolean isVisibilitySettingsOverriddenByReport(VisibilitySettingsDto visibilitySettingsDto) {
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

    public static boolean isRemoveLayer(ProjectionDto projection, ServiceLayerEntity serviceLayer, String bingApiKey) {
        if(!projection.getEpsgCode().equals(DEFAULT_EPSG) && DEFAULT_EPSG.equals(serviceLayer.getSrsCode())) {
            return true;
        }
        if (serviceLayer.getProviderFormat().getServiceType().equalsIgnoreCase(PROVIDER_FORMAT_BING) && bingApiKey == null) {
            return true;
        }
        return false;
    }

    public static List<ServiceLayerEntity> sortServiceLayers(List<ServiceLayerEntity> serviceLayers, List<Long> ids) {
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

    public static ConfigurationDto getAdminConfiguration(String adminPreference) throws IOException {
        return (adminPreference == null || adminPreference.isEmpty()) ? new ConfigurationDto() : getConfiguration(adminPreference);
    }

    public static ConfigurationDto getUserConfiguration(String userPreference) throws IOException {
        return (userPreference == null || userPreference.isEmpty()) ? new ConfigurationDto() : getConfiguration(userPreference);
    }

    public static VisibilitySettingsDto getVisibilitySettings(String visibilitySettings) throws ServiceException {
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

    public static StyleSettingsDto getStyleSettings(String styleSettings) throws ServiceException {
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

    public static ConfigurationDto getConfiguration(String configString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper.readValue(configString, ConfigurationDto.class);
    }

    public static String getJson(ConfigurationDto config) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(config);
    }

    public static String getVisibilitySettingsJson(VisibilitySettingsDto visibilitySettings) throws ServiceException {
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

    public static String getStyleSettingsJson(StyleSettingsDto styleSettings) throws ServiceException {
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

    public static String getAreaGroupCqlAll(String userName, String scopeName, String areaGroupName) {
        StringBuilder cql = new StringBuilder("type=" + "'" + areaGroupName + "'");
        LOGGER.info("cql All for geo server : \n" + cql);
        return cql.toString();
    }

    public static String getAreaGroupCqlActive(String startDate, String endDate) {
        StringBuilder cql = new StringBuilder();
        if (startDate != null && endDate != null) {
            cql.append("(").
                    append("(").append("start_date IS NULL").append(" AND ").append("end_date IS NULL").append(")").append(" OR ").
                    append("(").append("NOT ( ").append("start_date > ").append("'").append(endDate).append("'").append(" OR ").append("end_date < ").append("'").append(startDate).append("'").append(")").append(")").append(" OR ").
                    append("(").append("start_date IS NULL").append(" AND ").append("end_date >= ").append("'").append(startDate).append("'").append(")").append(" OR ").
                    append("(").append("end_date IS NULL").append(" AND ").append("start_date <= ").append("'").append(endDate).append("'").append(")").
                    append(")");
        } else if (startDate == null && endDate != null) {
            cql.append("(").append("start_date <= ").append("'").append(endDate).append("'").append(" OR ").append("start_date IS NULL").append(")");
        } else {
            return null;
        }
        LOGGER.info("cql Active for geo server : \n" + cql);
        return cql.toString();
    }

    public static LayerSettingsDto getLayerSettingsForMap(Set<ReportConnectServiceAreasEntity> reportConnectServiceArea) {
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

    public static ProjectionEntity createProjection(Long id) {
        ProjectionEntity entity = null;
        if (id != null) {
            entity = new ProjectionEntity(id);
        }
        return entity;
    }

    public static List<Long> getServiceLayerIds(List<? extends LayersDto> layers) {
        if(layers == null || layers.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> ids = new ArrayList<>();
        for (LayersDto layer : layers) {
            ids.add(Long.parseLong(layer.getServiceLayerId()));
        }
        return ids;
    }

    public static List<Long> getUserAreaIds(List<LayerAreaDto> layers) {
        if (layers == null || layers.isEmpty()) {
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
}
