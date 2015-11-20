package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FilterAreasDto {

    @JsonProperty("wkt_geometry")
    private String wktGeometry;

    @JsonProperty("result_code")
    private int resultCode;

    @JsonProperty("wkt_geometry")
    public String getWktGeometry() {
        return wktGeometry;
    }

    @JsonProperty("wkt_geometry")
    public void setWktGeometry(String wktGeometry) {
        this.wktGeometry = wktGeometry;
    }

    @JsonProperty("result_code")
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty("result_code")
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
