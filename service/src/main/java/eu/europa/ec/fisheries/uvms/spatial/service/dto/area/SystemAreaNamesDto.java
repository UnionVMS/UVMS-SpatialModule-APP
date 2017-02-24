/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.dto.area;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vividsolutions.jts.geom.Geometry;

import java.util.List;
import java.util.Set;

/**
 * Created by padhyad on 6/6/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SystemAreaNamesDto {

    @JsonProperty("code")
    private String code;

    @JsonProperty("areaNames")
    private Set<String> areaNames;

    @JsonProperty("extent")
    private String extent;

    @JsonIgnore
    private List<Geometry> geoms;

    public SystemAreaNamesDto(String code, Set<String> areaNames, List<Geometry> geoms) {
        this.code = code;
        this.areaNames = areaNames;
        this.geoms = geoms;
    }

    /**
     *
     * @return
     * The code
     */
    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     * The code
     */
    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    /**
     *
     * @return
     */
    @JsonProperty("areaNames")
    public Set<String> getAreaNames() {
        return areaNames;
    }

    /**
     *
     * @param areaNames
     */
    @JsonProperty("areaNames")
    public void setAreaNames(Set<String> areaNames) {
        this.areaNames = areaNames;
    }

    /**
     *
     * @return
     */
    @JsonProperty("extent")
    public String getExtent() {
        return extent;
    }

    /**
     *
     * @param extent
     */
    @JsonProperty("extent")
    public void setExtent(String extent) {
        this.extent = extent;
    }

    /**
     *
     * @return
     */
    public List<Geometry> getGeoms() {
        return geoms;
    }

    /**
     *
     * @param geoms
     */
    public void setGeoms(List<Geometry> geoms) {
        this.geoms = geoms;
    }
}