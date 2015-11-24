package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.spatial.entity.converter.CharBooleanConverter;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "projection", schema = "spatial")
@EqualsAndHashCode(exclude = { "reportConnectSpatialsForMapProjId", "reportConnectSpatialsForDisplayProjId"})
public class ProjectionEntity implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
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
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectionByMapProjId", cascade = CascadeType.ALL)
	private Set<ReportConnectSpatialEntity> reportConnectSpatialsForMapProjId;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectionByDisplayProjId", cascade = CascadeType.ALL)
	private Set<ReportConnectSpatialEntity> reportConnectSpatialsForDisplayProjId;

	public ProjectionEntity() {
	}

    @Builder
    public ProjectionEntity(String name, int srsCode, String projDef, String formats, String units, Boolean isWorld, Set<ReportConnectSpatialEntity> reportConnectSpatialsForMapProjId, Set<ReportConnectSpatialEntity> reportConnectSpatialsForDisplayProjId) {
        this.name = name;
        this.srsCode = srsCode;
        this.projDef = projDef;
        this.formats = formats;
        this.units = units;
        this.isWorld = isWorld;
        this.reportConnectSpatialsForMapProjId = reportConnectSpatialsForMapProjId;
        this.reportConnectSpatialsForDisplayProjId = reportConnectSpatialsForDisplayProjId;
    }

    public long getId() {
		return this.id;
	}

	public void setId(long id) {
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

	public Boolean getWorld() {
		return this.isWorld;
	}

	public void setWorld(Boolean isWorld) {
		this.isWorld = isWorld;
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
