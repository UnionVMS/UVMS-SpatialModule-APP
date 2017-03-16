/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.entity;

import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


@Entity
@Table(name = "report_connect_service_area")
@NamedQueries({
		@NamedQuery(name = ReportConnectServiceAreasEntity.FIND_REPORT_SERVICE_AREAS,
				query = "SELECT rcsa FROM ReportConnectServiceAreasEntity rcsa WHERE rcsa.reportConnectSpatial.reportId = :reportId"),
		@NamedQuery(name = ReportConnectServiceAreasEntity.DELETE_BY_REPORT_CONNECT_SPATIAL_ID,
				query = "DELETE FROM ReportConnectServiceAreasEntity rcsa WHERE rcsa.id = :id")
})
@EqualsAndHashCode(callSuper = false, exclude = {"reportConnectSpatial", "serviceLayer" })
@Data
@ToString(exclude = {"reportConnectSpatial", "serviceLayer" })
public class ReportConnectServiceAreasEntity extends BaseEntity implements Comparable<ReportConnectServiceAreasEntity> {

    public static final String DELETE_BY_REPORT_CONNECT_SPATIAL_ID = "ReportLayerConfig.deleteByReportConnectSpatialId";
    public static final String FIND_REPORT_SERVICE_AREAS = "ReportLayerConfig.findReportConnectServiceAreas";

	@Id
	@Column(name = "id")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="report_connect_service_area_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
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
        // why JPA why
    }

	@Override
	public int compareTo(ReportConnectServiceAreasEntity reportConnectServiceArea) {
		return Integer.compare(this.getLayerOrder(), reportConnectServiceArea.getLayerOrder());
	}
}