/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by padhyad on 1/8/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AreaServiceLayerDto extends ServiceLayerDto {

    @JsonProperty("data")
    private List<AreaDto> data;

    public AreaServiceLayerDto() {}

    public AreaServiceLayerDto(ServiceLayerDto serviceLayerDto, List<AreaDto> data) {
        super(serviceLayerDto.getId(), serviceLayerDto.getName(), serviceLayerDto.getLayerDesc(), serviceLayerDto.getSubType());
        this.data = data;
    }

    @JsonProperty("data")
    public List<AreaDto> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<AreaDto> data) {
        this.data = data;
    }
}