package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by padhyad on 1/14/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
        "portLayers",
        "additionalLayers",
        "systemLayers",
        "userLayer",
        "baseLayers"
})

public class ServiceLayersDto {

    @JsonProperty("port")
    private List<LayerDto> portLayers;

    @JsonProperty("additional")
    private List<LayerDto> additionalLayers;

    @JsonProperty("baseLayers")
    private List<LayerDto> baseLayers;

    @JsonProperty("systemAreas")
    private List<LayerDto> systemLayers;

/*    @JsonProperty("userAreas")
    private LayerDto userLayer;*/

    public ServiceLayersDto() {}

    public ServiceLayersDto(List<LayerDto> portLayers,
                            List<LayerDto> additionalLayers,
                            List<LayerDto> baseLayers,
                            List<LayerDto> systemLayers/*,
                            LayerDto userLayer*/) {
        this.portLayers = portLayers;
        this.additionalLayers = additionalLayers;
        this.baseLayers = baseLayers;
        this.systemLayers = systemLayers;
       // this.userLayer = userLayer;

    }

    @JsonProperty("port")
    public List<LayerDto> getPortLayers() {
        return portLayers;
    }

    @JsonProperty("port")
    public void setPortLayers(List<LayerDto> portLayers) {
        this.portLayers = portLayers;
    }

    @JsonProperty("additional")
    public List<LayerDto> getAdditionalLayers() {
        return additionalLayers;
    }

    @JsonProperty("additional")
    public void setAdditionalLayers(List<LayerDto> additionalLayers) {
        this.additionalLayers = additionalLayers;
    }

    @JsonProperty("baseLayers")
    public List<LayerDto> getBaseLayers() {
        return baseLayers;
    }

    @JsonProperty("baseLayers")
    public void setBaseLayers(List<LayerDto> baseLayers) {
        this.baseLayers = baseLayers;
    }

    @JsonProperty("systemAreas")
    public List<LayerDto> getSystemLayers() {
        return systemLayers;
    }

    @JsonProperty("systemAreas")
    public void setSystemLayers(List<LayerDto> systemLayers) {
        this.systemLayers = systemLayers;
    }

/*    @JsonProperty("userAreas")
    public LayerDto getUserLayer() {
        return userLayer;
    }

    @JsonProperty("userAreas")
    public void setUserLayer(LayerDto userLayer) {
        this.userLayer = userLayer;
    }*/
}
