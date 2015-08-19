package eu.europa.ec.fisheries.uvms.spatial.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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

@Entity
@Table(name = "area_group", schema = "spatial")
public class AreaGroupEntity implements Serializable {
	
	private static final long serialVersionUID = 6797853213499502856L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "user_id", nullable = false)
	private long userId;
	
	@Column(name = "scope_id", nullable = false)
	private long scopeId;
	
	@Lob
	@Column(name = "bookmark_definition", nullable = false)
	private String bookmarkDefinition;
	
	@Lob
	@Column(name = "description")
	private String description;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = false)
	private Date createdOn;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "areaGroup", cascade = CascadeType.ALL)
	private Set<ReportConnectServiceAreasEntity> reportConnectServiceAreases;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "areaGroup", cascade = CascadeType.ALL)
	private Set<AreaConnectGroupEntity> areaConnectGroups;

	public AreaGroupEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getScopeId() {
		return this.scopeId;
	}

	public void setScopeId(long scopeId) {
		this.scopeId = scopeId;
	}

	public String getBookmarkDefinition() {
		return this.bookmarkDefinition;
	}

	public void setBookmarkDefinition(String bookmarkDefinition) {
		this.bookmarkDefinition = bookmarkDefinition;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Set<ReportConnectServiceAreasEntity> getReportConnectServiceAreases() {
		return this.reportConnectServiceAreases;
	}

	public void setReportConnectServiceAreases(Set<ReportConnectServiceAreasEntity> reportConnectServiceAreases) {
		this.reportConnectServiceAreases = reportConnectServiceAreases;
	}

	public Set<AreaConnectGroupEntity> getAreaConnectGroups() {
		return this.areaConnectGroups;
	}

	public void setAreaConnectGroups(Set<AreaConnectGroupEntity> areaConnectGroups) {
		this.areaConnectGroups = areaConnectGroups;
	}

}
