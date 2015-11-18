package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FilterAreasDto {

    @JsonProperty("wkt_geometry")
    private String wktGeometry;

    @JsonProperty("result_code")
    private int resultCode;

    public String getWktGeometry() {
        return wktGeometry;
    }

    public void setWktGeometry(String wktGeometry) {
        this.wktGeometry = wktGeometry;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
