/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectionDto2 {

    private Long id;

    private String name;

    private Integer epsgCode;

    private String formats;

    private String units;

    private Boolean global;

    private String extent;

    private String axis;

    private String projDef;

    private String worldExtent;

    /**
     * No args constructor for use in serialization
     */
    public ProjectionDto2() {
    }

    public ProjectionDto2(Long id, String name, Integer epsgCode, String units, String formats, Boolean global, String extent) {
        this.id = id;
        this.name = name;
        this.epsgCode = epsgCode;
        this.units = units;
        this.formats = formats;
        this.global = global;
        this.extent = extent;
    }

    public ProjectionDto2(Integer epsgCode, String units, String formats) {
        this.epsgCode = epsgCode;
        this.units = units;
        this.formats = formats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEpsgCode() {
        return epsgCode;
    }

    public void setEpsgCode(Integer epsgCode) {
        this.epsgCode = epsgCode;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Boolean getGlobal() {
        return global;
    }

    public void setGlobal(Boolean global) {
        this.global = global;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormats() {
        return formats;
    }

    public void setFormats(String formats) {
        this.formats = formats;
    }

    public String getExtent() {
        return extent;
    }

    public void setExtent(String extent) {
        this.extent = extent;
    }

    public String getAxis() {
        return axis;
    }

    public void setAxis(String axis) {
        this.axis = axis;
    }

    public String getProjDef() {
        return projDef;
    }

    public void setProjDef(String projDef) {
        this.projDef = projDef;
    }

    public String getWorldExtent() {
        return worldExtent;
    }

    public void setWorldExtent(String worldExtent) {
        this.worldExtent = worldExtent;
    }
}