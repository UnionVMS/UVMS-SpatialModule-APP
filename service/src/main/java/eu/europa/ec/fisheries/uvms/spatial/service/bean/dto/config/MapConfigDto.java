package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "map",
        "vectorStyles"
})
public class MapConfigDto {

    @JsonProperty("map")
    private MapDto map;
    @JsonProperty("vectorStyles")
    private VectorStylesDto vectorStyles;

    /**
     * No args constructor for use in serialization
     */
    public MapConfigDto() {
    }

    public MapConfigDto(MapDto map, VectorStylesDto vectorStyles) {
        this.map = map;
        this.vectorStyles = vectorStyles;
    }

    @JsonProperty("map")
    public MapDto getMap() {
        return map;
    }

    @JsonProperty("map")
    public void setMap(MapDto map) {
        this.map = map;
    }

    @JsonProperty("vectorStyles")
    public VectorStylesDto getVectorStyles() {
        return vectorStyles;
    }

    @JsonProperty("vectorStyles")
    public void setVectorStyles(VectorStylesDto vectorStyles) {
        this.vectorStyles = vectorStyles;
    }
}
