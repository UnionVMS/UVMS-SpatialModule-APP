/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.dto.usm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LayersDto implements Comparable<LayersDto> {

    @JsonProperty("name")
    private String name;

    @JsonProperty("serviceLayerId")
    private String serviceLayerId;

    @JsonProperty("subType")
    private String subType;

    @JsonProperty("order")
    private Long order;

    @JsonIgnore
    private String areaLocationTypeName;

    public LayersDto() {}

    public LayersDto(String name, String serviceLayerId, String subType, Long order) {
        this.name = name;
        this.serviceLayerId = serviceLayerId;
        this.subType = subType;
        this.order = order;
    }

    public LayersDto(String serviceLayerId, Long order) {
        this.serviceLayerId = serviceLayerId;
        this.order = order;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("serviceLayerId")
    public String getServiceLayerId() {
        return serviceLayerId;
    }

    @JsonProperty("serviceLayerId")
    public void setServiceLayerId(String serviceLayerId) {
        this.serviceLayerId = serviceLayerId;
    }

    @Override
    public String toString() {
        return "ClassPojo [type = " + name + ", serviceLayerId = " + serviceLayerId + "]";
    }

    @JsonProperty("subType")
    public String getSubType() {
        return subType;
    }

    @JsonProperty("subType")
    public void setSubType(String subType) {
        this.subType = subType;
    }

    @JsonProperty("order")
    public Long getOrder() {
        return order;
    }

    @JsonProperty("order")
    public void setOrder(Long order) {
        this.order = order;
    }

    public String getAreaLocationTypeName() {
        return areaLocationTypeName;
    }

    public void setAreaLocationTypeName(String areaLocationTypeName) {
        this.areaLocationTypeName = areaLocationTypeName;
    }

    @Override
    public int compareTo(LayersDto layersDto) {
        if (this.getOrder() == null || layersDto.getOrder() == null) {
            return 1;
        }
        return Long.compare(this.getOrder(), layersDto.getOrder());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LayersDto layersDto = (LayersDto) o;

        if (name != null ? !name.equals(layersDto.name) : layersDto.name != null) return false;
        if (serviceLayerId != null ? !serviceLayerId.equals(layersDto.serviceLayerId) : layersDto.serviceLayerId != null)
            return false;
        if (subType != null ? !subType.equals(layersDto.subType) : layersDto.subType != null) return false;
        if (order != null ? !order.equals(layersDto.order) : layersDto.order != null) return false;
        return !(areaLocationTypeName != null ? !areaLocationTypeName.equals(layersDto.areaLocationTypeName) : layersDto.areaLocationTypeName != null);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + serviceLayerId.hashCode();
        result = 31 * result + subType.hashCode();
        result = 31 * result + order.hashCode();
        result = 31 * result + areaLocationTypeName.hashCode();
        return result;
    }
}