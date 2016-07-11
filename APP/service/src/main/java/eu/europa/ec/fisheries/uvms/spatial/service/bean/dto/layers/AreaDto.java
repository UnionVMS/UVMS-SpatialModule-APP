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

/**
 * Created by padhyad on 1/11/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AreaDto {

    @JsonProperty("gid")
    private Long gid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("desc")
    private String desc;

    public AreaDto() {

    }

    public AreaDto(Long gid, String name, String desc) {
        this.gid = gid;
        this.name = name;
        this.desc = desc;
    }

    @JsonProperty("gid")
    public Long getGid() {
        return gid;
    }

    @JsonProperty("gid")
    public void setGid(Long gid) {
        this.gid = gid;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("desc")
    public String getDesc() {
        return desc;
    }

    @JsonProperty("desc")
    public void setDesc(String desc) {
        this.desc = desc;
    }
}