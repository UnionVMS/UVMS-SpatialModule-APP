
package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import java.util.HashMap;
import java.util.Map;
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
    "flag_state",
    "speed"
})
public class VectorStyles {

    @JsonProperty("flag_state")
    private FlagState flagState;
    @JsonProperty("speed")
    private Speed speed;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public VectorStyles() {
    }

    /**
     * 
     * @param speed
     * @param flagState
     */
    public VectorStyles(FlagState flagState, Speed speed) {
        this.flagState = flagState;
        this.speed = speed;
    }

    /**
     * 
     * @return
     *     The flagState
     */
    @JsonProperty("flag_state")
    public FlagState getFlagState() {
        return flagState;
    }

    /**
     * 
     * @param flagState
     *     The flag_state
     */
    @JsonProperty("flag_state")
    public void setFlagState(FlagState flagState) {
        this.flagState = flagState;
    }

    public VectorStyles withFlagState(FlagState flagState) {
        this.flagState = flagState;
        return this;
    }

    /**
     * 
     * @return
     *     The speed
     */
    @JsonProperty("speed")
    public Speed getSpeed() {
        return speed;
    }

    /**
     * 
     * @param speed
     *     The speed
     */
    @JsonProperty("speed")
    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public VectorStyles withSpeed(Speed speed) {
        this.speed = speed;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public VectorStyles withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
