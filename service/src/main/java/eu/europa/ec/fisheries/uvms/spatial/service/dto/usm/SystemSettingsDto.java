/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemSettingsDto {

    @JsonProperty("geoserverUrl")
    private String geoserverUrl;

    @JsonProperty("bingApiKey")
    private String bingApiKey;

    @JsonProperty("spatialServerUrl")
    private String spatialServerUrl;

    public SystemSettingsDto() {}

    public SystemSettingsDto(String geoserverUrl, String bingApiKey, String spatialServerUrl) {
        this.geoserverUrl = geoserverUrl;
        this.bingApiKey = bingApiKey;
        this.spatialServerUrl = spatialServerUrl;
    }

    @JsonProperty("geoserverUrl")
    public String getGeoserverUrl() {
        return geoserverUrl;
    }

    @JsonProperty("geoserverUrl")
    public void setGeoserverUrl(String geoserverUrl) {
        this.geoserverUrl = geoserverUrl;
    }

    @JsonProperty("bingApiKey")
    public String getBingApiKey() {
        return bingApiKey;
    }

    @JsonProperty("bingApiKey")
    public void setBingApiKey(String bingApiKey) {
        this.bingApiKey = bingApiKey;
    }

    @JsonProperty("spatialServerUrl")
    public String getSpatialServerUrl() {
        return spatialServerUrl;
    }

    @JsonProperty("spatialServerUrl")
    public void setSpatialServerUrl(String spatialServerUrl) {
        this.spatialServerUrl = spatialServerUrl;
    }

    @Override
    public String toString() {
        return "ClassPojo[" +
                "geoserverUrl='" + geoserverUrl + '\'' +
                ", bingApiKey='" + bingApiKey + '\'' +
                ", spatialServerUrl='" + spatialServerUrl + '\'' +
                "]";
    }
}