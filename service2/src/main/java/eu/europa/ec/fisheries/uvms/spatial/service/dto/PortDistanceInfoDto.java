package eu.europa.ec.fisheries.uvms.spatial.service.dto;

import com.vividsolutions.jts.geom.Geometry;

public class PortDistanceInfoDto {

    private String type;
    private long gid;
    private String code;
    private double distance;
    private String name;
    private Geometry geom;

    public PortDistanceInfoDto(String type, long gid, String code, double distance) {
        this.type = type;
        this.gid = gid;
        this.code = code;
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
