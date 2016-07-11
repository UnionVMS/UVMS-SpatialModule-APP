/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.ServiceLayerDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "projection",
    "control",
    "tbControl",
    "layers",
    "refresh"
})
public class MapDto {

    @JsonProperty("projection")
    private ProjectionDto projectionDto;
    @JsonProperty("control")
    private List<ControlDto> controlDtos = new ArrayList<ControlDto>();
    @JsonProperty("tbControl")
    private List<TbControlDto> tbControlDtos = new ArrayList<TbControlDto>();
    @JsonProperty("layers")
    private ServiceLayersDto serviceLayersDto;
    @JsonProperty("refresh")
    private RefreshDto refreshDto;

    /**
     * No args constructor for use in serialization
     * 
     */
    public MapDto() {}

    public MapDto(ProjectionDto projectionDto, List<ControlDto> controlDtos, List<TbControlDto> tbControlDtos, ServiceLayersDto serviceLayersDto, RefreshDto refreshDto) {
        this.projectionDto = projectionDto;
        this.controlDtos = controlDtos;
        this.tbControlDtos = tbControlDtos;
        this.serviceLayersDto = serviceLayersDto;
        this.refreshDto = refreshDto;
    }

    @JsonProperty("projection")
    public ProjectionDto getProjectionDto() {
        return projectionDto;
    }

    @JsonProperty("projection")
    public void setProjectionDto(ProjectionDto projectionDto) {
        this.projectionDto = projectionDto;
    }

    @JsonProperty("control")
    public List<ControlDto> getControlDtos() {
        return controlDtos;
    }

    @JsonProperty("control")
    public void setControlDtos(List<ControlDto> controlDtos) {
        this.controlDtos = controlDtos;
    }

    @JsonProperty("tbControl")
    public List<TbControlDto> getTbControlDtos() {
        return tbControlDtos;
    }

    @JsonProperty("tbControl")
    public void setTbControlDtos(List<TbControlDto> tbControlDtos) {
        this.tbControlDtos = tbControlDtos;
    }

    @JsonProperty("layers")
    public ServiceLayersDto getServiceLayers() {
        return serviceLayersDto;
    }

    @JsonProperty("layers")
    public void setServiceLayers(ServiceLayersDto serviceLayersDto) {
        this.serviceLayersDto = serviceLayersDto;
    }

    @JsonProperty("refresh")
    public RefreshDto getRefreshDto() {
        return refreshDto;
    }

    @JsonProperty("refresh")
    public void setRefreshDto(RefreshDto refreshDto) {
        this.refreshDto = refreshDto;
    }
}