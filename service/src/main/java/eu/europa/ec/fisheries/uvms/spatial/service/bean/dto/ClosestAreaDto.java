package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

/**
 * Created by Michal Kopyczok on 03-Sep-15.
 */
public class ClosestAreaDto {
    private String id;
    private String areaType;
    private double distance;
    private String unit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
