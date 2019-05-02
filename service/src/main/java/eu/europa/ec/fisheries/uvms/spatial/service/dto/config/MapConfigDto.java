/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.dto.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.VisibilitySettingsDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "map",
        "vectorStyles"
})
public class MapConfigDto {

    @JsonProperty("map")
    private MapDto map;

    @JsonProperty("vectorStyles")
    private VectorStylesDto vectorStyles;

    @JsonProperty("visibilitySettings")
    private VisibilitySettingsDto visibilitySettings;

    /**
     * No args constructor for use in serialization
     */
    public MapConfigDto() {
    }

    public MapConfigDto(MapDto map, VectorStylesDto vectorStyles, VisibilitySettingsDto visibilitySettings) {
        this.map = map;
        this.vectorStyles = vectorStyles;
        this.visibilitySettings = visibilitySettings;
    }

    @JsonProperty("map")
    public MapDto getMap() {
        return map;
    }

    @JsonProperty("map")
    public void setMap(MapDto map) {
        this.map = map;
    }

    @JsonProperty("vectorStyles")
    public VectorStylesDto getVectorStyles() {
        return vectorStyles;
    }

    @JsonProperty("vectorStyles")
    public void setVectorStyles(VectorStylesDto vectorStyles) {
        this.vectorStyles = vectorStyles;
    }

    @JsonProperty("visibilitySettings")
    public VisibilitySettingsDto getVisibilitySettings() {
        return visibilitySettings;
    }

    @JsonProperty("visibilitySettings")
    public void setVisibilitySettings(VisibilitySettingsDto visibilitySettings) {
        this.visibilitySettings = visibilitySettings;
    }
}