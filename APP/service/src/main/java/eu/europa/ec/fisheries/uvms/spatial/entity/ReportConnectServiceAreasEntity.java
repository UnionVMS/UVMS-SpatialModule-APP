package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.LayerDto;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "report_connect_service_areas", schema = "spatial")
@NamedQueries({
		@NamedQuery(name = ReportConnectServiceAreasEntity.FIND_REPORT_SERVICE_AREAS,
				query = "SELECT rcsa FROM ReportConnectServiceAreasEntity rcsa WHERE rcsa.reportConnectSpatial.reportId = :reportId"),
		@NamedQuery(name = ReportConnectServiceAreasEntity.DELETE_BY_REPORT_CONNECT_SPATIAL_ID,
				query = "DELETE FROM ReportConnectServiceAreasEntity rcsa WHERE rcsa.id = :id")
})
public class ReportConnectServiceAreasEntity implements Serializable, Comparable<ReportConnectServiceAreasEntity> {

    public static final String DELETE_BY_REPORT_CONNECT_SPATIAL_ID = "ReportLayerConfig.deleteByReportConnectSpatialId";
    public static final String FIND_REPORT_SERVICE_AREAS = "ReportLayerConfig.findReportConnectServiceAreas";

    @Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "report_connect_spatial_id", nullable = false)
	private ReportConnectSpatialEntity reportConnectSpatial;
	
	@ManyToOne
	@JoinColumn(name = "service_layer_id")
	private ServiceLayerEntity serviceLayer;

	@Column(columnDefinition = "text", name = "sql_filter")
	private String sqlFilter;
	
	@Column(name = "layer_order", nullable = false)
	private int layerOrder;

	@Column(name = "layer_type", nullable = false)
	private String layerType;

	@Column(name = "area_type")
	private String areaType;

	public ReportConnectServiceAreasEntity() {
	}
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getLayerType() {
		return layerType;
	}

	public void setLayerType(String layerType) {
		this.layerType = layerType;
	}

	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	@Override
	public int compareTo(ReportConnectServiceAreasEntity reportConnectServiceArea) {
		return Integer.compare(this.getLayerOrder(), reportConnectServiceArea.getLayerOrder());
	}

	public LayerDto convertToServiceLayer(String geoServerUrl, String bingApiKey) {
		return serviceLayer.convertToServiceLayer(geoServerUrl, bingApiKey, false); // TODO Fix is background check
	}
}
