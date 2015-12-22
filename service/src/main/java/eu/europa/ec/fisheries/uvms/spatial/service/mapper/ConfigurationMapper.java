package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.ConfigurationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.MapSettingsDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.SystemSettingsDto;

/**
 * Created by padhyad on 11/26/2015.
 */
public class ConfigurationMapper {

    public static ConfigurationDto mergeConfiguration(ConfigurationDto source, ConfigurationDto target) {
        if ( source == null || target == null) {
            return target;
        }
        target.setSystemSettings((source.getSystemSettings() == null ? target.getSystemSettings() : source.getSystemSettings()));
        target.setStylesSettings(source.getStylesSettings() == null ? target.getStylesSettings() : source.getStylesSettings());
        target.setToolSettings(source.getToolSettings() == null ? target.getToolSettings() : source.getToolSettings());
        target.setLayerSettings(source.getLayerSettings() == null ? target.getLayerSettings() : source.getLayerSettings());
        target.setVisibilitySettings(source.getVisibilitySettings() == null ? target.getVisibilitySettings() : source.getVisibilitySettings());
        target.setMapSettings(source.getMapSettings() == null ? target.getMapSettings() : source.getMapSettings());
        return target;
    }

    public static void mergeMapSettings(MapSettingsDto source, MapSettingsDto target) {
        if ( source == null || target == null) {
            return;
        }
        target.setRefreshStatus(source.getRefreshStatus());
        target.setScaleBarUnits(source.getScaleBarUnits() == null ? target.getScaleBarUnits() : source.getScaleBarUnits());
        target.setCoordinatesFormat(source.getCoordinatesFormat() == null ? target.getCoordinatesFormat() : source.getCoordinatesFormat());
        target.setMapProjectionId(source.getMapProjectionId());
        target.setRefreshRate(source.getRefreshRate());
        target.setDisplayProjectionId(source.getDisplayProjectionId());
    }

    public static ConfigurationDto mergeUserConfiguration(ConfigurationDto source, ConfigurationDto target) {
        if ( source == null || target == null) {
            return target;
        }
        target.setStylesSettings(target.getStylesSettings() == null ? source.getStylesSettings() : target.getStylesSettings());
        target.setVisibilitySettings(target.getVisibilitySettings() == null ? source.getVisibilitySettings() : target.getVisibilitySettings());
        target.setMapSettings(target.getMapSettings() == null ? source.getMapSettings() : target.getMapSettings());
        return target;
    }

    public static ConfigurationDto resetUserConfiguration(ConfigurationDto source, ConfigurationDto target) {
        if ( source == null || target == null) {
            return target;
        }
        target.setStylesSettings(source.getStylesSettings() != null ? null : target.getStylesSettings());
        target.setVisibilitySettings(source.getVisibilitySettings() != null ? null : target.getVisibilitySettings());
        target.setMapSettings(source.getMapSettings() != null ? null : target.getMapSettings());
        return target;
    }

    public static ConfigurationDto getDefaultNodeConfiguration(ConfigurationDto configurationDto, ConfigurationDto adminConfigurationDto) {
        ConfigurationDto defaultNodeConfigurationDto = new ConfigurationDto();
        defaultNodeConfigurationDto.setStylesSettings(configurationDto.getStylesSettings() != null ? adminConfigurationDto.getStylesSettings() : null);
        defaultNodeConfigurationDto.setVisibilitySettings(configurationDto.getVisibilitySettings() != null ? adminConfigurationDto.getVisibilitySettings() : null);
        defaultNodeConfigurationDto.setMapSettings(configurationDto.getMapSettings() != null ? adminConfigurationDto.getMapSettings() : null);
        return defaultNodeConfigurationDto;
    }
}

