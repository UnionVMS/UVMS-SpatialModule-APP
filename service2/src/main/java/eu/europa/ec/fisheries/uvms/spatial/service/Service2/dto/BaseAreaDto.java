package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;

public class BaseAreaDto {


    private long gid;
    private String code;
    private double distance;
    private String name;
    private Geometry geom;
    private AreaType type;

    public BaseAreaDto(long gid, String type, Geometry geom, String code, String name, double distance) {
        this.type = AreaType.valueOf(type);
        this.geom = geom;
        this.code = code;
        this.name = name;
        this.distance = distance;
        this.gid = gid;
    }

    public BaseAreaDto(String type, Geometry geom, String code, String name) {
        this.type = AreaType.valueOf(type);
        this.geom = geom;
        this.code = code;
        this.name = name;
    }


    public BaseAreaDto(String type, long gid, String code, double distance) {
        this.type = AreaType.valueOf(type);
        this.gid = gid;
        this.code = code;
        this.distance = distance;
    }

    public BaseAreaDto( long gid, String code, double distance) {
        this.type = AreaType.PORT;
        this.gid = gid;
        this.code = code;
        this.distance = distance;
    }

    public BaseAreaDto(String type, long gid, String code, String name) {
        this.type = AreaType.valueOf(type);
        this.code = code;
        this.name = name;
        this.gid = gid;
    }

    public BaseAreaDto() {
    }

    public AreaType getType() {
        return type;
    }

    /*public void setType(AreaType type) {
        this.type = type;
    }*/

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

    public Geometry getGeom() {
        return geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
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
}
