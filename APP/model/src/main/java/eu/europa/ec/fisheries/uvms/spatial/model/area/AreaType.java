/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.model.area;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "areaType",
    "areaCode"
})
public class AreaType {

    @JsonProperty("areaType")
    private String areaType;
    @JsonProperty("areaCode")
    private String areaCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public AreaType() {
    }

    /**
     * 
     * @param areaCode
     * @param areaType
     */
    public AreaType(String areaType, String areaCode) {
        this.areaType = areaType;
        this.areaCode = areaCode;
    }

    /**
     * 
     * @return
     *     The areaType
     */
    @JsonProperty("areaType")
    public String getAreaType() {
        return areaType;
    }

    /**
     * 
     * @param areaType
     *     The areaType
     */
    @JsonProperty("areaType")
    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public AreaType withAreaType(String areaType) {
        this.areaType = areaType;
        return this;
    }

    /**
     * 
     * @return
     *     The areaCode
     */
    @JsonProperty("areaCode")
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * 
     * @param areaCode
     *     The areaCode
     */
    @JsonProperty("areaCode")
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public AreaType withAreaCode(String areaCode) {
        this.areaCode = areaCode;
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

    public AreaType withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(areaType).append(areaCode).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AreaType) == false) {
            return false;
        }
        AreaType rhs = ((AreaType) other);
        return new EqualsBuilder().append(areaType, rhs.areaType).append(areaCode, rhs.areaCode).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
