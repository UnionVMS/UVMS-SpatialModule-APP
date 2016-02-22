package eu.europa.ec.fisheries.uvms.spatial.model.mapfish.request;

import java.lang.Object;
import java.lang.Override;
import java.lang.String;
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
public class Segments {

    @JsonProperty("title")
    private String title;
    @JsonProperty("lineStyle")
    private String lineStyle;
    @JsonProperty("classes")
    @Valid
    private List<Class> classes = new ArrayList<Class>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Segments() {
    }

    /**
     * 
     * @param classes
     * @param title
     * @param lineStyle
     */
    public Segments(String title, String lineStyle, List<Class> classes) {
        this.title = title;
        this.lineStyle = lineStyle;
        this.classes = classes;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public Segments withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 
     * @return
     *     The lineStyle
     */
    @JsonProperty("lineStyle")
    public String getLineStyle() {
        return lineStyle;
    }

    /**
     * 
     * @param lineStyle
     *     The lineStyle
     */
    @JsonProperty("lineStyle")
    public void setLineStyle(String lineStyle) {
        this.lineStyle = lineStyle;
    }

    public Segments withLineStyle(String lineStyle) {
        this.lineStyle = lineStyle;
        return this;
    }

    /**
     * 
     * @return
     *     The classes
     */
    @JsonProperty("classes")
    public List<Class> getClasses() {
        return classes;
    }

    /**
     * 
     * @param classes
     *     The classes
     */
    @JsonProperty("classes")
    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public Segments withClasses(List<Class> classes) {
        this.classes = classes;
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

    public Segments withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(title).append(lineStyle).append(classes).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Segments) == false) {
            return false;
        }
        Segments rhs = ((Segments) other);
        return new EqualsBuilder().append(title, rhs.title).append(lineStyle, rhs.lineStyle).append(classes, rhs.classes).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
