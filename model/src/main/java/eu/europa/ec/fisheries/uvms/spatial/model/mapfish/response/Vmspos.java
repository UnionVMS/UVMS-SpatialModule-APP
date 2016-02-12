
package eu.europa.ec.fisheries.uvms.spatial.model.mapfish.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "base",
    "colors"
})
public class Vmspos {

    @JsonProperty("base")
    private String base;
    @JsonProperty("colors")
    @Valid
    private List<String> colors = new ArrayList<String>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Vmspos() {
    }

    /**
     * 
     * @param colors
     * @param base
     */
    public Vmspos(String base, List<String> colors) {
        this.base = base;
        this.colors = colors;
    }

    /**
     * 
     * @return
     *     The base
     */
    @JsonProperty("base")
    public String getBase() {
        return base;
    }

    /**
     * 
     * @param base
     *     The base
     */
    @JsonProperty("base")
    public void setBase(String base) {
        this.base = base;
    }

    public Vmspos withBase(String base) {
        this.base = base;
        return this;
    }

    /**
     * 
     * @return
     *     The colors
     */
    @JsonProperty("colors")
    public List<String> getColors() {
        return colors;
    }

    /**
     * 
     * @param colors
     *     The colors
     */
    @JsonProperty("colors")
    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public Vmspos withColors(List<String> colors) {
        this.colors = colors;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Vmspos withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(base).append(colors).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Vmspos) == false) {
            return false;
        }
        Vmspos rhs = ((Vmspos) other);
        return new EqualsBuilder().append(base, rhs.base).append(colors, rhs.colors).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
