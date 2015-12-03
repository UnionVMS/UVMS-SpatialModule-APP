package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.CoordinatesFormat;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScaleBarUnits;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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

@Entity
@Table(name = "report_connect_spatial", schema = "spatial")
@NamedQueries({
		@NamedQuery(name = QueryNameConstants.FIND_MAP_PROJ_BY_ID,
				query = "SELECT projection.srsCode AS epsgCode, projection.units AS units, projection.isWorld AS global, projection.extent as extent, projection.axis as axis " +
						"FROM ReportConnectSpatialEntity rcs INNER JOIN rcs.projectionByMapProjId AS projection " +
						"WHERE rcs.reportId = :reportId"),
		@NamedQuery(name = QueryNameConstants.FIND_DISPLAY_PROJ_BY_ID,
				query = "SELECT projection.srsCode AS epsgCode, rcs.scaleBarType AS units, rcs.displayFormatType AS formats " +
						"FROM ReportConnectSpatialEntity rcs INNER JOIN rcs.projectionByDisplayProjId AS projection " +
						"WHERE rcs.reportId = :reportId"),
		@NamedQuery(name = QueryNameConstants.FIND_BY_REPORT_ID,
				query = "from ReportConnectSpatialEntity where reportId = :reportId"),
        @NamedQuery(name = ReportConnectSpatialEntity.DELETE_BY_ID_LIST,
                query = "DELETE FROM ReportConnectSpatialEntity where id in :idList")
})
@EqualsAndHashCode(exclude = {"id", "reportConnectServiceAreases"})
public class ReportConnectSpatialEntity implements Serializable {

    public static final String DELETE_BY_ID_LIST = "reportConnectSpatialEntity.deleteByIdList";

    @Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "map_proj_id")
	private ProjectionEntity projectionByMapProjId;
	
	@ManyToOne
	@JoinColumn(name = "display_proj_id")
	private ProjectionEntity projectionByDisplayProjId;
	
	@Column(name = "report_id", nullable = false)
	private Long reportId;
	
	@Column(name = "map_center", length = 255)
	private String mapCenter;
	
	@Column(name = "map_zoom")
	private Integer mapZoom;
	
	@Column(columnDefinition = "text", name = "map_extent")
	private String mapExtent;
	
	@Column(name = "display_format", length = 255)
    @Enumerated(EnumType.STRING)
	private CoordinatesFormat displayFormatType;
	
	@Column(name = "measurement_units", length = 255)
	private String measurementUnits;
	
	@Column(name = "scalebar_units", length = 255)
    @Enumerated(EnumType.STRING)
    private ScaleBarUnits scaleBarType;

	@Column(columnDefinition = "text", name = "vector_styles")
	private String vectorStyles;

	@Column(name = "app_version", nullable = false, length = 255)
	private String appVersion;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "reportConnectSpatial", cascade = CascadeType.ALL)
	private Set<ReportConnectServiceAreasEntity> reportConnectServiceAreases;

    public ReportConnectSpatialEntity() {
    }

    @Builder
    public ReportConnectSpatialEntity(ProjectionEntity projectionByMapProjId,
                                      ProjectionEntity projectionByDisplayProjId,
                                      long reportId, String mapCenter, int mapZoom, String mapExtent,
                                      CoordinatesFormat displayFormatType, String measurementUnits,
                                      ScaleBarUnits scaleBarType, String vectorStyles, String appVersion,
                                      Set<ReportConnectServiceAreasEntity> reportConnectServiceAreases) {
        this.projectionByMapProjId = projectionByMapProjId;
        this.projectionByDisplayProjId = projectionByDisplayProjId;
        this.reportId = reportId;
        this.mapCenter = mapCenter;
        this.mapZoom = mapZoom;
        this.mapExtent = mapExtent;
        this.displayFormatType = displayFormatType;
        this.measurementUnits = measurementUnits;
        this.scaleBarType = scaleBarType;
        this.vectorStyles = vectorStyles;
        this.appVersion = appVersion;
        this.reportConnectServiceAreases = reportConnectServiceAreases;
    }

    public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProjectionEntity getProjectionByMapProjId() {
		return this.projectionByMapProjId;
	}

	public void setProjectionByMapProjId(ProjectionEntity projectionByMapProjId) {
		this.projectionByMapProjId = projectionByMapProjId;
	}

	public ProjectionEntity getProjectionByDisplayProjId() {
		return this.projectionByDisplayProjId;
	}

	public void setProjectionByDisplayProjId(ProjectionEntity projectionByDisplayProjId) {
		this.projectionByDisplayProjId = projectionByDisplayProjId;
	}

	public Long getReportId() {
		return this.reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getMapCenter() {
		return this.mapCenter;
	}

	public void setMapCenter(String mapCenter) {
		this.mapCenter = mapCenter;
	}

	public Integer getMapZoom() {
		return this.mapZoom;
	}

	public void setMapZoom(Integer mapZoom) {
		this.mapZoom = mapZoom;
	}

	public String getMapExtent() {
		return this.mapExtent;
	}

	public void setMapExtent(String mapExtent) {
		this.mapExtent = mapExtent;
	}

	public String getMeasurementUnits() {
		return this.measurementUnits;
	}

	public void setMeasurementUnits(String measurementUnits) {
		this.measurementUnits = measurementUnits;
	}

    public CoordinatesFormat getDisplayFormatType() {
        return displayFormatType;
    }

    public void setDisplayFormatType(CoordinatesFormat displayFormatType) {
        this.displayFormatType = displayFormatType;
    }

    public ScaleBarUnits getScaleBarType() {
        return scaleBarType;
    }

    public void setScaleBarType(ScaleBarUnits scaleBarType) {
        this.scaleBarType = scaleBarType;
    }

    public String getVectorStyles() {
		return this.vectorStyles;
	}

	public void setVectorStyles(String vectorStyles) {
		this.vectorStyles = vectorStyles;
	}


	public String getAppVersion() {
		return this.appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public Set<ReportConnectServiceAreasEntity> getReportConnectServiceAreases() {
		return this.reportConnectServiceAreases;
	}

	public void setReportConnectServiceAreases(Set<ReportConnectServiceAreasEntity> reportConnectServiceAreases) {
		this.reportConnectServiceAreases = reportConnectServiceAreases;
	}

}
