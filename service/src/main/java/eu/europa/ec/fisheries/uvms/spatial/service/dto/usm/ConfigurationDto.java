/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigurationDto {

    @NotNull
    @JsonProperty("toolSettings")
    private ToolSettingsDto toolSettings;

    @NotNull
    @JsonProperty("stylesSettings")
    private StyleSettingsDto stylesSettings;

    @NotNull
    @JsonProperty("systemSettings")
    private SystemSettingsDto systemSettings;

    @JsonProperty("layerSettings")
    private LayerSettingsDto layerSettings;

    @NotNull
    @JsonProperty("mapSettings")
    private MapSettingsDto mapSettings;

    @NotNull
    @JsonProperty("visibilitySettings")
    private VisibilitySettingsDto visibilitySettings;

    @JsonProperty("reportProperties")
    private ReportProperties reportProperties;

    @JsonProperty("referenceDataSettings")
    private Map<String, ReferenceDataPropertiesDto> referenceData;

    public ConfigurationDto() {}

    public ConfigurationDto(ToolSettingsDto toolSettings,
                            StyleSettingsDto stylesSettings,
                            SystemSettingsDto systemSettings,
                            LayerSettingsDto layerSettings,
                            MapSettingsDto mapSettings,
                            VisibilitySettingsDto visibilitySettings,
                            ReportProperties reportProperties,
                            Map<String, ReferenceDataPropertiesDto> referenceData) {
        this.toolSettings = toolSettings;
        this.systemSettings = systemSettings;
        this.stylesSettings = stylesSettings;
        this.layerSettings = layerSettings;
        this.mapSettings = mapSettings;
        this.visibilitySettings = visibilitySettings;
        this.reportProperties = reportProperties;
        this.referenceData = referenceData;
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

    @JsonProperty("reportProperties")
    public ReportProperties getReportProperties() {
        return reportProperties;
    }

    @JsonProperty("reportProperties")
    public void setReportProperties(ReportProperties reportProperties) {
        this.reportProperties = reportProperties;
    }

    @JsonProperty("referenceDataSettings")
    public Map<String, ReferenceDataPropertiesDto> getReferenceData() {
        return referenceData;
    }

    @JsonProperty("referenceDataSettings")
    public void setReferenceData(Map<String, ReferenceDataPropertiesDto> referenceData) {
        this.referenceData = referenceData;
    }

    @Override
    public String toString() {
        return "ClassPojo [toolSettings = " + toolSettings + ", stylesSettings = " + stylesSettings + ", systemSettings = " + systemSettings + ", layerSettings = " + layerSettings + ", mapSettings = " + mapSettings + "]";
    }
}