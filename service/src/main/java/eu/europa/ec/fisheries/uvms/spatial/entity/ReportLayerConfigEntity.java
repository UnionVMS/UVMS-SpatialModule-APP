package eu.europa.ec.fisheries.uvms.spatial.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "report_layer_config", schema = "spatial")
public class ReportLayerConfigEntity implements java.io.Serializable {

	private static final long serialVersionUID = 6797853213499502857L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "area_connect_group_id", nullable = false)
	private AreaConnectGroupEntity areaConnectGroup;
	
	@ManyToOne
	@JoinColumn(name = "report_connect_service_areas_id")
	private ReportConnectServiceAreasEntity reportConnectServiceAreas;
	
	@Column(name = "layer_order", nullable = false)
	private long layerOrder;
	
	@Lob
	@Column(name = "sld")
	private String sld;

	public ReportLayerConfigEntity() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AreaConnectGroupEntity getAreaConnectGroup() {
		return this.areaConnectGroup;
	}

	public void setAreaConnectGroup(AreaConnectGroupEntity areaConnectGroup) {
		this.areaConnectGroup = areaConnectGroup;
	}

	public ReportConnectServiceAreasEntity getReportConnectServiceAreas() {
		return this.reportConnectServiceAreas;
	}

	public void setReportConnectServiceAreas(ReportConnectServiceAreasEntity reportConnectServiceAreas) {
		this.reportConnectServiceAreas = reportConnectServiceAreas;
	}

	public long getLayerOrder() {
		return this.layerOrder;
	}

	public void setLayerOrder(long layerOrder) {
		this.layerOrder = layerOrder;
	}

	public String getSld() {
		return this.sld;
	}

	public void setSld(String sld) {
		this.sld = sld;
	}

}
