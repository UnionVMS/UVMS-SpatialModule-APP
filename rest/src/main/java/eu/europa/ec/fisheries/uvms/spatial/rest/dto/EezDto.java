package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.europa.ec.fisheries.uvms.movement.MovementDtoSerializer;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.serializer.EezDtoSerializer;

import java.math.BigInteger;

/**
 * Created by kopyczmi on 11-Aug-15.
 */
@JsonSerialize(using = EezDtoSerializer.class)
public class EezDto {
    private int gid;
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

    private String geometryType;
    private String coordinates;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getEez() {
        return eez;
    }

    public void setEez(String eez) {
        this.eez = eez;
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

    public Integer getSovId() {
        return sovId;
    }

    public void setSovId(Integer sovId) {
        this.sovId = sovId;
    }

    public Integer getEezId() {
        return eezId;
    }

    public void setEezId(Integer eezId) {
        this.eezId = eezId;
    }

    public String getIso3Digit() {
        return iso3Digit;
    }

    public void setIso3Digit(String iso3Digit) {
        this.iso3Digit = iso3Digit;
    }

    public BigInteger getMrgid() {
        return mrgid;
    }

    public void setMrgid(BigInteger mrgid) {
        this.mrgid = mrgid;
    }

    public String getDateChang() {
        return dateChang;
    }

    public void setDateChang(String dateChang) {
        this.dateChang = dateChang;
    }

    public Double getAreaM2() {
        return areaM2;
    }

    public void setAreaM2(Double areaM2) {
        this.areaM2 = areaM2;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getMrgidEez() {
        return mrgidEez;
    }

    public void setMrgidEez(Integer mrgidEez) {
        this.mrgidEez = mrgidEez;
    }

    public String getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
