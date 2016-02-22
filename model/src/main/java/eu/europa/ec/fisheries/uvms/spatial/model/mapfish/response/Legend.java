package eu.europa.ec.fisheries.uvms.spatial.model.mapfish.response;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Legend {

    @JsonProperty("base")
    private String base;
    @JsonProperty("positions")
    private String positions;
    @JsonProperty("segments")
    private String segments;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Legend() {
    }

    /**
     * 
     * @param positions
     * @param segments
     * @param base
     */
    public Legend(String base, String positions, String segments) {
        this.base = base;
        this.positions = positions;
        this.segments = segments;
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

    public Legend withBase(String base) {
        this.base = base;
        return this;
    }

    /**
     * 
     * @return
     *     The positions
     */
    @JsonProperty("positions")
    public String getPositions() {
        return positions;
    }

    /**
     * 
     * @param positions
     *     The positions
     */
    @JsonProperty("positions")
    public void setPositions(String positions) {
        this.positions = positions;
    }

    public Legend withPositions(String positions) {
        this.positions = positions;
        return this;
    }

    /**
     * 
     * @return
     *     The segments
     */
    @JsonProperty("segments")
    public String getSegments() {
        return segments;
    }

    /**
     * 
     * @param segments
     *     The segments
     */
    @JsonProperty("segments")
    public void setSegments(String segments) {
        this.segments = segments;
    }

    public Legend withSegments(String segments) {
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

    public Legend withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(base).append(positions).append(segments).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Legend) == false) {
            return false;
        }
        Legend rhs = ((Legend) other);
        return new EqualsBuilder().append(base, rhs.base).append(positions, rhs.positions).append(segments, rhs.segments).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
