package eu.europa.ec.fisheries.uvms.spatial.model.mapfish.request;

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
public class Alarms {

    @JsonProperty("title")
    private String title;
    @JsonProperty("cluster")
    @Valid
    private Cluster cluster;
    @JsonProperty("classes")
    @Valid
    private List<Class> classes = new ArrayList<>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Alarms() {
    }

    /**
     *
     * @param classes
     * @param title
     * @param cluster
     */
    public Alarms(String title, Cluster cluster, List<Class> classes) {
        this.title = title;
        this.cluster = cluster;
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

    public Alarms withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     *
     * @return
     *     The cluster
     */
    @JsonProperty("cluster")
    public Cluster getCluster() {
        return cluster;
    }

    /**
     *
     * @param cluster
     *     The cluster
     */
    @JsonProperty("cluster")
    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Alarms withCluster(Cluster cluster) {
        this.cluster = cluster;
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

    public Alarms withClasses(List<Class> classes) {
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

    public Alarms withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(title).append(cluster).append(classes).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Alarms) == false) {
            return false;
        }
        Alarms rhs = ((Alarms) other);
        return new EqualsBuilder().append(title, rhs.title).append(cluster, rhs.cluster).append(classes, rhs.classes).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
