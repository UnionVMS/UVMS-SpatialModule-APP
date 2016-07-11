/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by padhyad on 1/11/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AreaLayersDto {

    @JsonProperty("sysAreas")
    private List<LayerAreaDto> sysAreas;

    @JsonProperty("userAreas")
    private LayerAreaDto userAreas;

    public AreaLayersDto() {
    }

    public AreaLayersDto(List<LayerAreaDto> portLayers, LayerAreaDto userAreas) {
        this.sysAreas = portLayers;
        this.userAreas = userAreas;
    }

    @JsonProperty("sysAreas")
    public List<LayerAreaDto> getSysAreas() {
        return sysAreas;
    }

    @JsonProperty("sysAreas")
    public void setSysAreas(List<LayerAreaDto> sysAreas) {
        this.sysAreas = sysAreas;
    }

    @JsonProperty("userAreas")
    public LayerAreaDto getUserAreas() {
        return userAreas;
    }

    @JsonProperty("userAreas")
    public void setUserAreas(LayerAreaDto userAreas) {
        this.userAreas = userAreas;
    }
}