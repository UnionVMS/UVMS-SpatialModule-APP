package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.CoordinatesFormat;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScaleBarUnits;

/**
 * Created by padhyad on 11/30/2015.
 */
public class DisplayProjectionDto {

    @JsonProperty("epsgCode")
    private Integer epsgCode;

    @JsonProperty("formats")
    private CoordinatesFormat formats;

    @JsonProperty("units")
    private ScaleBarUnits units;

    public DisplayProjectionDto(Integer epsgCode, CoordinatesFormat formats, ScaleBarUnits units) {
        this.epsgCode = epsgCode;
        this.formats = formats;
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

    @JsonProperty("formats")
    public CoordinatesFormat getFormats() {
        return formats;
    }

    @JsonProperty("formats")
    public void setFormats(CoordinatesFormat formats) {
        this.formats = formats;
    }

    @JsonProperty("units")
    public ScaleBarUnits getUnits() {
        return units;
    }

    @JsonProperty("units")
    public void setUnits(ScaleBarUnits units) {
        this.units = units;
    }
}
