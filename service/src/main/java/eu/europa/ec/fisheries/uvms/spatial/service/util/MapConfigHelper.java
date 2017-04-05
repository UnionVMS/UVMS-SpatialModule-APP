/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.LayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.StylesDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.ConfigurationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.LayerAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.LayerSettingsDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.LayersDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.ReferenceDataPropertiesDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.StyleSettingsDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.VisibilitySettingsDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.enums.AreaTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.service.enums.LayerTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapConfigHelper {

    private static final String USER_AREA = "userarea";

    private static final String PROVIDER_FORMAT_BING = "BING";

    private static final String GEOSERVER = "geoserver";

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

    public static boolean isRemoveLayer(ServiceLayerEntity serviceLayer, String bingApiKey) {
        return serviceLayer.getProviderFormat().getServiceType().equalsIgnoreCase(PROVIDER_FORMAT_BING) &&
                (bingApiKey == null || bingApiKey.trim().equals(""));
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

    public static Map<String, ReferenceDataPropertiesDto> getReferenceDataSettings(String referenceData) throws ServiceException {
        if (referenceData == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            Object obj = mapper.readValue(referenceData, Map.class);
            String jsonString = mapper.writeValueAsString(obj);
            return mapper.readValue(jsonString, TypeFactory.defaultInstance().constructMapType(Map.class, String.class, ReferenceDataPropertiesDto.class));

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

    public static String getReferenceDataSettingsJson(Map<String, ReferenceDataPropertiesDto> referenceData) throws ServiceException {
        if (referenceData == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(referenceData);
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

        LayerSettingsDto result = new LayerSettingsDto();

        if (!CollectionUtils.isEmpty(reportConnectServiceArea)) {

            for (ReportConnectServiceAreasEntity layer : reportConnectServiceArea) {

                LayerTypeEnum layerTypeEnum = LayerTypeEnum.getLayerType(layer.getLayerType());

                if (layerTypeEnum == null) {
                    continue;
                }

                switch (layerTypeEnum) {
                    case BASE:
                        LayersDto baseLayersDto = new LayersDto(layer.getServiceLayer().getName(), String.valueOf(layer.getServiceLayer().getId()), layer.getServiceLayer().getSubType(), Long.valueOf(layer.getLayerOrder()));
                        result.addBaseLayer(baseLayersDto);
                        break;
                    case ADDITIONAL:
                        LayersDto additionalLayersDto = new LayersDto(layer.getServiceLayer().getName(), String.valueOf(layer.getServiceLayer().getId()), layer.getServiceLayer().getSubType(), Long.valueOf(layer.getLayerOrder()));
                        result.addAdditionalLayer(additionalLayersDto);
                        break;
                    case PORT:
                        LayersDto portLayersDto = new LayersDto(layer.getServiceLayer().getName(), String.valueOf(layer.getServiceLayer().getId()), layer.getServiceLayer().getSubType(), Long.valueOf(layer.getLayerOrder()));
                        result.addPortLayer(portLayersDto);
                        break;
                    case AREA:
                        addAreaLayer(result, layer);
                        break;
                }
            }
        }
        return result;
    }

    private static void addAreaLayer(LayerSettingsDto layerSettingsDto, ReportConnectServiceAreasEntity layer) {
        AreaTypeEnum areaTypeEnum = AreaTypeEnum.getEnumFromValue(layer.getAreaType());
        LayerAreaDto areaLayersDto = new LayerAreaDto(areaTypeEnum, String.valueOf(layer.getServiceLayer().getId()), (long) layer.getLayerOrder());
        areaLayersDto.setName(layer.getServiceLayer().getName());
        areaLayersDto.setSubType(layer.getServiceLayer().getSubType());

        if (AreaTypeEnum.userarea.equals(areaTypeEnum)) {
            areaLayersDto.setGid(Long.parseLong(layer.getSqlFilter()));
        }
        if (AreaTypeEnum.areagroup.equals(areaTypeEnum)) {
            areaLayersDto.setAreaGroupName(layer.getSqlFilter());
        }
        layerSettingsDto.addAreaLayer(areaLayersDto);
    }

    public static List<Long> getServiceLayerIds(List<? extends LayersDto> layers) {
        if(layers == null || layers.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> ids = new ArrayList<>();
        for (LayersDto layer : layers) {
            String serviceLayerId = layer.getServiceLayerId();
            if (serviceLayerId != null) {
                ids.add(Long.parseLong(serviceLayerId));
            }
        }
        return ids;
    }

    public static List<Long> getUserAreaIds(List<LayerAreaDto> layers) {
        if (layers == null || layers.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> userAreaIds = new ArrayList<>();
        for (LayerAreaDto layerDto : layers) {
            if (layerDto.getAreaType() != null && USER_AREA.equalsIgnoreCase(layerDto.getAreaType().getType())) {
                userAreaIds.add(layerDto.getGid());
            }
        }
        return userAreaIds;
    }

    public static LayerDto convertToServiceLayer(ServiceLayerEntity serviceLayerEntity, String geoServerUrl, String bingApiKey, boolean isBaseLayer, Map<String, ReferenceDataPropertiesDto> referenceData) {
        LayerDto layerDto = new LayerDto();
        String type = serviceLayerEntity.getProviderFormat().getServiceType();
        layerDto.setType(type);
        layerDto.setTitle(serviceLayerEntity.getName());
        layerDto.setIsBaseLayer(isBaseLayer);
        layerDto.setShortCopyright(serviceLayerEntity.getShortCopyright());
        layerDto.setLongCopyright(serviceLayerEntity.getLongCopyright());
        if(!(type.equalsIgnoreCase("OSM") || type.equalsIgnoreCase("OSEA") || type.equalsIgnoreCase("BING"))) {
            layerDto.setUrl(geoServerUrl.concat(serviceLayerEntity.getProviderFormat().getServiceType().toLowerCase()));
        }
        if(type.equalsIgnoreCase("WMS") && !serviceLayerEntity.getIsInternal()) {
            layerDto.setUrl(serviceLayerEntity.getServiceUrl());
        }
        layerDto.setServerType(serviceLayerEntity.getIsInternal() ? GEOSERVER : null);
        layerDto.setLayerGeoName(serviceLayerEntity.getGeoName());
        layerDto.setAreaLocationTypeName(serviceLayerEntity.getAreaType().getTypeName());
        if(!(StringUtils.isEmpty(serviceLayerEntity.getStyleGeom()) && StringUtils.isEmpty(serviceLayerEntity.getStyleLabel()) && StringUtils.isEmpty(serviceLayerEntity.getStyleLabelGeom()))) {
            layerDto.setStyles(new StylesDto(serviceLayerEntity.getStyleGeom(), serviceLayerEntity.getStyleLabel(), serviceLayerEntity.getStyleLabelGeom()));
        }
        if (type.equalsIgnoreCase("BING")) {
            layerDto.setApiKey(bingApiKey);
        }
        setCql(referenceData, layerDto, serviceLayerEntity.getAreaType());
        layerDto.setTypeName(serviceLayerEntity.getAreaType().getTypeName());
        return layerDto;
    }

    private static void setCql(Map<String, ReferenceDataPropertiesDto> referenceData, LayerDto layerDto, AreaLocationTypesEntity areaType) {

        if (areaType != null) {
            for (Map.Entry<String, ReferenceDataPropertiesDto> entry : referenceData.entrySet()) {
                if (areaType.getTypeName().equalsIgnoreCase(entry.getKey())) {
                    ReferenceDataPropertiesDto referenceDataPropertiesDto = entry.getValue();
                    switch (referenceDataPropertiesDto.getSelection()) {
                        case "custom" :
                            if (referenceDataPropertiesDto.getCodes().isEmpty()) {
                                layerDto.setIsWarning(true);
                                layerDto.setCql(null);
                            } else {
                                layerDto.setIsWarning(null);
                                StringBuilder cql = new StringBuilder();
                                cql.append("code in (");
                                cql.append(getConcatenateString(referenceDataPropertiesDto.getCodes()));
                                cql.append(")");
                                layerDto.setCql(cql.toString().replaceAll(", $", ""));
                            }
                            break;
                        case "all" :
                            layerDto.setCql(null);
                            layerDto.setIsWarning(null);
                            break;
                    }

                }
            }
        }
    }

    private static String getConcatenateString(List<String> codes) {
        StringBuilder concatStr = new StringBuilder();
        for (String code : codes) {
            concatStr.append("'" + code + "'" + ",");
        }
        return concatStr.toString().replaceAll(",$", "");
    }

    public static boolean isServiceLayerPermitted(String serviceLayerName, Collection<String> permittedServiceLayers) {
        for (String layer : permittedServiceLayers) {
            if (serviceLayerName.equalsIgnoreCase(layer)) {
                return true;
            }
        }
        return false;
    }
}