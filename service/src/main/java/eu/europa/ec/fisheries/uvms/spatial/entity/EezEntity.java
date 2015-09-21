package eu.europa.ec.fisheries.uvms.spatial.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;


@Entity
@SqlResultSetMappings({
	@SqlResultSetMapping(name = "implicit.eez", entities = @EntityResult(entityClass = EezEntity.class))
})
@NamedNativeQuery(
		name = QueryNameConstants.EEZ_BY_COORDINATE, 
		query = "select * from eez where st_intersects(geom, st_geomfromtext(CAST(:wktPoint as text), :crs))", resultSetMapping = "implicit.eez")
@Table(name = "eez", schema = "spatial")
public class EezEntity implements Serializable {

	private static final long serialVersionUID = 6797853213499502862L;

	@Id
	@Column(name = "gid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnAliasName(aliasName ="gid")
	private int gid;

    @Basic
    @Column(name = "geom")
    @Type(type = "org.hibernate.spatial.GeometryType")
    @ColumnAliasName(aliasName ="geometry")
	private Geometry geom;

    @Column(name = "eez", length = 200)
    @ColumnAliasName(aliasName ="eez")
	private String eez;

    @Column(name = "country", length = 100)
    @ColumnAliasName(aliasName ="country")
	private String country;

    @Column(name = "sovereign", length = 100)
    @ColumnAliasName(aliasName ="sovereign")
	private String sovereign;
    
    @Column(name = "remarks", length = 150)
    @ColumnAliasName(aliasName ="remarks")
	private String remarks;

    @Column(name = "sov_id")
    @ColumnAliasName(aliasName ="sovId")
	private Integer sovId;

    @Column(name = "eez_id")
    @ColumnAliasName(aliasName ="eezId")
	private Integer eezId;

    @Column(name = "iso_3digit", length = 5)
    @ColumnAliasName(aliasName ="iso3digit")
	private String iso3digit;

    @Column(name = "mrgid")
    @ColumnAliasName(aliasName ="mrgid")
	private BigDecimal mrgid;

    @Column(name = "date_chang", length = 50)
    @ColumnAliasName(aliasName ="dateChang")
	private String dateChang;

    @Column(name = "area_m2")
    @ColumnAliasName(aliasName ="areaM2")
	private Double areaM2;

    @Column(name = "longitude")
    @ColumnAliasName(aliasName ="longitude")
	private Double longitude;

    @Column(name = "latitude")
    @ColumnAliasName(aliasName ="latitude")
	private Double latitude;

    @Column(name = "mrgid_eez")
    @ColumnAliasName(aliasName ="mrgidEez")
	private Integer mrgidEez;

	public EezEntity() {
	}

	public int getGid() {
		return this.gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public Geometry getGeom() {
		return this.geom;
	}

	public void setGeom(Geometry geom) {
		this.geom = geom;
	}

	public String getEez() {
		return this.eez;
	}

	public void setEez(String eez) {
		this.eez = eez;
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

	public Integer getEezId() {
		return this.eezId;
	}

	public void setEezId(Integer eezId) {
		this.eezId = eezId;
	}

	public String getIso3digit() {
		return this.iso3digit;
	}

	public void setIso3digit(String iso3digit) {
		this.iso3digit = iso3digit;
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
