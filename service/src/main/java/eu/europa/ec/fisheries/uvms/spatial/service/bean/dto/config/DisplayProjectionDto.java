/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.CoordinatesFormat;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScaleBarUnits;

/**
 * Created by padhyad on 11/30/2015.
 */
public class DisplayProjectionDto {

    @JsonProperty("epsgCode")
    private Integer epsgCode;

    @JsonProperty("formats")
    private CoordinatesFormat formats;

    @JsonProperty("units")
    private ScaleBarUnits units;

    public DisplayProjectionDto() {
    }

    public DisplayProjectionDto(Integer epsgCode, CoordinatesFormat formats, ScaleBarUnits units) {
        this.epsgCode = epsgCode;
        this.formats = formats;
        this.units = units;
    }

    @JsonProperty("epsgCode")
    public Integer getEpsgCode() {
        return epsgCode;
    }

    @JsonProperty("epsgCode")
    public void setEpsgCode(Integer epsgCode) {
        this.epsgCode = epsgCode;
    }

    @JsonProperty("formats")
    public CoordinatesFormat getFormats() {
        return formats;
    }

    @JsonProperty("formats")
    public void setFormats(CoordinatesFormat formats) {
        this.formats = formats;
    }

    @JsonProperty("units")
    public ScaleBarUnits getUnits() {
        return units;
    }

    @JsonProperty("units")
    public void setUnits(ScaleBarUnits units) {
        this.units = units;
    }
}