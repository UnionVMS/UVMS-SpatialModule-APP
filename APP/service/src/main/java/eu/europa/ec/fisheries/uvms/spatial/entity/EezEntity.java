package eu.europa.ec.fisheries.uvms.spatial.entity;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.entity.converter.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import lombok.EqualsAndHashCode;
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
import java.math.BigDecimal;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name = EezEntity.EEZ_BY_COORDINATE, query = "FROM EezEntity WHERE intersects(geom, :shape) = true) AND enabled = 'Y'"),
        @NamedQuery(name = QueryNameConstants.EEZ_COLUMNS, query = "SELECT eez.name AS name, eez.code AS code FROM EezEntity AS eez WHERE eez.gid =:gid"),
        @NamedQuery(name = EezEntity.DISABLE_EEZ_AREAS, query = "UPDATE EezEntity SET enabled = 'N'"),
        @NamedQuery(name = EezEntity.LIST_EMPTY_GEOMETRIES, query = "FROM EezEntity WHERE isEmpty(geom) = true AND enabled = 'Y'")

})
@Where(clause = "enabled = 'Y'")
@Table(name = "eez", schema = "spatial")
@EqualsAndHashCode
public class EezEntity implements Serializable {

    public static final String EEZ_BY_COORDINATE = "eezEntity.ByCoordinate";
    public static final String LIST_EMPTY_GEOMETRIES = "eezEntity.TEST";
    public static final String DISABLE_EEZ_AREAS = "eezEntity.disableEezAreas";

    @Id
    @Column(name = "gid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnAliasName(aliasName = "gid")
    private long gid;

    @Column(name = "geom")
    @Type(type = "org.hibernate.spatial.GeometryType")
    @ColumnAliasName(aliasName = "geometry")
    private Geometry geom;

    @Column(name = "name", length = 200)
    @ColumnAliasName(aliasName = "name")
    private String name;

    @Column(name = "country", length = 100)
    @ColumnAliasName(aliasName = "country")
    private String country;

    @Column(name = "sovereign", length = 100)
    @ColumnAliasName(aliasName = "sovereign")
    private String sovereign;

    @Column(name = "remarks", length = 150)
    @ColumnAliasName(aliasName = "remarks")
    private String remarks;

    @Column(name = "sov_id")
    @ColumnAliasName(aliasName = "sovId")
    private Integer sovId;

    @Column(name = "eez_id")
    @ColumnAliasName(aliasName = "eezId")
    private Long eezId;

    @Column(name = "code", length = 5)
    @ColumnAliasName(aliasName = "code")
    private String code;

    @Column(name = "mrgid")
    @ColumnAliasName(aliasName = "mrgid")
    private BigDecimal mrgid;

    @Column(name = "date_chang", length = 50)
    @ColumnAliasName(aliasName = "dateChang")
    private String dateChang;

    @Column(name = "area_m2")
    @ColumnAliasName(aliasName = "areaM2")
    private Double areaM2;

    @Column(name = "longitude")
    @ColumnAliasName(aliasName = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    @ColumnAliasName(aliasName = "latitude")
    private Double latitude;

    @Column(name = "mrgid_eez")
    @ColumnAliasName(aliasName = "mrgidEez")
    private Integer mrgidEez;

    @Convert(converter = CharBooleanConverter.class)
    @Column(name = "enabled", nullable = false, length = 1)
    private Boolean enabled = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enabled_on")
    private Date enabledOn;

    public EezEntity() {
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSovereign() {
        return this.sovereign;
    }

    public void setSovereign(String sovereign) {
        this.sovereign = sovereign;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getSovId() {
        return this.sovId;
    }

    public void setSovId(Integer sovId) {
        this.sovId = sovId;
    }

    public Long getEezId() {
        return this.eezId;
    }

    public void setEezId(Long eezId) {
        this.eezId = eezId;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getMrgid() {
        return this.mrgid;
    }

    public void setMrgid(BigDecimal mrgid) {
        this.mrgid = mrgid;
    }

    public String getDateChang() {
        return this.dateChang;
    }

    public void setDateChang(String dateChang) {
        this.dateChang = dateChang;
    }

    public Double getAreaM2() {
        return this.areaM2;
    }

    public void setAreaM2(Double areaM2) {
        this.areaM2 = areaM2;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getMrgidEez() {
        return this.mrgidEez;
    }

    public void setMrgidEez(Integer mrgidEez) {
        this.mrgidEez = mrgidEez;
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
