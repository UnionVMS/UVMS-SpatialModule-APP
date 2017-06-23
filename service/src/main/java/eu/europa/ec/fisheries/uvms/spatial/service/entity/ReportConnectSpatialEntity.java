/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.entity;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.CoordinatesFormat;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScaleBarUnits;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


@Entity
@Table(name = "report_connect_spatial")
@NamedQueries({
		@NamedQuery(name = ReportConnectSpatialEntity.FIND_MAP_PROJ_BY_ID,
				query = "SELECT projection.srsCode AS epsgCode, projection.units AS units, projection.isWorld AS global, projection.extent as extent, projection.axis as axis , projection.projDef as projDef, projection.worldExtent as worldExtent " +
						"FROM ReportConnectSpatialEntity rcs INNER JOIN rcs.projectionByMapProjId AS projection " +
						"WHERE rcs.reportId = :reportId"),
		@NamedQuery(name = ReportConnectSpatialEntity.FIND_BY_REPORT_ID,
				query = "FROM ReportConnectSpatialEntity WHERE reportId = :reportId"),
		@NamedQuery(name = ReportConnectSpatialEntity.FIND_BY_ID,
				query = "FROM ReportConnectSpatialEntity WHERE reportId = :reportId AND id = :id"),
		@NamedQuery(name = ReportConnectSpatialEntity.FIND_BY_REPORT_CONNECT_ID,
				query = "FROM ReportConnectSpatialEntity WHERE id = :id"),
        @NamedQuery(name = ReportConnectSpatialEntity.DELETE_BY_ID_LIST,
                query = "DELETE FROM ReportConnectSpatialEntity WHERE id IN :idList")
})
@EqualsAndHashCode(exclude = {"id", "reportConnectServiceAreas"})
@Data
@ToString(exclude = {"reportConnectServiceAreas"})
public class ReportConnectSpatialEntity implements Serializable {

    public static final String FIND_BY_ID = "reportConnectSpatialEntity.findById";
    public static final String FIND_MAP_PROJ_BY_ID = "reportConnectSpatialEntity.findMapProjectionById";
    public static final String DELETE_BY_ID_LIST = "reportConnectSpatialEntity.deleteByIdList";
	public static final String FIND_BY_REPORT_CONNECT_ID = "reportConnectSpatialEntity.findByReportConnectId";
    public static final String FIND_BY_REPORT_ID = "ReportLayerConfig.findByReportId";

	
    @Id
	@Column(name = "id")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="report_connect_spatial_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
	private Long id;
	
	
	
	@ManyToOne
	@JoinColumn(name = "map_proj_id")
	private ProjectionEntity projectionByMapProjId;
	
	@ManyToOne
	@JoinColumn(name = "display_proj_id")
	private ProjectionEntity projectionByDisplayProjId;
	
	@Column(name = "report_id", nullable = false)
	private Long reportId;
	
	@Column(name = "map_center")
	private String mapCenter;
	
	@Column(name = "map_zoom")
	private Integer mapZoom;
	
	@Column(name = "display_format")
    @Enumerated(EnumType.STRING)
	private CoordinatesFormat displayFormatType;
	
	@Column(name = "measurement_units")
	private String measurementUnits;
	
	@Column(name = "scalebar_units")
    @Enumerated(EnumType.STRING)
    private ScaleBarUnits scaleBarType;

	@Column(columnDefinition = "text", name = "styles_settings")
	private String styleSettings;

	@Column(columnDefinition = "text", name = "visibility_settings")
	private String visibilitySettings;

	@Column(columnDefinition = "text", name = "reference_data")
	private String referenceData;

	@Column(name = "app_version", nullable = false)
	private String appVersion;
	
	@OneToMany(orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "reportConnectSpatial", cascade = CascadeType.ALL)
	private Set<ReportConnectServiceAreasEntity> reportConnectServiceAreas;

    public ReportConnectSpatialEntity() {
        // why JPA why
    }

    @Builder
    public ReportConnectSpatialEntity(ProjectionEntity projectionByMapProjId,
                                      ProjectionEntity projectionByDisplayProjId,
                                      long reportId, String mapCenter, int mapZoom, String styleSettings,
                                      CoordinatesFormat displayFormatType, String measurementUnits,
                                      ScaleBarUnits scaleBarType, String visibilitySettings, String appVersion,
                                      Set<ReportConnectServiceAreasEntity> reportConnectServiceAreases) {
        this.projectionByMapProjId = projectionByMapProjId;
        this.projectionByDisplayProjId = projectionByDisplayProjId;
        this.reportId = reportId;
        this.mapCenter = mapCenter;
        this.mapZoom = mapZoom;
        this.displayFormatType = displayFormatType;
        this.measurementUnits = measurementUnits;
        this.scaleBarType = scaleBarType;
        this.styleSettings = styleSettings;
        this.appVersion = appVersion;
        this.reportConnectServiceAreas = reportConnectServiceAreases;
		this.visibilitySettings = visibilitySettings;
    }
}