
package eu.europa.ec.fisheries.uvms.spatial.model.mapfish;

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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
public class Color {

    @JsonProperty("vessel")
    @Valid
    private List<String> vessel = new ArrayList<String>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Color() {
    }

    /**
     * 
     * @param vessel
     */
    public Color(List<String> vessel) {
        this.vessel = vessel;
    }

    /**
     * 
     * @return
     *     The vessel
     */
    @JsonProperty("vessel")
    public List<String> getVessel() {
        return vessel;
    }

    /**
     * 
     * @param vessel
     *     The vessel
     */
    @JsonProperty("vessel")
    public void setVessel(List<String> vessel) {
        this.vessel = vessel;
    }

    public Color withVessel(List<String> vessel) {
        this.vessel = vessel;
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

    public Color withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(vessel).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Color) == false) {
            return false;
        }
        Color rhs = ((Color) other);
        return new EqualsBuilder().append(vessel, rhs.vessel).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
