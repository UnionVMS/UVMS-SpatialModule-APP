/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.model.bookmark;

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
public class Bookmark {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("srs")
    private int srs;
    @JsonProperty("extent")
    private String extent;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Bookmark() {
    }

    /**
     * 
     * @param id
     * @param createdBy
     * @param extent
     * @param name
     * @param srs
     */
    public Bookmark(Long id, String name, Integer srs, String extent, String createdBy) {
        this.id = id;
        this.name = name;
        this.srs = srs;
        this.extent = extent;
        this.createdBy = createdBy;
    }

    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    public Bookmark withId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Bookmark withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * @return
     *     The srs
     */
    @JsonProperty("srs")
    public Integer getSrs() {
        return srs;
    }

    /**
     * 
     * @param srs
     *     The srs
     */
    @JsonProperty("srs")
    public void setSrs(Integer srs) {
        this.srs = srs;
    }

    public Bookmark withSrs(Integer srs) {
        this.srs = srs;
        return this;
    }

    /**
     * 
     * @return
     *     The extent
     */
    @JsonProperty("extent")
    public String getExtent() {
        return extent;
    }

    /**
     * 
     * @param extent
     *     The extent
     */
    @JsonProperty("extent")
    public void setExtent(String extent) {
        this.extent = extent;
    }

    public Bookmark withExtent(String extent) {
        this.extent = extent;
        return this;
    }

    /**
     * 
     * @return
     *     The createdBy
     */
    @JsonProperty("createdBy")
    @JsonIgnore
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * 
     * @param createdBy
     *     The createdBy
     */
    @JsonProperty("createdBy")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Bookmark withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public Bookmark withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(srs).append(extent).append(createdBy).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Bookmark) == false) {
            return false;
        }
        Bookmark rhs = ((Bookmark) other);
        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).append(srs, rhs.srs).append(extent, rhs.extent).append(createdBy, rhs.createdBy).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}