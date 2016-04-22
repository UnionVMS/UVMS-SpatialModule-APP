package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.spatial.entity.converter.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "projection", schema = "spatial")
@EqualsAndHashCode(exclude = { "reportConnectSpatialsForMapProjId", "reportConnectSpatialsForDisplayProjId"})
@NamedQueries({
        @NamedQuery(name = ProjectionEntity.FIND_BY_SRS_CODE, query = "FROM ProjectionEntity p WHERE p.srsCode = :srsCode"),
        @NamedQuery(name = ProjectionEntity.FIND_PROJECTION_BY_ID,
				query = "SELECT projection.srsCode AS epsgCode, projection.units AS units, projection.isWorld AS global, projection.extent as extent, projection.axis as axis " +
						"FROM ProjectionEntity projection WHERE projection.id = :id")
})
public class ProjectionEntity implements Serializable {

    public static final String FIND_PROJECTION_BY_ID = "ReportLayerConfig.findProjectionById";
    public static final String FIND_BY_SRS_CODE = "Projection.findBySrsCode";

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", unique = true, nullable = false, length = 255)
	private String name;
	
	@Column(name = "srs_code", unique = true, nullable = false)
	private int srsCode;

	@Column(columnDefinition = "text", name = "proj_def")
	private String projDef;
	
	@Column(name = "formats", nullable = false, length = 255)
	private String formats;
	
	@Column(name = "units", nullable = false, length = 255)
	private String units;

	@Convert(converter = CharBooleanConverter.class)
	@Column(name = "world", nullable = false, length = 1)
	private Boolean isWorld;

	@Column(name = "extent", nullable = false, length = 255)
	private String extent;

	@Column(name = "axis", nullable = false, length = 3)
	private String axis;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectionByMapProjId", cascade = CascadeType.ALL)
	private Set<ReportConnectSpatialEntity> reportConnectSpatialsForMapProjId;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectionByDisplayProjId", cascade = CascadeType.ALL)
	private Set<ReportConnectSpatialEntity> reportConnectSpatialsForDisplayProjId;

	public ProjectionEntity() {
        // No args constructor for use in serialization
    }

    public ProjectionEntity(Long id){
        this.id = id;
    }

    @Builder
    public ProjectionEntity(String name, int srsCode, String projDef, String formats, String units,
                            Boolean isWorld, Set<ReportConnectSpatialEntity> reportConnectSpatialsForMapProjId,
                            Set<ReportConnectSpatialEntity> reportConnectSpatialsForDisplayProjId) {
        this.name = name;
        this.srsCode = srsCode;
        this.projDef = projDef;
        this.formats = formats;
        this.units = units;
        this.isWorld = isWorld;
        this.reportConnectSpatialsForMapProjId = reportConnectSpatialsForMapProjId;
        this.reportConnectSpatialsForDisplayProjId = reportConnectSpatialsForDisplayProjId;
    }

    public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSrsCode() {
		return this.srsCode;
	}

	public void setSrsCode(int srsCode) {
		this.srsCode = srsCode;
	}

	public String getProjDef() {
		return this.projDef;
	}

	public void setProjDef(String projDef) {
		this.projDef = projDef;
	}

	public String getFormats() {
		return this.formats;
	}

	public void setFormats(String formats) {
		this.formats = formats;
	}

	public String getUnits() {
		return this.units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public Boolean getIsWorld() {
		return this.isWorld;
	}

	public void setIsWorld(Boolean isWorld) {
		this.isWorld = isWorld;
	}

	public String getExtent() {
		return extent;
	}

	public void setExtent(String extent) {
		this.extent = extent;
	}

	public String getAxis() {
		return axis;
	}

	public void setAxis(String axis) {
		this.axis = axis;
	}

	public Set<ReportConnectSpatialEntity> getReportConnectSpatialsForMapProjId() {
		return this.reportConnectSpatialsForMapProjId;
	}

	public void setReportConnectSpatialsForMapProjId(Set<ReportConnectSpatialEntity> reportConnectSpatialsForMapProjId) {
		this.reportConnectSpatialsForMapProjId = reportConnectSpatialsForMapProjId;
	}

	public Set<ReportConnectSpatialEntity> getReportConnectSpatialsForDisplayProjId() {
		return this.reportConnectSpatialsForDisplayProjId;
	}

	public void setReportConnectSpatialsForDisplayProjId(
			Set<ReportConnectSpatialEntity> reportConnectSpatialsForDisplayProjId) {
		this.reportConnectSpatialsForDisplayProjId = reportConnectSpatialsForDisplayProjId;
	}
}
