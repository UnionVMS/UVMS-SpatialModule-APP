package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

/**
 * Created by Michal Kopyczok on 03-Sep-15.
 */
public class ClosestAreaDto {
    private String id;
    private String areaType;
    private double distance;
    private String unit;

    public ClosestAreaDto(String id, String areaType, double distance, String unit) {
        this.id = id;
        this.areaType = areaType;
        this.distance = distance;
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public String getAreaType() {
        return areaType;
    }

    public double getDistance() {
        return distance;
    }

    public String getUnit() {
        return unit;
    }

}
