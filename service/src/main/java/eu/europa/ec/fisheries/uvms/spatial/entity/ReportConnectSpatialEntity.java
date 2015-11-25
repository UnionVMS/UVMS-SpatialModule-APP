package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.CoordinatesFormat;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScaleBarUnits;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "report_connect_spatial", schema = "spatial")
@NamedQueries(
		@NamedQuery(name = QueryNameConstants.FIND_MAP_PROJ_BY_ID,
				query = "SELECT projection.srsCode AS epsgCode, projection.units AS units, projection.isWorld AS global " +
						"FROM ReportConnectSpatialEntity rcs INNER JOIN rcs.projectionByMapProjId AS projection " +
						"WHERE rcs.reportId = :reportId")
)
@EqualsAndHashCode(exclude = {"id", "reportConnectServiceAreases"})
public class ReportConnectSpatialEntity implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "map_proj_id")
	private ProjectionEntity projectionByMapProjId;
	
	@ManyToOne
	@JoinColumn(name = "display_proj_id")
	private ProjectionEntity projectionByDisplayProjId;
	
	@Column(name = "report_id", nullable = false)
	private long reportId;
	
	@Column(name = "map_center", length = 255)
	private String mapCenter;
	
	@Column(name = "map_zoom")
	private int mapZoom;
	
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

    public long getId() {
		return this.id;
	}

	public void setId(long id) {
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

	public long getReportId() {
		return this.reportId;
	}

	public void setReportId(long reportId) {
		this.reportId = reportId;
	}

	public String getMapCenter() {
		return this.mapCenter;
	}

	public void setMapCenter(String mapCenter) {
		this.mapCenter = mapCenter;
	}

	public int getMapZoom() {
		return this.mapZoom;
	}

	public void setMapZoom(int mapZoom) {
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
