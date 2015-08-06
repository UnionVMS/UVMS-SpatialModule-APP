package eu.europa.ec.fisheries.uvms.spatial.entity;

import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Created by kopyczmi on 06-Aug-15.
 */
@Entity
@Table(name = "eez", schema = "spatial", catalog = "uvms")
public class EezEntity { // TODO  please use lombok @Data like CountryEntity it would be nice also to use some naming instead of abreviation like eez
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EezEntity eez1 = (EezEntity) o;

        if (gid != eez1.gid) return false;
        if (geom != null ? !geom.equals(eez1.geom) : eez1.geom != null) return false;
        if (eez != null ? !eez.equals(eez1.eez) : eez1.eez != null) return false;
        if (country != null ? !country.equals(eez1.country) : eez1.country != null) return false;
        if (sovereign != null ? !sovereign.equals(eez1.sovereign) : eez1.sovereign != null) return false;
        if (remarks != null ? !remarks.equals(eez1.remarks) : eez1.remarks != null) return false;
        if (sovId != null ? !sovId.equals(eez1.sovId) : eez1.sovId != null) return false;
        if (eezId != null ? !eezId.equals(eez1.eezId) : eez1.eezId != null) return false;
        if (iso3Digit != null ? !iso3Digit.equals(eez1.iso3Digit) : eez1.iso3Digit != null) return false;
        if (mrgid != null ? !mrgid.equals(eez1.mrgid) : eez1.mrgid != null) return false;
        if (dateChang != null ? !dateChang.equals(eez1.dateChang) : eez1.dateChang != null) return false;
        if (areaM2 != null ? !areaM2.equals(eez1.areaM2) : eez1.areaM2 != null) return false;
        if (longitude != null ? !longitude.equals(eez1.longitude) : eez1.longitude != null) return false;
        if (latitude != null ? !latitude.equals(eez1.latitude) : eez1.latitude != null) return false;
        if (mrgidEez != null ? !mrgidEez.equals(eez1.mrgidEez) : eez1.mrgidEez != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Long.valueOf(gid).hashCode();
        result = 31 * result + (geom != null ? geom.hashCode() : 0);
        result = 31 * result + (eez != null ? eez.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (sovereign != null ? sovereign.hashCode() : 0);
        result = 31 * result + (remarks != null ? remarks.hashCode() : 0);
        result = 31 * result + (sovId != null ? sovId.hashCode() : 0);
        result = 31 * result + (eezId != null ? eezId.hashCode() : 0);
        result = 31 * result + (iso3Digit != null ? iso3Digit.hashCode() : 0);
        result = 31 * result + (mrgid != null ? mrgid.hashCode() : 0);
        result = 31 * result + (dateChang != null ? dateChang.hashCode() : 0);
        result = 31 * result + (areaM2 != null ? areaM2.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (mrgidEez != null ? mrgidEez.hashCode() : 0);
        return result;
    }
}
