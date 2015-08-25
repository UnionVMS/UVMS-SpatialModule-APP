package eu.europa.ec.fisheries.uvms.spatial.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import eu.europa.ec.fisheries.uvms.spatial.entity.converter.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;

@Entity
@Table(name = "area_types", schema = "spatial", uniqueConstraints = @UniqueConstraint(columnNames = "type_name"))
@NamedQueries({
    @NamedQuery(name = QueryNameConstants.FIND_ALL_AREAS, query = "SELECT a.typeName FROM AreaTypesEntity a"),
    @NamedQuery(name = QueryNameConstants.FIND_SYSTEM_AREAS, query = "SELECT area FROM AreaTypesEntity area WHERE area.isSystemArea = 'Y'"),
})

public class AreaTypesEntity implements Serializable {
	
	private static final long serialVersionUID = 6797853213499502859L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_layer_id", nullable = false)
	private ServiceLayerEntity serviceLayer;
	
	@Column(name = "type_name", unique = true, nullable = false, length=255)
	private String typeName;
	
	@Column(name = "area_type_desc", length=255)
	private String areaTypeDesc;
	
	@Column(name = "area_db_table", nullable = false, length=255)
	private String areaDbTable;
	
	@Convert(converter = CharBooleanConverter.class)
	@Column(name = "is_system_area", nullable = false, length = 1)
	private Boolean isSystemArea = false;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "areaTypes", cascade = CascadeType.ALL)
	private Set<AreaConnectGroupEntity> areaConnectGroups;

	public AreaTypesEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ServiceLayerEntity getServiceLayer() {
		return this.serviceLayer;
	}

	public void setServiceLayer(ServiceLayerEntity serviceLayer) {
		this.serviceLayer = serviceLayer;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getAreaTypeDesc() {
		return this.areaTypeDesc;
	}

	public void setAreaTypeDesc(String areaTypeDesc) {
		this.areaTypeDesc = areaTypeDesc;
	}

	public String getAreaDbTable() {
		return this.areaDbTable;
	}

	public void setAreaDbTable(String areaDbTable) {
		this.areaDbTable = areaDbTable;
	}

	public Boolean getIsSystemArea() {
		return this.isSystemArea;
	}

	public void setIsSystemArea(Boolean isSystemArea) {
		this.isSystemArea = isSystemArea;
	}

	public Set<AreaConnectGroupEntity> getAreaConnectGroups() {
		return this.areaConnectGroups;
	}

	public void setAreaConnectGroups(Set<AreaConnectGroupEntity> areaConnectGroups) {
		this.areaConnectGroups = areaConnectGroups;
	}

}
