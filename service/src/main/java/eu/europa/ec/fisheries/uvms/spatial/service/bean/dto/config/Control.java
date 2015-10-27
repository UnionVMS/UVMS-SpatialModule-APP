
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
    "type",
    "units",
    "epsgCode",
    "format"
})
public class Control {

    @JsonProperty("type")
    private String type;
    @JsonProperty("units")
    private String units;
    @JsonProperty("epsgCode")
    private Integer epsgCode;
    @JsonProperty("format")
    private String format;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Control() {
    }

    public Control(String type) {
        this.type = type;
    }

    /**
     * 
     * @param epsgCode
     * @param format
     * @param type
     * @param units
     */
    public Control(String type, String units, Integer epsgCode, String format) {
        this.type = type;
        this.units = units;
        this.epsgCode = epsgCode;
        this.format = format;
    }

    /**
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public Control withType(String type) {
        this.type = type;
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

    public Control withUnits(String units) {
        this.units = units;
        return this;
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

    public Control withEpsgCode(Integer epsgCode) {
        this.epsgCode = epsgCode;
        return this;
    }

    /**
     * 
     * @return
     *     The format
     */
    @JsonProperty("format")
    public String getFormat() {
        return format;
    }

    /**
     * 
     * @param format
     *     The format
     */
    @JsonProperty("format")
    public void setFormat(String format) {
        this.format = format;
    }

    public Control withFormat(String format) {
        this.format = format;
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

    public Control withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
