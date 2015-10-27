
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
    "first",
    "second",
    "third",
    "fourth"
})
public class Speed {

    @JsonProperty("first")
    private String first;
    @JsonProperty("second")
    private String second;
    @JsonProperty("third")
    private String third;
    @JsonProperty("fourth")
    private String fourth;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Speed() {
    }

    /**
     * 
     * @param fourth
     * @param second
     * @param third
     * @param first
     */
    public Speed(String first, String second, String third, String fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    /**
     * 
     * @return
     *     The first
     */
    @JsonProperty("first")
    public String getFirst() {
        return first;
    }

    /**
     * 
     * @param first
     *     The first
     */
    @JsonProperty("first")
    public void setFirst(String first) {
        this.first = first;
    }

    public Speed withFirst(String first) {
        this.first = first;
        return this;
    }

    /**
     * 
     * @return
     *     The second
     */
    @JsonProperty("second")
    public String getSecond() {
        return second;
    }

    /**
     * 
     * @param second
     *     The second
     */
    @JsonProperty("second")
    public void setSecond(String second) {
        this.second = second;
    }

    public Speed withSecond(String second) {
        this.second = second;
        return this;
    }

    /**
     * 
     * @return
     *     The third
     */
    @JsonProperty("third")
    public String getThird() {
        return third;
    }

    /**
     * 
     * @param third
     *     The third
     */
    @JsonProperty("third")
    public void setThird(String third) {
        this.third = third;
    }

    public Speed withThird(String third) {
        this.third = third;
        return this;
    }

    /**
     * 
     * @return
     *     The fourth
     */
    @JsonProperty("fourth")
    public String getFourth() {
        return fourth;
    }

    /**
     * 
     * @param fourth
     *     The fourth
     */
    @JsonProperty("fourth")
    public void setFourth(String fourth) {
        this.fourth = fourth;
    }

    public Speed withFourth(String fourth) {
        this.fourth = fourth;
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

    public Speed withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
