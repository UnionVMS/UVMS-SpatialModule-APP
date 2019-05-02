/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.area;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericSystemAreaDto {

    @JsonProperty("gid")
    private Integer gid;
    @JsonProperty("code")
    private String code;
    @JsonProperty("areaType")
    private String areaType;
    @JsonProperty("extent")
    private String extent;
    @JsonProperty("name")
    private String name;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * No args constructor for use in serialization
     *
     */
    public GenericSystemAreaDto() {
    }

    /**
     *
     * @param code
     */
    public GenericSystemAreaDto(String code) {
        this.code = code;
    }

    /**
     *
     * @param extent
     * @param areaType
     * @param name
     * @param gid
     * @param code
     */
    public GenericSystemAreaDto(Integer gid, String code, String areaType, String extent, String name) {
        this.gid = gid;
        this.code = code;
        this.areaType = areaType;
        this.extent = extent;
        this.name = name;
    }

    /**
     *
     * @return
     * The gid
     */
    @JsonProperty("gid")
    public long getGid() {
        return gid;
    }

    /**
     *
     * @param gid
     * The gid
     */
    @JsonProperty("gid")
    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public GenericSystemAreaDto withGid(Integer gid) {
        this.gid = gid;
        return this;
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

    public GenericSystemAreaDto withCode(String code) {
        this.code = code;
        return this;
    }

    /**
     *
     * @return
     * The areaType
     */
    @JsonProperty("areaType")
    public String getAreaType() {
        return areaType;
    }

    /**
     *
     * @param areaType
     * The areaType
     */
    @JsonProperty("areaType")
    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public GenericSystemAreaDto withAreaType(String areaType) {
        this.areaType = areaType;
        return this;
    }

    /**
     *
     * @return
     * The extent
     */
    @JsonProperty("extent")
    public String getExtent() {
        return extent;
    }

    /**
     *
     * @param extent
     * The extent
     */
    @JsonProperty("extent")
    public void setExtent(String extent) {
        this.extent = extent;
    }

    public GenericSystemAreaDto withExtent(String extent) {
        this.extent = extent;
        return this;
    }

    /**
     *
     * @return
     * The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public GenericSystemAreaDto withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public GenericSystemAreaDto withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(gid).append(code).append(areaType).append(extent).append(name).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GenericSystemAreaDto) == false) {
            return false;
        }
        GenericSystemAreaDto rhs = ((GenericSystemAreaDto) other);
        return new EqualsBuilder().append(gid, rhs.gid).append(code, rhs.code).append(areaType, rhs.areaType).append(extent, rhs.extent).append(name, rhs.name).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}