package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.spatial.entity.converter.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.LayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.StylesDto;

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

@Entity
@Table(name = "report_connect_service_areas", schema = "spatial")
@NamedQueries(
		@NamedQuery(name = QueryNameConstants.FIND_REPORT_SERVICE_AREAS,
				query = "SELECT rcsa FROM ReportConnectServiceAreasEntity rcsa WHERE rcsa.reportConnectSpatial.reportId = :reportId")
)
public class ReportConnectServiceAreasEntity implements Serializable, Comparable<ReportConnectServiceAreasEntity> {
	
	private static final long serialVersionUID = 6797853213499502868L;

	private static final String GEOSERVER = "geoserver";

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "area_group_id")
	private AreaGroupEntity areaGroup;
	
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

	@Convert(converter = CharBooleanConverter.class)
	@Column(name = "is_background", length = 1)
	private Boolean isBackground = false;
	
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

	public Boolean getIsBackground() {
		return this.isBackground;
	}

	public void setIsBackground(Boolean isBackground) {
		this.isBackground = isBackground;
	}

	public Set<ReportLayerConfigEntity> getReportLayerConfigs() {
		return this.reportLayerConfigs;
	}

	public void setReportLayerConfigs(Set<ReportLayerConfigEntity> reportLayerConfigs) {
		this.reportLayerConfigs = reportLayerConfigs;
	}

	@Override
	public int compareTo(ReportConnectServiceAreasEntity reportConnectServiceArea) {
		return Integer.compare(this.getLayerOrder(), reportConnectServiceArea.getLayerOrder());
	}

	public LayerDto convertToServiceLayer(String geoServerUrl) {
		LayerDto layerDto = new LayerDto();
		layerDto.setType(serviceLayer.getProviderFormat().getServiceType());
		layerDto.setGroupType(serviceLayer.getAreaType().getAreaGroupType());
		layerDto.setTitle(serviceLayer.getName());
		layerDto.setIsBaseLayer(getIsBackground());
		layerDto.setShortCopyright(serviceLayer.getShortCopyright());
		layerDto.setLongCopyright(serviceLayer.getLongCopyright());
		if (!(serviceLayer.getName().equalsIgnoreCase("OSM") || serviceLayer.getName().equalsIgnoreCase("OSEA"))) {
			layerDto.setUrl(geoServerUrl.concat(serviceLayer.getProviderFormat().getServiceType().toLowerCase()));
		}
		layerDto.setServerType(serviceLayer.getIsInternal() ? GEOSERVER : null);
		layerDto.setLayerGeoName(serviceLayer.getGeoName());
		setStyle(layerDto);
		return layerDto;
	}

	private void setStyle(LayerDto layerDto) {
		if(!serviceLayer.isStyleEmpty()) {
			layerDto.setStyles(new StylesDto(serviceLayer.getStyleGeom(), serviceLayer.getStyleLabel(), serviceLayer.getStyleLabelGeom()));
		}
	}
}
