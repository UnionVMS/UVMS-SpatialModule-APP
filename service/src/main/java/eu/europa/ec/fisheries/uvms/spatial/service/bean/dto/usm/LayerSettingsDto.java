package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LayerSettingsDto {

    @JsonProperty("baseLayers")
    private List<LayersDto> baseLayers;

    @JsonProperty("portLayers")
    private List<LayersDto> portLayers;

    @JsonProperty("additionalLayers")
    private List<LayersDto> additionalLayers;

    @JsonProperty("areaLayers")
    private List<LayerAreaDto> areaLayers;

    public LayerSettingsDto() {}

    public LayerSettingsDto(List<LayersDto> baseLayers, List<LayersDto> portLayers, List<LayersDto> additionalLayers, List<LayerAreaDto> areaLayers) {
        this.baseLayers = baseLayers;
        this.portLayers = portLayers;
        this.additionalLayers = additionalLayers;
        this.areaLayers = areaLayers;
    }

    @JsonProperty("areaLayers")
    public List<LayerAreaDto> getAreaLayers() {
        return areaLayers;
    }

    @JsonProperty("areaLayers")
    public void setAreaLayers(List<LayerAreaDto> areaLayers) {
        this.areaLayers = areaLayers;
    }

    @JsonProperty("additionalLayers")
    public List<LayersDto> getAdditionalLayers() {
        return additionalLayers;
    }

    @JsonProperty("additionalLayers")
    public void setAdditionalLayers(List<LayersDto> additionalLayers) {
        this.additionalLayers = additionalLayers;
    }

    @JsonProperty("baseLayers")
    public List<LayersDto> getBaseLayers() {
        return baseLayers;
    }

    @JsonProperty("baseLayers")
    public void setBaseLayers(List<LayersDto> baseLayers) {
        this.baseLayers = baseLayers;
    }

    @JsonProperty("portLayers")
    public List<LayersDto> getPortLayers() {
        return portLayers;
    }

    @JsonProperty("portLayers")
    public void setPortLayers(List<LayersDto> portLayers) {
        this.portLayers = portLayers;
    }

    public void addBaseLayer(LayersDto layersDto) {
        if (baseLayers == null) {
            baseLayers = new ArrayList<>();
        }
        baseLayers.add(layersDto);
    }

    public  void addPortLayer(LayersDto layersDto) {
        if (portLayers == null) {
            portLayers = new ArrayList<>();
        }
        portLayers.add(layersDto);
    }

    public void addAdditionalLayer(LayersDto layersDto) {
        if (additionalLayers == null) {
            additionalLayers = new ArrayList<>();
        }
        additionalLayers.add(layersDto);
    }

    public void addAreaLayer(LayerAreaDto layerAreaDto) {
        if (areaLayers == null) {
            areaLayers = new ArrayList<>();
        }
        areaLayers.add(layerAreaDto);
    }
}

