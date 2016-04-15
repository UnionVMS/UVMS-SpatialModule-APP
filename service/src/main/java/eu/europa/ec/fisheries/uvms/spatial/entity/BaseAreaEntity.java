package eu.europa.ec.fisheries.uvms.spatial.entity;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.entity.converter.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@ToString
@EqualsAndHashCode
public class BaseAreaEntity implements Serializable {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @ColumnAliasName(aliasName = "gid") Long gid;
    @Type(type = "org.hibernate.spatial.GeometryType") @ColumnAliasName(aliasName = "geometry")
    protected Geometry geom;

    @Column(name = "name", length = 255)
    @ColumnAliasName(aliasName="name")
    protected String name;

    @Column(name = "code", length = 5)
    @ColumnAliasName(aliasName = "code")
    protected String code;

    @Convert(converter = CharBooleanConverter.class)
    @Column(name = "enabled", nullable = false, length = 1)
    @ColumnAliasName(aliasName ="enabled")
    protected Boolean enabled = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enabled_on")
    protected Date enabledOn;

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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
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
