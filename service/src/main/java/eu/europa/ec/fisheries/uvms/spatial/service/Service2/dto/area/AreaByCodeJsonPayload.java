/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.area;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "areaTypes"
})
public class AreaByCodeJsonPayload {

    @JsonProperty("areaTypes")
    @Valid
    private List<AreaType> areaTypes = new ArrayList<AreaType>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public AreaByCodeJsonPayload() {
    }

    /**
     * 
     * @param areaTypes
     */
    public AreaByCodeJsonPayload(List<AreaType> areaTypes) {
        this.areaTypes = areaTypes;
    }

    /**
     * 
     * @return
     *     The areaTypes
     */
    @JsonProperty("areaTypes")
    public List<AreaType> getAreaTypes() {
        return areaTypes;
    }

    /**
     * 
     * @param areaTypes
     *     The areaTypes
     */
    @JsonProperty("areaTypes")
    public void setAreaTypes(List<AreaType> areaTypes) {
        this.areaTypes = areaTypes;
    }

    public AreaByCodeJsonPayload withAreaTypes(List<AreaType> areaTypes) {
        this.areaTypes = areaTypes;
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

    public AreaByCodeJsonPayload withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(areaTypes).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AreaByCodeJsonPayload) == false) {
            return false;
        }
        AreaByCodeJsonPayload rhs = ((AreaByCodeJsonPayload) other);
        return new EqualsBuilder().append(areaTypes, rhs.areaTypes).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
