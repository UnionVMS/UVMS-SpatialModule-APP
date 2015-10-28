package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "geom"
})
public class Styles {

    @JsonProperty("geom")
    private String geom;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Styles() {
    }

    public Styles(String geom) {
        this.geom = geom;
    }

    @JsonProperty("geom")
    public String getGeom() {
        return geom;
    }

    @JsonProperty("geom")
    public void setGeom(String geom) {
        this.geom = geom;
    }

    public Styles withGeom(String geom) {
        this.geom = geom;
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

    public Styles withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
