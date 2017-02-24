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
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cluster {

    @JsonProperty("text")
    private String text;
    @JsonProperty("bgcolor")
    private String bgcolor;
    @JsonProperty("bordercolor")
    private String bordercolor;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Cluster() {
    }

    /**
     * 
     * @param bordercolor
     * @param text
     * @param bgcolor
     */
    public Cluster(String text, String bgcolor, String bordercolor) {
        this.text = text;
        this.bgcolor = bgcolor;
        this.bordercolor = bordercolor;
    }

    /**
     * 
     * @return
     *     The text
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     * 
     * @param text
     *     The text
     */
    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    public Cluster withText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 
     * @return
     *     The bgcolor
     */
    @JsonProperty("bgcolor")
    public String getBgcolor() {
        return bgcolor;
    }

    /**
     * 
     * @param bgcolor
     *     The bgcolor
     */
    @JsonProperty("bgcolor")
    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public Cluster withBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
        return this;
    }

    /**
     * 
     * @return
     *     The bordercolor
     */
    @JsonProperty("bordercolor")
    public String getBordercolor() {
        return bordercolor;
    }

    /**
     * 
     * @param bordercolor
     *     The bordercolor
     */
    @JsonProperty("bordercolor")
    public void setBordercolor(String bordercolor) {
        this.bordercolor = bordercolor;
    }

    public Cluster withBordercolor(String bordercolor) {
        this.bordercolor = bordercolor;
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

    public Cluster withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(text).append(bgcolor).append(bordercolor).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Cluster) == false) {
            return false;
        }
        Cluster rhs = ((Cluster) other);
        return new EqualsBuilder().append(text, rhs.text).append(bgcolor, rhs.bgcolor).append(bordercolor, rhs.bordercolor).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}