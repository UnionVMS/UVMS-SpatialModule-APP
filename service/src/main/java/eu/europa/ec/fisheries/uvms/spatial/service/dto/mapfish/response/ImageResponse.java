/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.dto.mapfish.response;

import java.util.HashMap;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageResponse {

    @JsonProperty("map")
    @Valid
    private Map map = new Map();
    @JsonProperty("legend")
    @Valid
    private Legend legend = new Legend();
    @JsonIgnore
    private java.util.Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ImageResponse() {
    }

    /**
     * 
     * @param legend
     * @param map
     */
    public ImageResponse(Map map, Legend legend) {
        this.map = map;
        this.legend = legend;
    }

    /**
     * 
     * @return
     *     The map
     */
    @JsonProperty("map")
    public Map getMap() {
        return map;
    }

    /**
     * 
     * @param map
     *     The map
     */
    @JsonProperty("map")
    public void setMap(Map map) {
        this.map = map;
    }

    public ImageResponse withMap(Map map) {
        this.map = map;
        return this;
    }

    /**
     * 
     * @return
     *     The legend
     */
    @JsonProperty("legend")
    public Legend getLegend() {
        return legend;
    }

    /**
     * 
     * @param legend
     *     The legend
     */
    @JsonProperty("legend")
    public void setLegend(Legend legend) {
        this.legend = legend;
    }

    public ImageResponse withLegend(Legend legend) {
        this.legend = legend;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public java.util.Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public ImageResponse withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(map).append(legend).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ImageResponse) == false) {
            return false;
        }
        ImageResponse rhs = ((ImageResponse) other);
        return new EqualsBuilder().append(map, rhs.map).append(legend, rhs.legend).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}