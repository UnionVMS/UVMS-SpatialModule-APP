package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

/**
 * Created by padhyad on 6/6/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SystemAreaNamesDto {

    @JsonProperty("code")
    private String code;

    @JsonProperty("areaNames")
    private Set<String> areaNames;

    public SystemAreaNamesDto(String code, Set<String> areaNames) {
        this.code = code;
        this.areaNames = areaNames;
    }

    /**
     *
     * @return
     * The code
     */
    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     * The code
     */
    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    /**
     *
     * @return
     */
    @JsonProperty("areaNames")
    public Set<String> getAreaNames() {
        return areaNames;
    }

    /**
     *
     * @param areaNames
     */
    @JsonProperty("areaNames")
    public void setAreaNames(Set<String> areaNames) {
        this.areaNames = areaNames;
    }
}
