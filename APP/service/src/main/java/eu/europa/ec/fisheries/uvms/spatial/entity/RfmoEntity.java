package eu.europa.ec.fisheries.uvms.spatial.entity;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.entity.converter.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name = RfmoEntity.RFMO_BY_COORDINATE,
                query = "FROM RfmoEntity WHERE intersects(geom, :shape) = true) AND enabled = 'Y'"),
        @NamedQuery(name = QueryNameConstants.RFMO_COLUMNS,
                query = "SELECT rfmo.name AS name, rfmo.code AS code FROM RfmoEntity AS rfmo WHERE rfmo.gid =:gid"),
        @NamedQuery(name = QueryNameConstants.DISABLE_RFMO_AREAS,
                query = "UPDATE RfmoEntity SET enabled = 'N'")
})
@Where(clause = "enabled = 'Y'")
@Table(name = "rfmo", schema = "spatial")
public class RfmoEntity implements Serializable {

    public static final String RFMO_BY_COORDINATE = "rfmoEntity.ByCoordinate";

    @Id
    @Column(name = "gid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnAliasName(aliasName = "gid")
    private long gid;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enabled_on")
    private Date enabledOn;

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

    public Date getEnabledOn() {
        return enabledOn;
    }

    public void setEnabledOn(Date enabledOn) {
        this.enabledOn = enabledOn;
    }
}
