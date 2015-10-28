package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

    /**
     * No args constructor for use in serialization
     */
    public Speed() {
    }

    public Speed(String first, String second, String third, String fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    @JsonProperty("first")
    public String getFirst() {
        return first;
    }

    @JsonProperty("first")
    public void setFirst(String first) {
        this.first = first;
    }

    public Speed withFirst(String first) {
        this.first = first;
        return this;
    }

    @JsonProperty("second")
    public String getSecond() {
        return second;
    }

    @JsonProperty("second")
    public void setSecond(String second) {
        this.second = second;
    }

    public Speed withSecond(String second) {
        this.second = second;
        return this;
    }

    @JsonProperty("third")
    public String getThird() {
        return third;
    }

    @JsonProperty("third")
    public void setThird(String third) {
        this.third = third;
    }

    public Speed withThird(String third) {
        this.third = third;
        return this;
    }

    @JsonProperty("fourth")
    public String getFourth() {
        return fourth;
    }

    @JsonProperty("fourth")
    public void setFourth(String fourth) {
        this.fourth = fourth;
    }

    public Speed withFourth(String fourth) {
        this.fourth = fourth;
        return this;
    }

}
