package eu.europa.ec.fisheries.uvms.spatial.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

@Entity
@Table(name = "user_areas", schema = "spatial")
public class UserAreasEntity implements Serializable {
	
	private static final long serialVersionUID = 6797853213499502873L;

	@Id
	@Column(name = "gid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long gid;
	
	@Column(name = "user_id", nullable = false)
	private long userId;
	
	@Column(name = "scope_id")
	private Long scopeId;
	
	@Column(name = "name", nullable = false, length = 255)
	private String name;
	
	@Lob
	@Column(name = "area_desc")
	private String areaDesc;
	
    @Basic
    @Column(name = "geom", nullable = false)
    @Type(type = "org.hibernate.spatial.GeometryType")
	private Geometry geom;
    
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = false)
	private Date createdOn;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userAreas", cascade = CascadeType.ALL)
	private Set<AreaStatusEntity> areaStatuses;

	public UserAreasEntity() {
	}

	public long getGid() {
		return this.gid;
	}

	public void setGid(long gid) {
		this.gid = gid;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Long getScopeId() {
		return this.scopeId;
	}

	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAreaDesc() {
		return this.areaDesc;
	}

	public void setAreaDesc(String areaDesc) {
		this.areaDesc = areaDesc;
	}

	public Geometry getGeom() {
		return this.geom;
	}

	public void setGeom(Geometry geom) {
		this.geom = geom;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Set<AreaStatusEntity> getAreaStatuses() {
		return this.areaStatuses;
	}

	public void setAreaStatuses(Set<AreaStatusEntity> areaStatuses) {
		this.areaStatuses = areaStatuses;
	}

}
