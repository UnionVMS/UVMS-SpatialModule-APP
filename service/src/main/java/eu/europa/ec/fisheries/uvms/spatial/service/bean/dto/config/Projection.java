
package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "epsgCode",
    "units",
    "global"
})
public class Projection {

    @JsonProperty("epsgCode")
    private Integer epsgCode;
    @JsonProperty("units")
    private String units;
    @JsonProperty("global")
    private Boolean global;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Projection() {
    }

    /**
     * 
     * @param epsgCode
     * @param global
     * @param units
     */
    public Projection(Integer epsgCode, String units, Boolean global) {
        this.epsgCode = epsgCode;
        this.units = units;
        this.global = global;
    }

    /**
     * 
     * @return
     *     The epsgCode
     */
    @JsonProperty("epsgCode")
    public Integer getEpsgCode() {
        return epsgCode;
    }

    /**
     * 
     * @param epsgCode
     *     The epsgCode
     */
    @JsonProperty("epsgCode")
    public void setEpsgCode(Integer epsgCode) {
        this.epsgCode = epsgCode;
    }

    public Projection withEpsgCode(Integer epsgCode) {
        this.epsgCode = epsgCode;
        return this;
    }

    /**
     * 
     * @return
     *     The units
     */
    @JsonProperty("units")
    public String getUnits() {
        return units;
    }

    /**
     * 
     * @param units
     *     The units
     */
    @JsonProperty("units")
    public void setUnits(String units) {
        this.units = units;
    }

    public Projection withUnits(String units) {
        this.units = units;
        return this;
    }

    /**
     * 
     * @return
     *     The global
     */
    @JsonProperty("global")
    public Boolean getGlobal() {
        return global;
    }

    /**
     * 
     * @param global
     *     The global
     */
    @JsonProperty("global")
    public void setGlobal(Boolean global) {
        this.global = global;
    }

    public Projection withGlobal(Boolean global) {
        this.global = global;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Projection withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
