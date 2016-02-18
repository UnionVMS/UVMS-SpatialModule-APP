package eu.europa.ec.fisheries.uvms.spatial.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import org.hibernate.annotations.Where;

@Entity
@SqlResultSetMappings({
	@SqlResultSetMapping(name = "implicit.country", entities = @EntityResult(entityClass = CountriesEntity.class))
})
@NamedNativeQuery(
		name = QueryNameConstants.COUNTRY_BY_COORDINATE, 
		query = "select * from spatial.countries where st_intersects(geom, st_geomfromtext(CAST(:wktPoint as text), :crs)) and enabled = 'Y'", resultSetMapping = "implicit.country")
@NamedQueries({
		@NamedQuery(name = QueryNameConstants.FIND_ALL_COUNTRY_DESC,
		query = "SELECT country.code AS code, country.name AS name FROM CountriesEntity country WHERE country.code IN (SELECT DISTINCT c.code FROM CountriesEntity c)")
})
@Where(clause = "enabled = 'Y'")
@Table(name = "countries", schema = "spatial")
@EqualsAndHashCode(exclude = "id")
public class CountriesEntity implements Serializable { // TODO rename to CountryEntity

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
    @Basic
    @Column(name = "geom")
    @Type(type = "org.hibernate.spatial.GeometryType")
    @ColumnAliasName(aliasName="geom")
	private Geometry geom;
    
    @Column(name = "sovereignt", length = 32)
    @ColumnAliasName(aliasName="sovereignt")
	private String sovereignt;
    
    @Column(name = "sov_a3", length = 3)
    @ColumnAliasName(aliasName="sovA3")
	private String sovA3;
    
    @Column(name = "type", length = 17)
    @ColumnAliasName(aliasName="type")
	private String type;
    
    @Column(name = "admin", length = 40)
    @ColumnAliasName(aliasName="admin")
	private String admin;
    
    @Column(name = "code", length = 3)
    @ColumnAliasName(aliasName="code")
	private String code;
    
    @Column(name = "name", length = 36)
    @ColumnAliasName(aliasName="name")
	private String name;
    
    @Column(name = "name_long", length = 40)
    @ColumnAliasName(aliasName="nameLong")
	private String nameLong;
    
    @Column(name = "pop_est", precision = 17, scale = 17)
    @ColumnAliasName(aliasName="popEst")
	private Double popEst;
    
    @Column(name = "gdp_md_est", precision = 17, scale = 17)
    @ColumnAliasName(aliasName="gdpMdEst")
	private Double gdpMdEst;
    
    @Column(name = "income_grp", length = 23)
    @ColumnAliasName(aliasName="incomeGrp")
	private String incomeGrp;
    
    @Column(name = "continent", length = 23)
    @ColumnAliasName(aliasName="continent")
	private String continent;
    
    @Column(name = "region_un", length = 23)
    @ColumnAliasName(aliasName="regionUn")
	private String regionUn;
    
    @Column(name = "subregion", length = 25)
    @ColumnAliasName(aliasName="subregion")
	private String subregion;
    
    @Column(name = "region_wb", length = 26)
    @ColumnAliasName(aliasName="regionWb")
	private String regionWb;

	public CountriesEntity() {
	}

    @Builder
    public CountriesEntity(Geometry geom, String sovereignt, String sovA3, String type, String admin, String code, String name, String nameLong, Double popEst, Double gdpMdEst, String incomeGrp, String continent, String regionUn, String subregion, String regionWb) {
        this.geom = geom;
        this.sovereignt = sovereignt;
        this.sovA3 = sovA3;
        this.type = type;
        this.admin = admin;
        this.code = code;
        this.name = name;
        this.nameLong = nameLong;
        this.popEst = popEst;
        this.gdpMdEst = gdpMdEst;
        this.incomeGrp = incomeGrp;
        this.continent = continent;
        this.regionUn = regionUn;
        this.subregion = subregion;
        this.regionWb = regionWb;
    }

    public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Geometry getGeom() {
		return this.geom;
	}

	public void setGeom(Geometry geom) {
		this.geom = geom;
	}

	public String getSovereignt() {
		return this.sovereignt;
	}

	public void setSovereignt(String sovereignt) {
		this.sovereignt = sovereignt;
	}

	public String getSovA3() {
		return this.sovA3;
	}

	public void setSovA3(String sovA3) {
		this.sovA3 = sovA3;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAdmin() {
		return this.admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameLong() {
		return this.nameLong;
	}

	public void setNameLong(String nameLong) {
		this.nameLong = nameLong;
	}

	public Double getPopEst() {
		return this.popEst;
	}

	public void setPopEst(Double popEst) {
		this.popEst = popEst;
	}

	public Double getGdpMdEst() {
		return this.gdpMdEst;
	}

	public void setGdpMdEst(Double gdpMdEst) {
		this.gdpMdEst = gdpMdEst;
	}

	public String getIncomeGrp() {
		return this.incomeGrp;
	}

	public void setIncomeGrp(String incomeGrp) {
		this.incomeGrp = incomeGrp;
	}

	public String getContinent() {
		return this.continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public String getRegionUn() {
		return this.regionUn;
	}

	public void setRegionUn(String regionUn) {
		this.regionUn = regionUn;
	}

	public String getSubregion() {
		return this.subregion;
	}

	public void setSubregion(String subregion) {
		this.subregion = subregion;
	}

	public String getRegionWb() {
		return this.regionWb;
	}

	public void setRegionWb(String regionWb) {
		this.regionWb = regionWb;
	}

}
