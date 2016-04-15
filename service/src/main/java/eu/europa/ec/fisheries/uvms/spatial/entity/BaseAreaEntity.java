package eu.europa.ec.fisheries.uvms.spatial.entity;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
@ToString
@EqualsAndHashCode
public class BaseAreaEntity implements Serializable {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @ColumnAliasName(aliasName = "gid") Long gid;
    private @Type(type = "org.hibernate.spatial.GeometryType") @ColumnAliasName(aliasName = "geometry") Geometry geom;

    protected BaseAreaEntity(){
        this.gid = null;
    }

    public Long getGid() {
        return gid;
    }

    public Geometry getGeom() {
        return this.geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }
}
