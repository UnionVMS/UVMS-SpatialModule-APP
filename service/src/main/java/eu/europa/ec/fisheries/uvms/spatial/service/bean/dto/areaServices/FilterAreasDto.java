package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices;

public class FilterAreasDto {

    private String wktgeometry;

    private int resultcode;

    public String getWktgeometry() {
        return wktgeometry;
    }

    public void setWktgeometry(String wktgeometry) {
        this.wktgeometry = wktgeometry;
    }

    public int getResultcode() {
        return resultcode;
    }

    public void setResultcode(int resultcode) {
        this.resultcode = resultcode;
    }
}
