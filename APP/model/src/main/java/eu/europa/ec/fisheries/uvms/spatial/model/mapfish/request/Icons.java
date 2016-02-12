
package eu.europa.ec.fisheries.uvms.spatial.model.mapfish.request;

import java.util.HashMap;
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
    "positions",
    "segments"
})
public class Icons {

    @JsonProperty("positions")
    @Valid
    private Positions positions;
    @JsonProperty("segments")
    @Valid
    private Segments segments;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Icons() {
    }

    /**
     * 
     * @param positions
     * @param segments
     */
    public Icons(Positions positions, Segments segments) {
        this.positions = positions;
        this.segments = segments;
    }

    /**
     * 
     * @return
     *     The positions
     */
    @JsonProperty("positions")
    public Positions getPositions() {
        return positions;
    }

    /**
     * 
     * @param positions
     *     The positions
     */
    @JsonProperty("positions")
    public void setPositions(Positions positions) {
        this.positions = positions;
    }

    public Icons withPositions(Positions positions) {
        this.positions = positions;
        return this;
    }

    /**
     * 
     * @return
     *     The segments
     */
    @JsonProperty("segments")
    public Segments getSegments() {
        return segments;
    }

    /**
     * 
     * @param segments
     *     The segments
     */
    @JsonProperty("segments")
    public void setSegments(Segments segments) {
        this.segments = segments;
    }

    public Icons withSegments(Segments segments) {
        this.segments = segments;
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

    public Icons withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(positions).append(segments).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Icons) == false) {
            return false;
        }
        Icons rhs = ((Icons) other);
        return new EqualsBuilder().append(positions, rhs.positions).append(segments, rhs.segments).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
