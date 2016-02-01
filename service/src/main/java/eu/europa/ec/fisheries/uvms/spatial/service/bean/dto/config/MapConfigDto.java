package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.VisibilitySettingsDto;

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

    @JsonProperty("visibilitySettings")
    private VisibilitySettingsDto visibilitySettings;

    /**
     * No args constructor for use in serialization
     */
    public MapConfigDto() {
    }

    public MapConfigDto(MapDto map, VectorStylesDto vectorStyles, VisibilitySettingsDto visibilitySettings) {
        this.map = map;
        this.vectorStyles = vectorStyles;
        this.visibilitySettings = visibilitySettings;
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

    @JsonProperty("visibilitySettings")
    public VisibilitySettingsDto getVisibilitySettings() {
        return visibilitySettings;
    }

    @JsonProperty("visibilitySettings")
    public void setVisibilitySettings(VisibilitySettingsDto visibilitySettings) {
        this.visibilitySettings = visibilitySettings;
    }
}
