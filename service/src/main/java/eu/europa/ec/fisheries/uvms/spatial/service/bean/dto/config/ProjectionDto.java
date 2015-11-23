package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "epsgCode",
        "units",
        "global"
})
public class ProjectionDto {

    @JsonProperty("epsgCode")
    private Integer epsgCode;
    @JsonProperty("units")
    private String units;
    @JsonProperty("global")
    private Boolean global;

    /**
     * No args constructor for use in serialization
     */
    public ProjectionDto() {
    }

    public ProjectionDto(Integer epsgCode, String units, Boolean global) {
        this.epsgCode = epsgCode;
        this.units = units;
        this.global = global;
    }

    @JsonProperty("epsgCode")
    public Integer getEpsgCode() {
        return epsgCode;
    }

    @JsonProperty("epsgCode")
    public void setEpsgCode(Integer epsgCode) {
        this.epsgCode = epsgCode;
    }

    @JsonProperty("units")
    public String getUnits() {
        return units;
    }

    @JsonProperty("units")
    public void setUnits(String units) {
        this.units = units;
    }

    @JsonProperty("global")
    public Boolean getGlobal() {
        return global;
    }

    @JsonProperty("global")
    public void setGlobal(Boolean global) {
        this.global = global;
    }
}
