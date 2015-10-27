
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
//@JsonPropertyOrder({
//    "dnk",
//    "swe"
//})
public class FlagState {

//    @JsonProperty("dnk")
//    private String dnk;
//    @JsonProperty("swe")
//    private String swe;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public FlagState() {
    }

//    /**
//     *
//     * @param dnk
//     * @param swe
//     */
//    public FlagState(String dnk, String swe) {
//        this.dnk = dnk;
//        this.swe = swe;
//    }

//    /**
//     *
//     * @return
//     *     The dnk
//     */
//    @JsonProperty("dnk")
//    public String getDnk() {
//        return dnk;
//    }
//
//    /**
//     *
//     * @param dnk
//     *     The dnk
//     */
//    @JsonProperty("dnk")
//    public void setDnk(String dnk) {
//        this.dnk = dnk;
//    }
//
//    public FlagState withDnk(String dnk) {
//        this.dnk = dnk;
//        return this;
//    }
//
//    /**
//     *
//     * @return
//     *     The swe
//     */
//    @JsonProperty("swe")
//    public String getSwe() {
//        return swe;
//    }
//
//    /**
//     *
//     * @param swe
//     *     The swe
//     */
//    @JsonProperty("swe")
//    public void setSwe(String swe) {
//        this.swe = swe;
//    }
//
//    public FlagState withSwe(String swe) {
//        this.swe = swe;
//        return this;
//    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public FlagState withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
