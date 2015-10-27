package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

public class FilterAreasDto {
    private String wkt_geometry;
    private int result_code;

    public String getWkt_geometry() {
        return wkt_geometry;
    }

    public void setWkt_geometry(String wkt_geometry) {
        this.wkt_geometry = wkt_geometry;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }
}
