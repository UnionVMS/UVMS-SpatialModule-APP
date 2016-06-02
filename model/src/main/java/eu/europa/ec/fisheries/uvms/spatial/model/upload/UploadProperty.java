
package eu.europa.ec.fisheries.uvms.spatial.model.upload;

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
public class UploadProperty {

    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private Object value;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public UploadProperty() {
    }

    /**
     * 
     * @param name
     * @param type
     * @param value
     */
    public UploadProperty(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;

    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public UploadProperty withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public UploadProperty withType(String type) {
        this.type = type;
        return this;
    }

    /**
     *
     * @return
     *     The value
     */
    @JsonProperty("value")
    public Object getValue() {
        return value;
    }

    /**
     *
     * @param value
     *     The value
     */
    @JsonProperty("name")
    public void setValue(Object value) {
        this.value = value;
    }

    public UploadProperty withValue(Object value) {
        this.value = value;
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

    public UploadProperty withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(type).append(value).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UploadProperty)) {
            return false;
        }
        UploadProperty rhs = ((UploadProperty) other);
        return new EqualsBuilder().append(name, rhs.name).append(type, rhs.type).append(value, rhs.value).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
