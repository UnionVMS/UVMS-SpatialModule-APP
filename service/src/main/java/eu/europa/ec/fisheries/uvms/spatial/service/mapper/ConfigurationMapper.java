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
        mergeMapSettings(source.getMapSettings(), target.getMapSettings());
        return target;
    }

    public static void mergeMapSettings(MapSettingsDto source, MapSettingsDto target) {
        if ( source == null || target == null) {
            return;
        }
        target.setRefreshState(source.getRefreshState() == null ? target.getRefreshState() : source.getRefreshState());
        target.setScaleBarUnits(source.getScaleBarUnits() == null ? target.getScaleBarUnits() : source.getScaleBarUnits());
        target.setCoordinatesFormat(source.getCoordinatesFormat() == null ? target.getCoordinatesFormat() : source.getCoordinatesFormat());
        target.setMapProjection(source.getMapProjection() == null ? target.getMapProjection() : source.getMapProjection());
        target.setRefreshRate(source.getRefreshRate() == null ? target.getRefreshRate() : source.getRefreshRate());
        target.setDisplayProjection(source.getDisplayProjection() == null ? target.getDisplayProjection() : source.getDisplayProjection());
    }
}

