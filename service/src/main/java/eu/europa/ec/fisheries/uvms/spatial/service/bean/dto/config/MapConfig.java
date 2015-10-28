package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "map",
        "vectorStyles"
})
public class MapConfig {

    @JsonProperty("map")
    private Map map;
    @JsonProperty("vectorStyles")
    private VectorStyles vectorStyles;

    /**
     * No args constructor for use in serialization
     */
    public MapConfig() {
    }

    public MapConfig(Map map, VectorStyles vectorStyles) {
        this.map = map;
        this.vectorStyles = vectorStyles;
    }

    @JsonProperty("map")
    public Map getMap() {
        return map;
    }

    @JsonProperty("map")
    public void setMap(Map map) {
        this.map = map;
    }

    public MapConfig withMap(Map map) {
        this.map = map;
        return this;
    }

    @JsonProperty("vectorStyles")
    public VectorStyles getVectorStyles() {
        return vectorStyles;
    }

    @JsonProperty("vectorStyles")
    public void setVectorStyles(VectorStyles vectorStyles) {
        this.vectorStyles = vectorStyles;
    }

    public MapConfig withVectorStyles(VectorStyles vectorStyles) {
        this.vectorStyles = vectorStyles;
        return this;
    }

}
