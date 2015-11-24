package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "epsgCode",
        "units",
        "isWorld"
})
public class ProjectionDto {

    @JsonProperty("epsgCode")
    private Integer epsgCode;
    @JsonProperty("units")
    private String units;
    @JsonProperty("isWorld")
    private Boolean isWorld;

    /**
     * No args constructor for use in serialization
     */
    public ProjectionDto() {
    }

    public ProjectionDto(Integer epsgCode, String units, Boolean isWorld) {
        this.epsgCode = epsgCode;
        this.units = units;
        this.isWorld = isWorld;
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

    @JsonProperty("isWorld")
    public Boolean getIsWorld() {
        return isWorld;
    }

    @JsonProperty("isWorld")
    public void setIsWorld(Boolean isWorld) {
        this.isWorld = isWorld;
    }
}
