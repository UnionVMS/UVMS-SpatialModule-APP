
package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    "projection",
    "controls",
    "tb-controls",
    "layers"
})
public class Map {

    @JsonProperty("projection")
    private Projection projection;
    @JsonProperty("controls")
    private List<Control> controls = new ArrayList<Control>();
    @JsonProperty("tb-controls")
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

    /**
     * 
     * @param controls
     * @param layers
     * @param projection
     * @param tbControls
     */
    public Map(Projection projection, List<Control> controls, List<TbControl> tbControls, List<Layer> layers) {
        this.projection = projection;
        this.controls = controls;
        this.tbControls = tbControls;
        this.layers = layers;
    }

    /**
     * 
     * @return
     *     The projection
     */
    @JsonProperty("projection")
    public Projection getProjection() {
        return projection;
    }

    /**
     * 
     * @param projection
     *     The projection
     */
    @JsonProperty("projection")
    public void setProjection(Projection projection) {
        this.projection = projection;
    }

    public Map withProjection(Projection projection) {
        this.projection = projection;
        return this;
    }

    /**
     * 
     * @return
     *     The controls
     */
    @JsonProperty("controls")
    public List<Control> getControls() {
        return controls;
    }

    /**
     * 
     * @param controls
     *     The controls
     */
    @JsonProperty("controls")
    public void setControls(List<Control> controls) {
        this.controls = controls;
    }

    public Map withControls(List<Control> controls) {
        this.controls = controls;
        return this;
    }

    /**
     * 
     * @return
     *     The tbControls
     */
    @JsonProperty("tb-controls")
    public List<TbControl> getTbControls() {
        return tbControls;
    }

    /**
     * 
     * @param tbControls
     *     The tb-controls
     */
    @JsonProperty("tb-controls")
    public void setTbControls(List<TbControl> tbControls) {
        this.tbControls = tbControls;
    }

    public Map withTbControls(List<TbControl> tbControls) {
        this.tbControls = tbControls;
        return this;
    }

    /**
     * 
     * @return
     *     The layers
     */
    @JsonProperty("layers")
    public List<Layer> getLayers() {
        return layers;
    }

    /**
     * 
     * @param layers
     *     The layers
     */
    @JsonProperty("layers")
    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    public Map withLayers(List<Layer> layers) {
        this.layers = layers;
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

    public Map withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
