package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigurationDto {

    @JsonProperty("toolSettings")
    private ToolSettingsDto toolSettings;

    @JsonProperty("stylesSettings")
    private StyleSettingsDto stylesSettings;

    @JsonProperty("systemSettings")
    private SystemSettingsDto systemSettings;

    @JsonProperty("layerSettings")
    private LayerSettingsDto layerSettings;

    @JsonProperty("mapSettings")
    private MapSettingsDto mapSettings;

    @JsonProperty("visibilitySettings")
    private VisibilitySettingsDto visibilitySettings;

    public ConfigurationDto() {}

    public ConfigurationDto(ToolSettingsDto toolSettings, StyleSettingsDto stylesSettings, SystemSettingsDto systemSettings, LayerSettingsDto layerSettings, MapSettingsDto mapSettings, VisibilitySettingsDto visibilitySettings) {
        this.toolSettings = toolSettings;
        this.systemSettings = systemSettings;
        this.stylesSettings = stylesSettings;
        this.layerSettings = layerSettings;
        this.mapSettings = mapSettings;
        this.visibilitySettings = visibilitySettings;
    }

    @JsonProperty("toolSettings")
    public ToolSettingsDto getToolSettings() {
        return toolSettings;
    }

    @JsonProperty("toolSettings")
    public void setToolSettings(ToolSettingsDto toolSettings) {
        this.toolSettings = toolSettings;
    }

    @JsonProperty("stylesSettings")
    public StyleSettingsDto getStylesSettings() {
        return stylesSettings;
    }

    @JsonProperty("stylesSettings")
    public void setStylesSettings(StyleSettingsDto stylesSettings) {
        this.stylesSettings = stylesSettings;
    }

    @JsonProperty("systemSettings")
    public SystemSettingsDto getSystemSettings() {
        return systemSettings;
    }

    @JsonProperty("systemSettings")
    public void setSystemSettings(SystemSettingsDto systemSettings) {
        this.systemSettings = systemSettings;
    }

    @JsonProperty("layerSettings")
    public LayerSettingsDto getLayerSettings() {
        return layerSettings;
    }

    @JsonProperty("layerSettings")
    public void setLayerSettings(LayerSettingsDto layerSettings) {
        this.layerSettings = layerSettings;
    }

    @JsonProperty("mapSettings")
    public MapSettingsDto getMapSettings() {
        return mapSettings;
    }

    @JsonProperty("mapSettings")
    public void setMapSettings(MapSettingsDto mapSettings) {
        this.mapSettings = mapSettings;
    }

    @JsonProperty("visibilitySettings")
    public VisibilitySettingsDto getVisibilitySettings() {
        return visibilitySettings;
    }

    @JsonProperty("visibilitySettings")
    public void setVisibilitySettings(VisibilitySettingsDto visibilitySettings) {
        this.visibilitySettings = visibilitySettings;
    }

    @Override
    public String toString() {
        return "ClassPojo [toolSettings = " + toolSettings + ", stylesSettings = " + stylesSettings + ", systemSettings = " + systemSettings + ", layerSettings = " + layerSettings + ", mapSettings = " + mapSettings + "]";
    }
}
