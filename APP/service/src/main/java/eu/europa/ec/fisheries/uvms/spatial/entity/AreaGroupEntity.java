package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "area_group", schema = "spatial")
@NamedQueries({
		@NamedQuery(name = QueryNameConstants.FIND_ALL_AREA_GROUP_BY_NAME,
				query = "FROM AreaGroupEntity areaGroup WHERE areaGroup.userId = :userId"),
		@NamedQuery(name = QueryNameConstants.FIND_AREA_GROUP_BY_ID,
				query = "FROM AreaGroupEntity areaGroup WHERE areaGroup.id = :id")
})
public class AreaGroupEntity implements Serializable {
	
	private static final long serialVersionUID = 6797853213499502856L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id", nullable = false)
	private String userId;
	
	@Column(name = "scope_id", nullable = false)
	private String scopeId;

	@Column(name = "group_name", nullable = false)
	private String groupName;

	@Column(columnDefinition = "text", name = "bookmark_definition")
	private String bookmarkDefinition;

	@Column(columnDefinition = "text", name = "description")
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getScopeId() {
		return scopeId;
	}

	public void setScopeId(String scopeId) {
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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
