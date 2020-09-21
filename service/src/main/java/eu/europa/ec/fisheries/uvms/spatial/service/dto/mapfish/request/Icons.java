/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.dto.mapfish.request;

import java.util.HashMap;
import java.util.Map;
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
public class Icons {

    @JsonProperty("positions")
    @Valid
    private Positions positions;
    @JsonProperty("activities")
    @Valid
    private Positions activities;
    @JsonProperty("alarms")
    @Valid
    private Alarms alarms;
    @JsonProperty("segments")
    @Valid
    private Segments segments;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Icons() {
    }

    /**
     * 
     * @param positions
     * @param segments
     */
    public Icons(Positions positions, Segments segments) {
        this.positions = positions;
        this.segments = segments;
    }

    /**
     * 
     * @return
     *     The positions
     */
    @JsonProperty("positions")
    public Positions getPositions() {
        return positions;
    }

    /**
     *
     * @return
     *     The alarms
     */
    @JsonProperty("alarms")
    public Alarms getAlarms() {
        return alarms;
    }

    /**
     * 
     * @param positions
     *     The positions
     */
    @JsonProperty("positions")
    public void setPositions(Positions positions) {
        this.positions = positions;
    }

    /**
     *
     * @return
     *     The activity
     */
    @JsonProperty("activities")
    public Positions getActivities() {
       return activities;
    }

    /**
     *
     * @param activities
     *     The activities
     */
    @JsonProperty("activities")
    public void setActivities(Positions activities) {
        this.activities = activities;
    }

    /**
     *
     * @param alarms
     *     The alarms
     */
    @JsonProperty("alarms")
    public void setAlarms(Alarms alarms) {
        this.alarms = alarms;
    }


    public Icons withPositions(Positions positions) {
        this.positions = positions;
        return this;
    }

    public Icons withAlarms(Alarms alarms) {
        this.alarms = alarms;
        return this;
    }

    /**
     * 
     * @return
     *     The segments
     */
    @JsonProperty("segments")
    public Segments getSegments() {
        return segments;
    }

    /**
     * 
     * @param segments
     *     The segments
     */
    @JsonProperty("segments")
    public void setSegments(Segments segments) {
        this.segments = segments;
    }

    public Icons withSegments(Segments segments) {
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

    public Icons withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(positions).append(alarms).append(segments).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Icons) == false) {
            return false;
        }
        Icons rhs = ((Icons) other);
        return new EqualsBuilder().append(alarms, rhs.alarms).append(positions, rhs.positions).append(activities, rhs.activities).append(segments, rhs.segments).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}