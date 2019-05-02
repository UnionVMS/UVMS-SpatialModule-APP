/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MapSettingsDto {

    @NotNull
    @JsonProperty("refreshStatus")
    private boolean refreshStatus;

    @NotNull
    @JsonProperty("scaleBarUnits")
    private String scaleBarUnits;

    @NotNull
    @JsonProperty("coordinatesFormat")
    private String coordinatesFormat;

    @NotNull
    @JsonProperty("mapProjectionId")
    private int mapProjectionId;

    @NotNull
    @JsonProperty("refreshRate")
    private int refreshRate;

    @NotNull
    @JsonProperty("displayProjectionId")
    private int displayProjectionId;

    public MapSettingsDto() {}

    public MapSettingsDto(boolean refreshStatus, String scaleBarUnits, String coordinatesFormat, int mapProjectionId, int refreshRate, int displayProjectionId) {
        this.refreshRate = refreshRate;
        this.scaleBarUnits =scaleBarUnits;
        this.coordinatesFormat = coordinatesFormat;
        this.mapProjectionId = mapProjectionId;
        this.refreshStatus = refreshStatus;
        this.displayProjectionId = displayProjectionId;
    }

    @JsonProperty("refreshStatus")
    public boolean getRefreshStatus() {
        return refreshStatus;
    }

    @JsonProperty("refreshStatus")
    public void setRefreshStatus(boolean refreshStatus) {
        this.refreshStatus = refreshStatus;
    }

    @JsonProperty("scaleBarUnits")
    public String getScaleBarUnits() {
        return scaleBarUnits;
    }

    @JsonProperty("scaleBarUnits")
    public void setScaleBarUnits(String scaleBarUnits) {
        this.scaleBarUnits = scaleBarUnits;
    }

    @JsonProperty("coordinatesFormat")
    public String getCoordinatesFormat() {
        return coordinatesFormat;
    }

    @JsonProperty("coordinatesFormat")
    public void setCoordinatesFormat(String coordinatesFormat) {
        this.coordinatesFormat = coordinatesFormat;
    }

    @JsonProperty("mapProjectionId")
    public int getMapProjectionId() {
        return mapProjectionId;
    }

    @JsonProperty("mapProjectionId")
    public void setMapProjectionId(int mapProjectionId) {
        this.mapProjectionId = mapProjectionId;
    }

    @JsonProperty("refreshRate")
    public int getRefreshRate() {
        return refreshRate;
    }

    @JsonProperty("refreshRate")
    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }

    @JsonProperty("displayProjectionId")
    public int getDisplayProjectionId() {
        return displayProjectionId;
    }

    @JsonProperty("displayProjectionId")
    public void setDisplayProjectionId(int displayProjectionId) {
        this.displayProjectionId = displayProjectionId;
    }

    @Override
    public String toString() {
        return "ClassPojo [refreshState = " + refreshStatus + ", scaleBarUnits = " + scaleBarUnits + ", coordinatesFormat = " + coordinatesFormat + ", mapProjection = " + mapProjectionId + ", refreshRate = " + refreshRate + ", displayProjection = " + displayProjectionId + "]";
    }
}