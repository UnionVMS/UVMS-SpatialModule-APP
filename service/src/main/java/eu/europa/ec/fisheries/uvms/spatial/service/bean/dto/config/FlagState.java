package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlagState {

    @JsonIgnore
    private Map<String, Object> flagStates = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public FlagState() {
    }

    @JsonAnyGetter
    public Map<String, Object> getFlagStates() {
        return this.flagStates;
    }

    @JsonAnySetter
    public void setFlagState(String name, Object value) {
        this.flagStates.put(name, value);
    }

    public FlagState withFlagState(String name, Object value) {
        this.flagStates.put(name, value);
        return this;
    }

}
