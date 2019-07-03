package eu.europa.ec.fisheries.uvms.spatial.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseAreaDto {


    private long gid;
    private String code;
    private String name;
    private AreaType type;
    private double distance;
    private String geometryWKT;

    public BaseAreaDto(String type, long gid, String code, String name) {
        this.type = AreaType.valueOf(type);
        this.code = code;
        this.name = name;
        this.gid = gid;
    }

    public BaseAreaDto(String type, long gid, String code, String name, Double distance) {
        this.type = AreaType.valueOf(type);
        this.code = code;
        this.name = name;
        this.gid = gid;
        this.distance = distance;
    }

    public BaseAreaDto() {
    }

    public AreaType getType() {
        return type;
    }

    public void setType(AreaType type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = AreaType.valueOf(type);
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

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getGeometryWKT() {
        return geometryWKT;
    }

    public void setGeometryWKT(String geometryWKT) {
        this.geometryWKT = geometryWKT;
    }
}
