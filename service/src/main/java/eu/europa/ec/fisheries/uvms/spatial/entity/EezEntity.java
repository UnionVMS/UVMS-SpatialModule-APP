package eu.europa.ec.fisheries.uvms.spatial.entity;

import com.vividsolutions.jts.geom.Geometry;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Created by kopyczmi on 06-Aug-15.
 */
@Entity
@Table(name = "eez", schema = "spatial", catalog = "uvms")
@EqualsAndHashCode
@ToString
public class EezEntity {
    private long gid;
    private Geometry geom;
    private String eez;
    private String country;
    private String sovereign;
    private String remarks;
    private Integer sovId;
    private Integer eezId;
    private String iso3Digit;
    private BigInteger mrgid;
    private String dateChang;
    private Double areaM2;
    private Double longitude;
    private Double latitude;
    private Integer mrgidEez;

    @Id
    @Column(name = "gid")
    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    @Basic
    @Column(name = "geom")
    @Type(type = "org.hibernate.spatial.GeometryType")
    public Geometry getGeom() {
        return geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

    @Basic
    @Column(name = "eez")
    public String getEez() {
        return eez;
    }

    public void setEez(String eez) {
        this.eez = eez;
    }

    @Basic
    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "sovereign")
    public String getSovereign() {
        return sovereign;
    }

    public void setSovereign(String sovereign) {
        this.sovereign = sovereign;
    }

    @Basic
    @Column(name = "remarks")
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Basic
    @Column(name = "sov_id")
    public Integer getSovId() {
        return sovId;
    }

    public void setSovId(Integer sovId) {
        this.sovId = sovId;
    }

    @Basic
    @Column(name = "eez_id")
    public Integer getEezId() {
        return eezId;
    }

    public void setEezId(Integer eezId) {
        this.eezId = eezId;
    }

    @Basic
    @Column(name = "iso_3digit")
    public String getIso3Digit() {
        return iso3Digit;
    }

    public void setIso3Digit(String iso3Digit) {
        this.iso3Digit = iso3Digit;
    }

    @Basic
    @Column(name = "mrgid")
    public BigInteger getMrgid() {
        return mrgid;
    }

    public void setMrgid(BigInteger mrgid) {
        this.mrgid = mrgid;
    }

    @Basic
    @Column(name = "date_chang")
    public String getDateChang() {
        return dateChang;
    }

    public void setDateChang(String dateChang) {
        this.dateChang = dateChang;
    }

    @Basic
    @Column(name = "area_m2")
    public Double getAreaM2() {
        return areaM2;
    }

    public void setAreaM2(Double areaM2) {
        this.areaM2 = areaM2;
    }

    @Basic
    @Column(name = "longitude")
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "latitude")
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "mrgid_eez")
    public Integer getMrgidEez() {
        return mrgidEez;
    }

    public void setMrgidEez(Integer mrgidEez) {
        this.mrgidEez = mrgidEez;
    }

}
