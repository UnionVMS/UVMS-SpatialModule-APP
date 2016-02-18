package eu.europa.ec.fisheries.uvms.spatial.entity;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.entity.converter.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.GeoJsonDto;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@SqlResultSetMappings({
        @SqlResultSetMapping(name = "implicit.portareas", entities = @EntityResult(entityClass = PortAreasEntity.class))
})
@NamedQueries({
        @NamedQuery(name = QueryNameConstants.FIND_PORT_AREA_BY_ID,
                query = "SELECT portarea FROM PortAreasEntity portarea WHERE portarea.gid = :portAreaId"),
        @NamedQuery(name = QueryNameConstants.DISABLE_PORT_AREAS, query = "update PortAreasEntity set enabled = 'N'")
})
@NamedNativeQueries({
        @NamedNativeQuery(
                name = QueryNameConstants.PORTAREA_BY_COORDINATE,
                query = "select * from port_area where st_intersects(geom, st_geomfromtext(CAST(:wktPoint as text), :crs)) and enabled = 'Y'"
                , resultSetMapping = "implicit.port")
})
@Where(clause = "enabled = 'Y'")
@Table(name = "port_area", schema = "spatial")
public class PortAreasEntity implements Serializable {

    private static final long serialVersionUID = -2233177907262739920L;

    @Id
    @Column(name = "gid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnAliasName(aliasName = "gid")
    private long gid;

    @Column(name = "geom", nullable = false)
    @Type(type = "org.hibernate.spatial.GeometryType")
    @ColumnAliasName(aliasName = GeoJsonDto.GEOMETRY)
    private Geometry geom;

    @Column(name = "code", length = 10)
    @ColumnAliasName(aliasName = "code")
    private String code;

    @Convert(converter = CharBooleanConverter.class)
    @Column(name = "enabled", nullable = false, length = 1)
    private Boolean enabled = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enabled_on")
    private Date enabledOn;

    public PortAreasEntity() {
    }

    public long getGid() {
        return this.gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public Geometry getGeom() {
        return geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
