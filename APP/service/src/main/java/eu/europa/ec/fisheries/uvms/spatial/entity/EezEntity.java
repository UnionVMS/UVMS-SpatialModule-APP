package eu.europa.ec.fisheries.uvms.spatial.entity;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;
import org.opengis.feature.Property;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    private static final String COUNTRY = "country";
    private static final String SOVEREIGN = "sovereign";
    private static final String REMARKS = "remarks";
    private static final String SOV_ID = "sov_id";
    private static final String EEZ_ID = "eez_id";
    private static final String MRGID = "mrgid";
    private static final String DATE_CHANG = "date_chang";
    private static final String AREA_M_2 = "area_m2";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String MRGID_EEZ = "mrgid_eez";

    @Column(length = 100)
    @ColumnAliasName(aliasName = COUNTRY)
    private String country;

    @Column(length = 100)
    @ColumnAliasName(aliasName = SOVEREIGN)
    private String sovereign;

    @Column(length = 150)
    @ColumnAliasName(aliasName = REMARKS)
    private String remarks;

    @Column(name = SOV_ID)
    @ColumnAliasName(aliasName = "sovId")
    private Long sovId;

    @Column(name = EEZ_ID)
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

    @Column(name = LONGITUDE)
    @ColumnAliasName(aliasName = LONGITUDE)
    private Double longitude;

    @Column(name = LATITUDE)
    @ColumnAliasName(aliasName = LATITUDE)
    private Double latitude;

    @Column(name = "mrgid_eez")
    @ColumnAliasName(aliasName = "mrgidEez")
    private Long mrgidEez;

    public EezEntity() {
    }

    public EezEntity(List<Property> properties) throws UnsupportedEncodingException {
        Map<String, Object> values = createAttributesMap(properties);
        setName(readStringProperty(values, NAME));
        setCountry(readStringProperty(values, COUNTRY));
        setSovereign(readStringProperty(values, SOVEREIGN));
        setRemarks(readStringProperty(values, REMARKS));
        setSovId((Long) values.get(SOV_ID));
        setEezId((Long) values.get(EEZ_ID));
        setCode(readStringProperty(values, CODE));
        setMrgid(BigDecimal.valueOf(((Double) values.get(MRGID)).longValue()));
        setDateChang(readStringProperty(values, DATE_CHANG));
        setAreaM2((Double) values.get(AREA_M_2));
        setLongitude((Double) values.get(LONGITUDE));
        setLatitude((Double) values.get(LATITUDE));
        setMrgidEez((Long) values.get(MRGID_EEZ));
        setGeom((Geometry) values.get(THE_GEOM));
        setEnabledOn(new Date());
        setEnabled(true);
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

    public Long getSovId() {
        return this.sovId;
    }

    public void setSovId(Long sovId) {
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

    public Long getMrgidEez() {
        return this.mrgidEez;
    }

    public void setMrgidEez(Long mrgidEez) {
        this.mrgidEez = mrgidEez;
    }

}
