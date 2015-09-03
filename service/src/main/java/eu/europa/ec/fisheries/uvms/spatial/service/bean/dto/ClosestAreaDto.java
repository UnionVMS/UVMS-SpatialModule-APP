package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

/**
 * Created by Michal Kopyczok on 03-Sep-15.
 */
public class ClosestAreaDto {
    private String id;
    private String areaTypeName;
    private double distance;
    private String unit;

    public ClosestAreaDto(String id, String areaTypeName, double distance, String unit) {
        this.id = id;
        this.areaTypeName = areaTypeName;
        this.distance = distance;
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public String getAreaTypeName() {
        return areaTypeName;
    }

    public double getDistance() {
        return distance;
    }

    public String getUnit() {
        return unit;
    }

}
