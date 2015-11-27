package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LayerSettingsDto {

    @JsonProperty("overlayLayers")
    private List<LayersDto> overlayLayers;

    @JsonProperty("baseLayers")
    private List<LayersDto> baseLayers;

    public LayerSettingsDto() {}

    public LayerSettingsDto(List<LayersDto> overlayLayers, List<LayersDto> baseLayers) {
        this.overlayLayers = overlayLayers;
        this.baseLayers = baseLayers;
    }

    @JsonProperty("overlayLayers")
    public List<LayersDto> getOverlayLayers() {
        return overlayLayers;
    }

    @JsonProperty("overlayLayers")
    public void setOverlayLayers(List<LayersDto> overlayLayers) {
        this.overlayLayers = overlayLayers;
    }

    @JsonProperty("baseLayers")
    public List<LayersDto> getBaseLayers() {
        return baseLayers;
    }

    @JsonProperty("baseLayers")
    public void setBaseLayers(List<LayersDto> baseLayers) {
        this.baseLayers = baseLayers;
    }

    @Override
    public String toString() {
        return "ClassPojo [overlayLayers = " + overlayLayers + ", baseLayers = " + baseLayers + "]";
    }
}

