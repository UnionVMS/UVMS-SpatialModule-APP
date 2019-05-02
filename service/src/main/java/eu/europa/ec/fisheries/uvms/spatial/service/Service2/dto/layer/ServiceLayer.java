/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.layer;

import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.Views;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceLayer {

    @JsonProperty("id")
    @JsonView(Views.Public.class)
    private Long id;
    @JsonProperty("name")
    @JsonView(Views.Public.class)
    private String name;
    @JsonView(Views.Public.class)
    @JsonProperty("layerDesc")
    private String layerDesc;
    @JsonView(Views.Public.class)
    @JsonProperty("longCopyright")
    private String longCopyright;
    @JsonView(Views.Public.class)
    @JsonProperty("shortCopyright")
    private String shortCopyright;
    @JsonProperty("geoName")
    private String geoName;
    @JsonProperty("srsCode")
    private Integer srsCode;
    @JsonProperty("styleGeom")
    private String styleGeom;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public ServiceLayer() {
    }

    /**
     *
     * @param id
     * @param styleGeom
     * @param longCopyright
     * @param name
     * @param geoName
     * @param srsCode
     * @param layerDesc
     * @param shortCopyright
     */
    public ServiceLayer(Long id, String name, String layerDesc, String longCopyright, String shortCopyright, String geoName, Integer srsCode, String styleGeom) {
        this.id = id;
        this.name = name;
        this.layerDesc = layerDesc;
        this.longCopyright = longCopyright;
        this.shortCopyright = shortCopyright;
        this.geoName = geoName;
        this.srsCode = srsCode;
        this.styleGeom = styleGeom;
    }

    /**
     *
     * @return
     *     The id
     */
    @JsonProperty("id")
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(long id) {
        this.id = id;
    }

    public ServiceLayer withId(long id) {
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

    public ServiceLayer withName(String name) {
        this.name = name;
        return this;
    }

    /**
     *
     * @return
     *     The layerDesc
     */
    @JsonProperty("layerDesc")
    public String getLayerDesc() {
        return layerDesc;
    }

    /**
     *
     * @param layerDesc
     *     The layerDesc
     */
    @JsonProperty("layerDesc")
    public void setLayerDesc(String layerDesc) {
        this.layerDesc = layerDesc;
    }

    public ServiceLayer withLayerDesc(String layerDesc) {
        this.layerDesc = layerDesc;
        return this;
    }

    /**
     *
     * @return
     *     The longCopyright
     */
    @JsonProperty("longCopyright")
    public String getLongCopyright() {
        return longCopyright;
    }

    /**
     *
     * @param longCopyright
     *     The longCopyright
     */
    @JsonProperty("longCopyright")
    public void setLongCopyright(String longCopyright) {
        this.longCopyright = longCopyright;
    }

    public ServiceLayer withLongCopyright(String longCopyright) {
        this.longCopyright = longCopyright;
        return this;
    }

    /**
     *
     * @return
     *     The shortCopyright
     */
    @JsonProperty("shortCopyright")
    public String getShortCopyright() {
        return shortCopyright;
    }

    /**
     *
     * @param shortCopyright
     *     The shortCopyright
     */
    @JsonProperty("shortCopyright")
    public void setShortCopyright(String shortCopyright) {
        this.shortCopyright = shortCopyright;
    }

    public ServiceLayer withShortCopyright(String shortCopyright) {
        this.shortCopyright = shortCopyright;
        return this;
    }

    /**
     *
     * @return
     *     The geoName
     */
    @JsonProperty("geoName")
    public String getGeoName() {
        return geoName;
    }

    /**
     *
     * @param geoName
     *     The geoName
     */
    @JsonProperty("geoName")
    public void setGeoName(String geoName) {
        this.geoName = geoName;
    }

    public ServiceLayer withGeoName(String geoName) {
        this.geoName = geoName;
        return this;
    }

    /**
     *
     * @return
     *     The srsCode
     */
    @JsonProperty("srsCode")
    public Integer getSrsCode() {
        return srsCode;
    }

    /**
     *
     * @param srsCode
     *     The srsCode
     */
    @JsonProperty("srsCode")
    public void setSrsCode(Integer srsCode) {
        this.srsCode = srsCode;
    }

    public ServiceLayer withSrsCode(Integer srsCode) {
        this.srsCode = srsCode;
        return this;
    }

    /**
     *
     * @return
     *     The styleGeom
     */
    @JsonProperty("styleGeom")
    public String getStyleGeom() {
        return styleGeom;
    }

    /**
     *
     * @param styleGeom
     *     The styleGeom
     */
    @JsonProperty("styleGeom")
    public void setStyleGeom(String styleGeom) {
        this.styleGeom = styleGeom;
    }

    public ServiceLayer withStyleGeom(String styleGeom) {
        this.styleGeom = styleGeom;
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

    public ServiceLayer withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(layerDesc).append(longCopyright).append(shortCopyright).append(geoName).append(srsCode).append(styleGeom).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ServiceLayer) == false) {
            return false;
        }
        ServiceLayer rhs = ((ServiceLayer) other);
        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).append(layerDesc, rhs.layerDesc).append(longCopyright, rhs.longCopyright).append(shortCopyright, rhs.shortCopyright).append(geoName, rhs.geoName).append(srsCode, rhs.srsCode).append(styleGeom, rhs.styleGeom).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}