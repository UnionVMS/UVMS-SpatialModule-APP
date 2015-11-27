package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MapSettingsDto {

    @JsonProperty("refreshState")
    private String refreshState;

    @JsonProperty("scaleBarUnits")
    private String scaleBarUnits;

    @JsonProperty("coordinatesFormat")
    private String coordinatesFormat;

    @JsonProperty("mapProjection")
    private String mapProjection;

    @JsonProperty("refreshRate")
    private String refreshRate;

    @JsonProperty("displayProjection")
    private String displayProjection;

    public MapSettingsDto() {}

    public MapSettingsDto(String refreshState, String scaleBarUnits, String coordinatesFormat, String mapProjection, String refreshRate, String displayProjection) {
        this.refreshRate = refreshRate;
        this.scaleBarUnits =scaleBarUnits;
        this.coordinatesFormat = coordinatesFormat;
        this.mapProjection = mapProjection;
        this.refreshState = refreshState;
        this.displayProjection = displayProjection;
    }

    @JsonProperty("refreshState")
    public String getRefreshState() {
        return refreshState;
    }

    @JsonProperty("refreshState")
    public void setRefreshState(String refreshState) {
        this.refreshState = refreshState;
    }

    @JsonProperty("scaleBarUnits")
    public String getScaleBarUnits() {
        return scaleBarUnits;
    }

    @JsonProperty("scaleBarUnits")
    public void setScaleBarUnits(String scaleBarUnits) {
        this.scaleBarUnits = scaleBarUnits;
    }

    @JsonProperty("coordinatesFormat")
    public String getCoordinatesFormat() {
        return coordinatesFormat;
    }

    @JsonProperty("coordinatesFormat")
    public void setCoordinatesFormat(String coordinatesFormat) {
        this.coordinatesFormat = coordinatesFormat;
    }

    @JsonProperty("mapProjection")
    public String getMapProjection() {
        return mapProjection;
    }

    @JsonProperty("mapProjection")
    public void setMapProjection(String mapProjection) {
        this.mapProjection = mapProjection;
    }

    @JsonProperty("refreshRate")
    public String getRefreshRate() {
        return refreshRate;
    }

    @JsonProperty("refreshRate")
    public void setRefreshRate(String refreshRate) {
        this.refreshRate = refreshRate;
    }

    @JsonProperty("displayProjection")
    public String getDisplayProjection() {
        return displayProjection;
    }

    @JsonProperty("displayProjection")
    public void setDisplayProjection(String displayProjection) {
        this.displayProjection = displayProjection;
    }

    @Override
    public String toString() {
        return "ClassPojo [refreshState = " + refreshState + ", scaleBarUnits = " + scaleBarUnits + ", coordinatesFormat = " + coordinatesFormat + ", mapProjection = " + mapProjection + ", refreshRate = " + refreshRate + ", displayProjection = " + displayProjection + "]";
    }
}
