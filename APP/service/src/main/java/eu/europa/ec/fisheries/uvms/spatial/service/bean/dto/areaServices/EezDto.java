package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices;

import com.vividsolutions.jts.geom.Geometry;
import java.math.BigInteger;
import java.util.Date;

public class EezDto {

    private Geometry geometry;
    private String name;
    private String country;
    private String sovereign;
    private String remarks;
    private String code;
    private String dateChange;
    private Long sovId;
    private Long eezId;
    private Long mrgidEez;
    private BigInteger mrgid;
    private BigInteger gid;
    private Double areaM2;
    private Double longitude;
    private Double latitude;
    private Boolean enabled;
    private Date enabledOn;

    public BigInteger getGid() {
        return gid;
    }

    public void setGid(BigInteger gid) {
        this.gid = gid;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSovereign() {
        return sovereign;
    }

    public void setSovereign(String sovereign) {
        this.sovereign = sovereign;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getSovId() {
        return sovId;
    }

    public void setSovId(long sovId) {
        this.sovId = sovId;
    }

    public Long getEezId() {
        return eezId;
    }

    public void setEezId(long eezId) {
        this.eezId = eezId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigInteger getMrgid() {
        return mrgid;
    }

    public void setMrgid(BigInteger mrgid) {
        this.mrgid = mrgid;
    }

    public String getDateChang() {
        return dateChange;
    }

    public void setDateChang(String dateChang) {
        this.dateChange = dateChang;
    }

    public Double getAreaM2() {
        return areaM2;
    }

    public void setAreaM2(double areaM2) {
        this.areaM2 = areaM2;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Long getMrgidEez() {
        return mrgidEez;
    }

    public void setMrgidEez(long mrgidEez) {
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
