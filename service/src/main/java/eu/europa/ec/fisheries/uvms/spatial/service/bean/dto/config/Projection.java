package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "epsgCode",
        "formats",
        "units",
        "global"
})
public class Projection {

    @JsonProperty("name")
    private String name;
    @JsonProperty("epsgCode")
    private Integer epsgCode;
    @JsonProperty("formats")
    private String formats;
    @JsonProperty("units")
    private String units;
    @JsonProperty("global")
    private Boolean global;
    /**
     * No args constructor for use in serialization
     */
    public Projection() {
    }

    public Projection(String name, Integer epsgCode, String formats, String units, Boolean global) {
        this.name = name;
        this.epsgCode = epsgCode;
        this.formats = formats;
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

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Projection withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("formats")
    public String getFormats() {
        return formats;
    }

    @JsonProperty("formats")
    public void setFormats(String formats) {
        this.formats = formats;
    }

    public Projection withFormats(String formats) {
        this.formats = formats;
        return this;
    }

}
