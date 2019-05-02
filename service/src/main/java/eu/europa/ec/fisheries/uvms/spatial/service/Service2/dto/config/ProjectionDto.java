/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "epsgCode",
        "units",
        "formats",
        "global",
        "extent",
        "axis"
})
public class ProjectionDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("epsgCode")
    private Integer epsgCode;

    @JsonProperty("formats")
    private String formats;

    @JsonProperty("units")
    private String units;

    @JsonProperty("global")
    private Boolean global;

    @JsonProperty("extent")
    private String extent;

    @JsonProperty("axis")
    private String axis;

    @JsonProperty("projDef")
    private String projDef;

    @JsonProperty("worldExtent")
    private String worldExtent;

    /**
     * No args constructor for use in serialization
     */
    public ProjectionDto() {
    }

    public ProjectionDto(Long id, String name, Integer epsgCode, String units, String formats, Boolean global, String extent) {
        this.id = id;
        this.name = name;
        this.epsgCode = epsgCode;
        this.units = units;
        this.formats = formats;
        this.global = global;
        this.extent = extent;
    }

    public ProjectionDto(Integer epsgCode, String units, String formats) {
        this.epsgCode = epsgCode;
        this.units = units;
        this.formats = formats;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("epsgCode")
    public Integer getEpsgCode() {
        return epsgCode;
    }

    @JsonProperty("epsgCode")
    public void setEpsgCode(Integer epsgCode) {
        this.epsgCode = epsgCode;
    }

    @JsonProperty("units")
    public String getUnits() {
        return units;
    }

    @JsonProperty("units")
    public void setUnits(String units) {
        this.units = units;
    }

    @JsonProperty("global")
    public Boolean getGlobal() {
        return global;
    }

    @JsonProperty("global")
    public void setGlobal(Boolean global) {
        this.global = global;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("formats")
    public String getFormats() {
        return formats;
    }

    @JsonProperty("formats")
    public void setFormats(String formats) {
        this.formats = formats;
    }

    @JsonProperty("extent")
    public String getExtent() {
        return extent;
    }

    @JsonProperty("extent")
    public void setExtent(String extent) {
        this.extent = extent;
    }

    @JsonProperty("axis")
    public String getAxis() {
        return axis;
    }

    @JsonProperty("axis")
    public void setAxis(String axis) {
        this.axis = axis;
    }

    @JsonProperty("projDef")
    public String getProjDef() {
        return projDef;
    }

    @JsonProperty("projDef")
    public void setProjDef(String projDef) {
        this.projDef = projDef;
    }

    @JsonProperty("worldExtent")
    public String getWorldExtent() {
        return worldExtent;
    }

    @JsonProperty("worldExtent")
    public void setWorldExtent(String worldExtent) {
        this.worldExtent = worldExtent;
    }
}