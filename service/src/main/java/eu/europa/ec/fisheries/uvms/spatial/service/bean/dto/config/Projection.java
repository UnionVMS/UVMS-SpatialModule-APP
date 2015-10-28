package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "epsgCode",
        "units",
        "global"
})
public class Projection {

    @JsonProperty("epsgCode")
    private Integer epsgCode;
    @JsonProperty("units")
    private String units;
    @JsonProperty("global")
    private Boolean global;

    /**
     * No args constructor for use in serialization
     */
    public Projection() {
    }

    public Projection(Integer epsgCode, String units, Boolean global) {
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

    public Projection withEpsgCode(Integer epsgCode) {
        this.epsgCode = epsgCode;
        return this;
    }

    @JsonProperty("units")
    public String getUnits() {
        return units;
    }

    @JsonProperty("units")
    public void setUnits(String units) {
        this.units = units;
    }

    public Projection withUnits(String units) {
        this.units = units;
        return this;
    }

    @JsonProperty("global")
    public Boolean getGlobal() {
        return global;
    }

    @JsonProperty("global")
    public void setGlobal(Boolean global) {
        this.global = global;
    }

    public Projection withGlobal(Boolean global) {
        this.global = global;
        return this;
    }

}
