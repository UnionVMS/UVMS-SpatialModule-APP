package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

/**
 * Created by Michal Kopyczok on 24-Aug-15.
 */
public class AreaIdentifierDto {

    private String id;
    private String areaType;

    public AreaIdentifierDto() {
    }

    public AreaIdentifierDto(String id, String areaType) {
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
