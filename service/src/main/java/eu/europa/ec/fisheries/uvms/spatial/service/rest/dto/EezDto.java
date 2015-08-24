package eu.europa.ec.fisheries.uvms.spatial.service.rest.dto;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.math.BigInteger;

public class EezDto {

    public static final SimpleFeatureType EEZ = build();

    private Geometry geometry;
    private String eez;
    private String country;
    private String sovereign;
    private String remarks;
    private String iso3Digit;
    private String dateChange;
    private Integer sovId;
    private Integer eezId;
    private Integer mrgidEez;
    private BigInteger mrgid;
    private BigInteger gid;
    private Double areaM2;
    private Double longitude;
    private Double latitude;

    public SimpleFeature toFeature(){
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(EEZ);
        featureBuilder.add(getGeometry());
        featureBuilder.add(getGid());
        featureBuilder.add(getEez());
        featureBuilder.add(getCountry());
        featureBuilder.add(getSovId());
        featureBuilder.add(getEezId());
        featureBuilder.add(getIso3Digit());
        featureBuilder.add(getMrgid());
        featureBuilder.add(getAreaM2());
        featureBuilder.add(getLongitude());
        featureBuilder.add(getLatitude());
        featureBuilder.add(getMrgidEez());
        featureBuilder.add(getSovereign());
        featureBuilder.add(getRemarks());
        return featureBuilder.buildFeature(String.valueOf(getEezId()));
    }

    private static SimpleFeatureType build() {

        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setCRS(DefaultGeographicCRS.WGS84); //TODO check it
        sb.setName("EEZ");
        sb.add("geometry", MultiPolygon.class);
        sb.add("gid", BigInteger.class);
        sb.add("eez", String.class);
        sb.add("country", String.class);
        sb.add("sovId", Integer.class);
        sb.add("eezId", Integer.class);
        sb.add("iso3Digit", String.class);
        sb.add("mrgId", BigInteger.class);
        sb.add("areaM2", Double.class);
        sb.add("longitude", Double.class);
        sb.add("latitude", Double.class);
        sb.add("mrgidEez", Integer.class);
        sb.add("sovereign", String.class);
        sb.add("remarks", String.class);
        return sb.buildFeatureType();
    }

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

    public void setSovId(int sovId) {
        this.sovId = sovId;
    }

    public Integer getEezId() {
        return eezId;
    }

    public void setEezId(int eezId) {
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

    public Integer getMrgidEez() {
        return mrgidEez;
    }

    public void setMrgidEez(int mrgidEez) {
        this.mrgidEez = mrgidEez;
    }

}
