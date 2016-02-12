
package eu.europa.ec.fisheries.uvms.spatial.model.mapfish.response;

import java.util.HashMap;
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
    "vmspos"
})
public class Map {

    @JsonProperty("vmspos")
    @Valid
    private Vmspos vmspos = new Vmspos();
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
     * @param vmspos
     */
    public Map(Vmspos vmspos) {
        this.vmspos = vmspos;
    }

    /**
     * 
     * @return
     *     The vmspos
     */
    @JsonProperty("vmspos")
    public Vmspos getVmspos() {
        return vmspos;
    }

    /**
     * 
     * @param vmspos
     *     The vmspos
     */
    @JsonProperty("vmspos")
    public void setVmspos(Vmspos vmspos) {
        this.vmspos = vmspos;
    }

    public Map withVmspos(Vmspos vmspos) {
        this.vmspos = vmspos;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(vmspos).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Map) == false) {
            return false;
        }
        Map rhs = ((Map) other);
        return new EqualsBuilder().append(vmspos, rhs.vmspos).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
