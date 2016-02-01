package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "units",
        "epsgCode",
        "format"
})
public class ControlDto {

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
    public ControlDto() {
    }

    public ControlDto(String type) {
        this.type = type;
    }

    public ControlDto(String type, String units, Integer epsgCode, String format) {
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

    @JsonProperty("units")
    public String getUnits() {
        return units;
    }

    @JsonProperty("units")
    public void setUnits(String units) {
        this.units = units;
    }

    @JsonProperty("epsgCode")
    public Integer getEpsgCode() {
        return epsgCode;
    }

    @JsonProperty("epsgCode")
    public void setEpsgCode(Integer epsgCode) {
        this.epsgCode = epsgCode;
    }

    @JsonProperty("format")
    public String getFormat() {
        return format;
    }

    @JsonProperty("format")
    public void setFormat(String format) {
        this.format = format;
    }
}
