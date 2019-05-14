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

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "report_connect_spatial")
@NamedQueries({
		@NamedQuery(name = ReportConnectSpatialEntity.FIND_MAP_PROJ_BY_ID,
				query = "SELECT projection.srsCode AS epsgCode, projection.units AS units, projection.world AS global, projection.extent as extent, projection.axis as axis , projection.projDef as projDef, projection.worldExtent as worldExtent " +
						"FROM ReportConnectSpatialEntity2 rcs INNER JOIN rcs.projectionByMapProjId AS projection " +
						"WHERE rcs.reportId = :reportId"),
		@NamedQuery(name = ReportConnectSpatialEntity.FIND_BY_REPORT_ID,
				query = "FROM ReportConnectSpatialEntity2 WHERE reportId = :reportId"),
		@NamedQuery(name = ReportConnectSpatialEntity.FIND_BY_ID,
				query = "FROM ReportConnectSpatialEntity2 WHERE reportId = :reportId AND id = :id"),
		@NamedQuery(name = ReportConnectSpatialEntity.FIND_BY_REPORT_CONNECT_ID,
				query = "FROM ReportConnectSpatialEntity2 WHERE id = :id"),
        @NamedQuery(name = ReportConnectSpatialEntity.DELETE_BY_ID_LIST,
                query = "DELETE FROM ReportConnectSpatialEntity2 WHERE id IN :idList")
})
public class ReportConnectSpatialEntity implements Serializable {

    public static final String FIND_BY_ID = "reportConnectSpatialEntity2.findById";
    public static final String FIND_MAP_PROJ_BY_ID = "reportConnectSpatialEntity2.findMapProjectionById";
    public static final String DELETE_BY_ID_LIST = "reportConnectSpatialEntity2.deleteByIdList";
	public static final String FIND_BY_REPORT_CONNECT_ID = "reportConnectSpatialEntity2.findByReportConnectId";
    public static final String FIND_BY_REPORT_ID = "ReportLayerConfig2.findByReportId";

	
    @Id
	@Column(name = "id")
	@SequenceGenerator(name="SEQ_REPCONN2_GEN", sequenceName="report_connect_spatial_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_REPCONN2_GEN")
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProjectionEntity getProjectionByMapProjId() {
		return projectionByMapProjId;
	}

	public void setProjectionByMapProjId(ProjectionEntity projectionByMapProjId) {
		this.projectionByMapProjId = projectionByMapProjId;
	}

	public ProjectionEntity getProjectionByDisplayProjId() {
		return projectionByDisplayProjId;
	}

	public void setProjectionByDisplayProjId(ProjectionEntity projectionByDisplayProjId) {
		this.projectionByDisplayProjId = projectionByDisplayProjId;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getMapCenter() {
		return mapCenter;
	}

	public void setMapCenter(String mapCenter) {
		this.mapCenter = mapCenter;
	}

	public Integer getMapZoom() {
		return mapZoom;
	}

	public void setMapZoom(Integer mapZoom) {
		this.mapZoom = mapZoom;
	}

	public CoordinatesFormat getDisplayFormatType() {
		return displayFormatType;
	}

	public void setDisplayFormatType(CoordinatesFormat displayFormatType) {
		this.displayFormatType = displayFormatType;
	}

	public String getMeasurementUnits() {
		return measurementUnits;
	}

	public void setMeasurementUnits(String measurementUnits) {
		this.measurementUnits = measurementUnits;
	}

	public ScaleBarUnits getScaleBarType() {
		return scaleBarType;
	}

	public void setScaleBarType(ScaleBarUnits scaleBarType) {
		this.scaleBarType = scaleBarType;
	}

	public String getStyleSettings() {
		return styleSettings;
	}

	public void setStyleSettings(String styleSettings) {
		this.styleSettings = styleSettings;
	}

	public String getVisibilitySettings() {
		return visibilitySettings;
	}

	public void setVisibilitySettings(String visibilitySettings) {
		this.visibilitySettings = visibilitySettings;
	}

	public String getReferenceData() {
		return referenceData;
	}

	public void setReferenceData(String referenceData) {
		this.referenceData = referenceData;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public Set<ReportConnectServiceAreasEntity> getReportConnectServiceAreas() {
		return reportConnectServiceAreas;
	}

	public void setReportConnectServiceAreas(Set<ReportConnectServiceAreasEntity> reportConnectServiceAreas) {
		this.reportConnectServiceAreas = reportConnectServiceAreas;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ReportConnectSpatialEntity that = (ReportConnectSpatialEntity) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(projectionByMapProjId, that.projectionByMapProjId) &&
				Objects.equals(projectionByDisplayProjId, that.projectionByDisplayProjId) &&
				Objects.equals(reportId, that.reportId) &&
				Objects.equals(mapCenter, that.mapCenter) &&
				Objects.equals(mapZoom, that.mapZoom) &&
				displayFormatType == that.displayFormatType &&
				Objects.equals(measurementUnits, that.measurementUnits) &&
				scaleBarType == that.scaleBarType &&
				Objects.equals(styleSettings, that.styleSettings) &&
				Objects.equals(visibilitySettings, that.visibilitySettings) &&
				Objects.equals(referenceData, that.referenceData) &&
				Objects.equals(appVersion, that.appVersion) &&
				Objects.equals(reportConnectServiceAreas, that.reportConnectServiceAreas);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, projectionByMapProjId, projectionByDisplayProjId, reportId, mapCenter, mapZoom, displayFormatType, measurementUnits, scaleBarType, styleSettings, visibilitySettings, referenceData, appVersion, reportConnectServiceAreas);
	}
}