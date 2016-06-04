package eu.europa.ec.fisheries.uvms.spatial.model.upload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class UploadMapping {

    @JsonProperty("mapping")
    @Valid
    private List<UploadMappingProperty> mapping = new ArrayList<UploadMappingProperty>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public UploadMapping() {
    }

    /**
     * 
     * @param mapping
     */
    public UploadMapping(List<UploadMappingProperty> mapping) {
        this.mapping = mapping;
    }

    /**
     * 
     * @return
     *     The mapping
     */
    @JsonProperty("mapping")
    public List<UploadMappingProperty> getMapping() {
        return mapping;
    }

    /**
     * 
     * @param mapping
     *     The mapping
     */
    @JsonProperty("mapping")
    public void setMapping(List<UploadMappingProperty> mapping) {
        this.mapping = mapping;
    }

    public UploadMapping withMapping(List<UploadMappingProperty> mapping) {
        this.mapping = mapping;
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

    public UploadMapping withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(mapping).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof UploadMapping) == false) {
            return false;
        }
        UploadMapping rhs = ((UploadMapping) other);
        return new EqualsBuilder().append(mapping, rhs.mapping).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
