
package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "projection",
    "control",
    "tbControl",
    "layers"
})
public class MapDto {

    @JsonProperty("projection")
    private ProjectionDto projectionDto;
    @JsonProperty("control")
    private List<ControlDto> controlDtos = new ArrayList<ControlDto>();
    @JsonProperty("tbControl")
    private List<TbControlDto> tbControlDtos = new ArrayList<TbControlDto>();
    @JsonProperty("layers")
    private List<LayerDto> layers = new ArrayList<LayerDto>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public MapDto() {}

    public MapDto(ProjectionDto projectionDto, List<ControlDto> controlDtos, List<TbControlDto> tbControlDtos, List<LayerDto> layers) {
        this.projectionDto = projectionDto;
        this.controlDtos = controlDtos;
        this.tbControlDtos = tbControlDtos;
        this.layers = layers;
    }

    @JsonProperty("projection")
    public ProjectionDto getProjectionDto() {
        return projectionDto;
    }

    @JsonProperty("projection")
    public void setProjectionDto(ProjectionDto projectionDto) {
        this.projectionDto = projectionDto;
    }

    @JsonProperty("control")
    public List<ControlDto> getControlDtos() {
        return controlDtos;
    }

    @JsonProperty("control")
    public void setControlDtos(List<ControlDto> controlDtos) {
        this.controlDtos = controlDtos;
    }

    @JsonProperty("tbControl")
    public List<TbControlDto> getTbControlDtos() {
        return tbControlDtos;
    }

    @JsonProperty("tbControl")
    public void setTbControlDtos(List<TbControlDto> tbControlDtos) {
        this.tbControlDtos = tbControlDtos;
    }

    @JsonProperty("layers")
    public List<LayerDto> getLayers() {
        return layers;
    }

    @JsonProperty("layers")
    public void setLayers(List<LayerDto> layers) {
        this.layers = layers;
    }
}
