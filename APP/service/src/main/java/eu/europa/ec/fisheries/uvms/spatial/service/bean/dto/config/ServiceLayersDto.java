/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by padhyad on 1/14/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
        "portLayers",
        "additionalLayers",
        "systemLayers",
        "userLayer",
        "baseLayers"
})

public class ServiceLayersDto {

    @JsonProperty("port")
    private List<LayerDto> portLayers;

    @JsonProperty("additional")
    private List<LayerDto> additionalLayers;

    @JsonProperty("baseLayers")
    private List<LayerDto> baseLayers;

    @JsonProperty("areas")
    private List<LayerDto> systemLayers;

    public ServiceLayersDto() {}

    public ServiceLayersDto(List<LayerDto> portLayers,
                            List<LayerDto> additionalLayers,
                            List<LayerDto> baseLayers,
                            List<LayerDto> systemLayers) {
        this.portLayers = portLayers;
        this.additionalLayers = additionalLayers;
        this.baseLayers = baseLayers;
        this.systemLayers = systemLayers;

    }

    @JsonProperty("port")
    public List<LayerDto> getPortLayers() {
        return portLayers;
    }

    @JsonProperty("port")
    public void setPortLayers(List<LayerDto> portLayers) {
        this.portLayers = portLayers;
    }

    @JsonProperty("additional")
    public List<LayerDto> getAdditionalLayers() {
        return additionalLayers;
    }

    @JsonProperty("additional")
    public void setAdditionalLayers(List<LayerDto> additionalLayers) {
        this.additionalLayers = additionalLayers;
    }

    @JsonProperty("baseLayers")
    public List<LayerDto> getBaseLayers() {
        return baseLayers;
    }

    @JsonProperty("baseLayers")
    public void setBaseLayers(List<LayerDto> baseLayers) {
        this.baseLayers = baseLayers;
    }

    @JsonProperty("areas")
    public List<LayerDto> getSystemLayers() {
        return systemLayers;
    }

    @JsonProperty("areas")
    public void setSystemLayers(List<LayerDto> systemLayers) {
        this.systemLayers = systemLayers;
    }
}