package eu.europa.ec.fisheries.uvms.spatial.entity;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.entity.converter.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@SqlResultSetMappings({
        @SqlResultSetMapping(name = "implicit.rfmo", entities = @EntityResult(entityClass = RfmoEntity.class))
})
@NamedNativeQuery(
        name = QueryNameConstants.RFMO_BY_COORDINATE,
        query = "select * from rfmo where st_intersects(geom, st_geomfromtext(CAST(:wktPoint as text), :crs)) and enabled = 'Y'", resultSetMapping = "implicit.rfmo")
@NamedQueries({
        @NamedQuery(name = QueryNameConstants.RFMO_COLUMNS, query = "select rfmo.name as name, rfmo.code as code from RfmoEntity as rfmo where rfmo.gid =:gid and rfmo.enabled = 'Y'"),
        @NamedQuery(name = QueryNameConstants.DISABLE_RFMO_AREAS, query = "update RfmoEntity set enabled = 'N'")
})
@Table(name = "rfmo", schema = "spatial")
public class RfmoEntity implements Serializable {

    private static final long serialVersionUID = 6797853213499502870L;

    @Id
    @Column(name = "gid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnAliasName(aliasName = "gid")
    private long gid;

    @Basic
    @Column(name = "geom")
    @Type(type = "org.hibernate.spatial.GeometryType")
    @ColumnAliasName(aliasName = "geometry")
    private Geometry geom;

    @Column(name = "code", length = 10)
    @ColumnAliasName(aliasName = "code")
    private String code;

    @Column(name = "name", length = 125)
    @ColumnAliasName(aliasName = "name")
    private String name;

    @Column(name = "tuna", length = 10)
    @ColumnAliasName(aliasName = "tuna")
    private String tuna;

    @Convert(converter = CharBooleanConverter.class)
    @Column(name = "enabled", nullable = false, length = 1)
    private Boolean enabled = false;

    public RfmoEntity() {
    }

    public long getGid() {
        return this.gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public Geometry getGeom() {
        return this.geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTuna() {
        return this.tuna;
    }

    public void setTuna(String tuna) {
        this.tuna = tuna;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
