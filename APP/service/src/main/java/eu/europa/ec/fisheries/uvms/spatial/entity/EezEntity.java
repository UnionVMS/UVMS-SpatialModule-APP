package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@NamedQueries({
        @NamedQuery(name = EezEntity.EEZ_BY_COORDINATE, query = "FROM EezEntity WHERE intersects(geom, :shape) = true) AND enabled = 'Y'"),
        @NamedQuery(name = QueryNameConstants.EEZ_COLUMNS, query = "SELECT eez.name AS name, eez.code AS code FROM EezEntity AS eez WHERE eez.gid =:gid"),
        @NamedQuery(name = EezEntity.DISABLE_EEZ_AREAS, query = "UPDATE EezEntity SET enabled = 'N'"),
        @NamedQuery(name = EezEntity.LIST_EMPTY_GEOMETRIES, query = "FROM EezEntity WHERE isEmpty(geom) = true AND enabled = 'Y'")
})
@Where(clause = "enabled = 'Y'")
@Table(name = "eez", schema = "spatial")
@EqualsAndHashCode(callSuper = true)
public class EezEntity extends BaseAreaEntity {

    public static final String EEZ_BY_COORDINATE = "eezEntity.ByCoordinate";
    public static final String LIST_EMPTY_GEOMETRIES = "eezEntity.TEST";
    public static final String DISABLE_EEZ_AREAS = "eezEntity.disableEezAreas";

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

    public EezEntity() {
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

}
