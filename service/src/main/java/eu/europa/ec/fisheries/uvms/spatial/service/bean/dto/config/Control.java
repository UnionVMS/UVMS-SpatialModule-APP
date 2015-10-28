package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "units",
        "epsgCode",
        "format"
})
public class Control {

    @JsonProperty("type")
    private String type;
    @JsonProperty("units")
    private String units;
    @JsonProperty("epsgCode")
    private Integer epsgCode;
    @JsonProperty("format")
    private String format;

    /**
     * No args constructor for use in serialization
     */
    public Control() {
    }

    public Control(String type) {
        this.type = type;
    }

    public Control(String type, String units, Integer epsgCode, String format) {
        this.type = type;
        this.units = units;
        this.epsgCode = epsgCode;
        this.format = format;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public Control withType(String type) {
        this.type = type;
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

    public Control withUnits(String units) {
        this.units = units;
        return this;
    }

    @JsonProperty("epsgCode")
    public Integer getEpsgCode() {
        return epsgCode;
    }

    @JsonProperty("epsgCode")
    public void setEpsgCode(Integer epsgCode) {
        this.epsgCode = epsgCode;
    }

    public Control withEpsgCode(Integer epsgCode) {
        this.epsgCode = epsgCode;
        return this;
    }

    @JsonProperty("format")
    public String getFormat() {
        return format;
    }

    @JsonProperty("format")
    public void setFormat(String format) {
        this.format = format;
    }

    public Control withFormat(String format) {
        this.format = format;
        return this;
    }

}
