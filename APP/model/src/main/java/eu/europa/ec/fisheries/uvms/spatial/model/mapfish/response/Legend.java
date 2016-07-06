/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.model.mapfish.response;

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
public class Legend {

    @JsonProperty("base")
    private String base;
    @JsonProperty("positions")
    private String positions;
    @JsonProperty("segments")
    private String segments;
    @JsonProperty("alarms")
    private String alarms;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Legend() {
    }

    /**
     * 
     * @param positions
     * @param segments
     * @param base
     */
    public Legend(String base, String positions, String segments) {
        this.base = base;
        this.positions = positions;
        this.segments = segments;
    }

    /**
     * 
     * @return
     *     The base
     */
    @JsonProperty("base")
    public String getBase() {
        return base;
    }

    /**
     * 
     * @param base
     *     The base
     */
    @JsonProperty("base")
    public void setBase(String base) {
        this.base = base;
    }

    public Legend withBase(String base) {
        this.base = base;
        return this;
    }

    /**
     * 
     * @return
     *     The positions
     */
    @JsonProperty("positions")
    public String getPositions() {
        return positions;
    }

    /**
     * 
     * @param positions
     *     The positions
     */
    @JsonProperty("positions")
    public void setPositions(String positions) {
        this.positions = positions;
    }

    public Legend withPositions(String positions) {
        this.positions = positions;
        return this;
    }

    /**
     * 
     * @return
     *     The segments
     */
    @JsonProperty("segments")
    public String getSegments() {
        return segments;
    }

    /**
     *
     * @return
     *     The alarms
     */
    @JsonProperty("alarms")
    public String getAlarms() {
        return alarms;
    }

    /**
     * 
     * @param segments
     *     The segments
     */
    @JsonProperty("segments")
    public void setSegments(String segments) {
        this.segments = segments;
    }

    /**
     *
     * @param alarms
     *     The alarms
     */
    @JsonProperty("alarms")
    public void setAlarms(String alarms) {
        this.alarms = alarms;
    }


    public Legend withSegments(String segments) {
        this.segments = segments;
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

    public Legend withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(base).append(positions).append(alarms).append(segments).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Legend) == false) {
            return false;
        }
        Legend rhs = ((Legend) other);
        return new EqualsBuilder().append(base, rhs.base).append(positions, rhs.positions).append(alarms, rhs.alarms).append(segments, rhs.segments).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    public Legend withAlarms(String alarms) {
        this.alarms = alarms;
        return this;

    }
}