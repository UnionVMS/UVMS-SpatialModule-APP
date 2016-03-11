package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "id",
        "areaType",
        "code",
        "name",
})
public class AreaExtendedIdentifierDto {

    private String id;
    private String areaType;
    private String code;
    private String name;

    public AreaExtendedIdentifierDto() {
    }

    public AreaExtendedIdentifierDto(String id, String areaType) {
        this.id = id;
        this.areaType = areaType;
    }

    public AreaExtendedIdentifierDto(String id, String areaType, String code, String name) {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
