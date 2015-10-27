
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
    "geom"
})
public class Styles {

    @JsonProperty("geom")
    private String geom;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Styles() {
    }

    /**
     * 
     * @param geom
     */
    public Styles(String geom) {
        this.geom = geom;
    }

    /**
     * 
     * @return
     *     The geom
     */
    @JsonProperty("geom")
    public String getGeom() {
        return geom;
    }

    /**
     * 
     * @param geom
     *     The geom
     */
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
