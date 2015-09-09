package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

/**
 * Created by Michal Kopyczok on 24-Aug-15.
 */
public class AreaDto {

    private String id;
    private String areaType;

    public AreaDto(String id, String areaType) {
        this.id = id;
        this.areaType = areaType;
    }

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
}
