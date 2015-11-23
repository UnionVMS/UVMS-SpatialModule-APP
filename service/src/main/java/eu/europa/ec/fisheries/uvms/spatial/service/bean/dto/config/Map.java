package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "projection",
    "controls",
    "tbControls",
    "layers"
})
public class Map {

    @JsonProperty("projection")
    private Projection projection;
    @JsonProperty("controls")
    private List<Control> controls = new ArrayList<Control>();
    @JsonProperty("tbControls")
    private List<TbControl> tbControls = new ArrayList<TbControl>();
    @JsonProperty("layers")
    private List<Layer> layers = new ArrayList<Layer>();
    @JsonIgnore
    private java.util.Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Map() {
    }

    public Map(Projection projection, List<Control> controls, List<TbControl> tbControls, List<Layer> layers) {
        this.projection = projection;
        this.controls = controls;
        this.tbControls = tbControls;
        this.layers = layers;
    }

    @JsonProperty("projection")
    public Projection getProjection() {
        return projection;
    }

    @JsonProperty("projection")
    public void setProjection(Projection projection) {
        this.projection = projection;
    }

    public Map withProjection(Projection projection) {
        this.projection = projection;
        return this;
    }

    @JsonProperty("controls")
    public List<Control> getControls() {
        return controls;
    }

    @JsonProperty("controls")
    public void setControls(List<Control> controls) {
        this.controls = controls;
    }

    public Map withControls(List<Control> controls) {
        this.controls = controls;
        return this;
    }

    @JsonProperty("tbControls")
    public List<TbControl> getTbControls() {
        return tbControls;
    }

    @JsonProperty("tbControls")
    public void setTbControls(List<TbControl> tbControls) {
        this.tbControls = tbControls;
    }

    public Map withTbControls(List<TbControl> tbControls) {
        this.tbControls = tbControls;
        return this;
    }

    @JsonProperty("layers")
    public List<Layer> getLayers() {
        return layers;
    }

    @JsonProperty("layers")
    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    public Map withLayers(List<Layer> layers) {
        this.layers = layers;
        return this;
    }

}
