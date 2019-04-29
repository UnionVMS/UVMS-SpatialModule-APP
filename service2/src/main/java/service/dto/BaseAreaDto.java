package service.dto;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;

public class BaseAreaDto {

    private AreaType type;
    private Geometry geom;
    private String code;
    private String name;

    public BaseAreaDto(String type, Geometry geom, String code, String name) {
        this.type = AreaType.valueOf(type);
        this.geom = geom;
        this.code = code;
        this.name = name;
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

    public Geometry getGeom() {
        return geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }
}
