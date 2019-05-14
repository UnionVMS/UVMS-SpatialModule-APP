/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "report_connect_service_area")
@NamedQueries({
		@NamedQuery(name = ReportConnectServiceAreasEntity.FIND_REPORT_SERVICE_AREAS,
				query = "SELECT rcsa FROM ReportConnectServiceAreasEntity rcsa WHERE rcsa.reportConnectSpatial.reportId = :reportId"),
		@NamedQuery(name = ReportConnectServiceAreasEntity.DELETE_BY_REPORT_CONNECT_SPATIAL_ID,
				query = "DELETE FROM ReportConnectServiceAreasEntity rcsa WHERE rcsa.id = :id")
})
public class ReportConnectServiceAreasEntity implements Comparable<ReportConnectServiceAreasEntity> {

    public static final String DELETE_BY_REPORT_CONNECT_SPATIAL_ID = "ReportLayerConfig.deleteByReportConnectSpatialId";
    public static final String FIND_REPORT_SERVICE_AREAS = "ReportLayerConfig.findReportConnectServiceAreas";

	@Id
	@Column(name = "id")
    @SequenceGenerator(name = "SEQ_REP_CONN_GEN", sequenceName = "report_conn_serv_area_seq", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_REP_CONN_GEN")
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


	@Override
	public int compareTo(ReportConnectServiceAreasEntity reportConnectServiceArea) {
		return Integer.compare(this.getLayerOrder(), reportConnectServiceArea.getLayerOrder());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ReportConnectSpatialEntity getReportConnectSpatial() {
		return reportConnectSpatial;
	}

	public void setReportConnectSpatial(ReportConnectSpatialEntity reportConnectSpatial) {
		this.reportConnectSpatial = reportConnectSpatial;
	}

	public ServiceLayerEntity getServiceLayer() {
		return serviceLayer;
	}

	public void setServiceLayer(ServiceLayerEntity serviceLayer) {
		this.serviceLayer = serviceLayer;
	}

	public String getSqlFilter() {
		return sqlFilter;
	}

	public void setSqlFilter(String sqlFilter) {
		this.sqlFilter = sqlFilter;
	}

	public int getLayerOrder() {
		return layerOrder;
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ReportConnectServiceAreasEntity that = (ReportConnectServiceAreasEntity) o;
		return layerOrder == that.layerOrder &&
				Objects.equals(id, that.id) &&
				Objects.equals(reportConnectSpatial, that.reportConnectSpatial) &&
				Objects.equals(serviceLayer, that.serviceLayer) &&
				Objects.equals(sqlFilter, that.sqlFilter) &&
				Objects.equals(layerType, that.layerType) &&
				Objects.equals(areaType, that.areaType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, reportConnectSpatial, serviceLayer, sqlFilter, layerOrder, layerType, areaType);
	}
}