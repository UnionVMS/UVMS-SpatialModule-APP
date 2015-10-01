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
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;

@Entity
@SqlResultSetMappings({
	@SqlResultSetMapping(name = "implicit.rfmo", entities = @EntityResult(entityClass = RfmoEntity.class))
})
@NamedNativeQuery(
		name = QueryNameConstants.RFMO_BY_COORDINATE, 
		query = "select * from rfmo where st_intersects(geom, st_geomfromtext(CAST(:wktPoint as text), :crs))", resultSetMapping = "implicit.rfmo")

@Table(name = "rfmo", schema = "spatial")
public class RfmoEntity implements Serializable {
	
	private static final long serialVersionUID = 6797853213499502870L;
	
	@Id
	@Column(name = "gid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnAliasName(aliasName="gid")
	private int gid;
	
    @Basic
    @Column(name = "geom")
    @Type(type = "org.hibernate.spatial.GeometryType")
    @ColumnAliasName(aliasName="geometry")
	private Geometry geom;
    
    @Column(name = "code", length = 10)
    @ColumnAliasName(aliasName="code")
	private String code;
    
    @Column(name = "name", length = 125)
    @ColumnAliasName(aliasName="name")
	private String name;
    
    @Column(name = "tuna", length = 10)
    @ColumnAliasName(aliasName="tuna")
	private String tuna;

	public RfmoEntity() {
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

	public String getTuna() {
		return this.tuna;
	}

	public void setTuna(String tuna) {
		this.tuna = tuna;
	}

}
