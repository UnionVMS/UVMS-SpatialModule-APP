package eu.europa.ec.fisheries.uvms.spatial.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "report_connect_service_areas", schema = "spatial")
public class ReportConnectServiceAreasEntity implements Serializable {
	
	private static final long serialVersionUID = 6797853213499502868L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "area_group_id")
	private AreaGroupEntity areaGroup;
	
	@ManyToOne
	@JoinColumn(name = "report_id", nullable = false)
	private ReportConnectSpatialEntity reportConnectSpatial;
	
	@ManyToOne
	@JoinColumn(name = "service_layer_id")
	private ServiceLayerEntity serviceLayer;
	
	@Lob
	@Column(name = "sql_filter")
	private String sqlFilter;
	
	@Column(name = "layer_order", nullable = false)
	private int layerOrder = 0;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "reportConnectServiceAreas", cascade = CascadeType.ALL)
	private Set<ReportLayerConfigEntity> reportLayerConfigs;

	public ReportConnectServiceAreasEntity() {
	}
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AreaGroupEntity getAreaGroup() {
		return this.areaGroup;
	}

	public void setAreaGroup(AreaGroupEntity areaGroup) {
		this.areaGroup = areaGroup;
	}

	public ReportConnectSpatialEntity getReportConnectSpatial() {
		return this.reportConnectSpatial;
	}

	public void setReportConnectSpatial(ReportConnectSpatialEntity reportConnectSpatial) {
		this.reportConnectSpatial = reportConnectSpatial;
	}

	public ServiceLayerEntity getServiceLayer() {
		return this.serviceLayer;
	}

	public void setServiceLayer(ServiceLayerEntity serviceLayer) {
		this.serviceLayer = serviceLayer;
	}

	public String getSqlFilter() {
		return this.sqlFilter;
	}

	public void setSqlFilter(String sqlFilter) {
		this.sqlFilter = sqlFilter;
	}

	public int getLayerOrder() {
		return this.layerOrder;
	}

	public void setLayerOrder(int layerOrder) {
		this.layerOrder = layerOrder;
	}

	public Set<ReportLayerConfigEntity> getReportLayerConfigs() {
		return this.reportLayerConfigs;
	}

	public void setReportLayerConfigs(Set<ReportLayerConfigEntity> reportLayerConfigs) {
		this.reportLayerConfigs = reportLayerConfigs;
	}

}
