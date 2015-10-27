
package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import java.util.HashMap;
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
    "map",
    "vectorStyles"
})
public class MapConfig {

    @JsonProperty("map")
    private Map map;
    @JsonProperty("vectorStyles")
    private VectorStyles vectorStyles;
    @JsonIgnore
    private java.util.Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public MapConfig() {
    }

    /**
     * 
     * @param vectorStyles
     * @param map
     */
    public MapConfig(Map map, VectorStyles vectorStyles) {
        this.map = map;
        this.vectorStyles = vectorStyles;
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

    public MapConfig withMap(Map map) {
        this.map = map;
        return this;
    }

    /**
     * 
     * @return
     *     The vectorStyles
     */
    @JsonProperty("vectorStyles")
    public VectorStyles getVectorStyles() {
        return vectorStyles;
    }

    /**
     * 
     * @param vectorStyles
     *     The vectorStyles
     */
    @JsonProperty("vectorStyles")
    public void setVectorStyles(VectorStyles vectorStyles) {
        this.vectorStyles = vectorStyles;
    }

    public MapConfig withVectorStyles(VectorStyles vectorStyles) {
        this.vectorStyles = vectorStyles;
        return this;
    }

    @JsonAnyGetter
    public java.util.Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public MapConfig withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
