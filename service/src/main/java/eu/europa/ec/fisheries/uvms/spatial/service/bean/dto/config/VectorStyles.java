package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "flag_state",
        "speed"
})
public class VectorStyles {

    @JsonProperty("flag_state")
    private FlagState flagState;
    @JsonProperty("speed")
    private Speed speed;

    /**
     * No args constructor for use in serialization
     */
    public VectorStyles() {
    }

    public VectorStyles(FlagState flagState, Speed speed) {
        this.flagState = flagState;
        this.speed = speed;
    }

    @JsonProperty("flag_state")
    public FlagState getFlagState() {
        return flagState;
    }

    @JsonProperty("flag_state")
    public void setFlagState(FlagState flagState) {
        this.flagState = flagState;
    }

    public VectorStyles withFlagState(FlagState flagState) {
        this.flagState = flagState;
        return this;
    }

    @JsonProperty("speed")
    public Speed getSpeed() {
        return speed;
    }

    @JsonProperty("speed")
    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public VectorStyles withSpeed(Speed speed) {
        this.speed = speed;
        return this;
    }

}
