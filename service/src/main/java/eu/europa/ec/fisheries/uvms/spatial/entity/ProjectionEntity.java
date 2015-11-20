package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.spatial.entity.converter.CharBooleanConverter;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "projection", schema = "spatial", uniqueConstraints = { @UniqueConstraint(columnNames = "name"),
		@UniqueConstraint(columnNames = "srs_code") })
public class ProjectionEntity implements Serializable {
	
	private static final long serialVersionUID = 6797853213499502866L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "name", unique = true, nullable = false, length = 255)
	private String name;
	
	@Column(name = "srs_code", unique = true, nullable = false)
	private int srsCode;

	@Column(columnDefinition = "text",name = "proj_def", nullable = false)
	private String projDef;
	
	@Column(name = "formats", nullable = false, length = 255)
	private String formats;
	
	@Column(name = "units", nullable = false, length = 255)
	private String units;

	@Convert(converter = CharBooleanConverter.class)
	@Column(name = "world", nullable = false, length = 1)
	private Boolean isSystemWide;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectionByMapProjId", cascade = CascadeType.ALL)
	private Set<ReportConnectSpatialEntity> reportConnectSpatialsForMapProjId;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectionByDisplayProjId", cascade = CascadeType.ALL)
	private Set<ReportConnectSpatialEntity> reportConnectSpatialsForDisplayProjId;

	public ProjectionEntity() {
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

	public Boolean getIsSystemWide() {
		return this.isSystemWide;
	}

	public void setIsSystemWide(Boolean isSystemWide) {
		this.isSystemWide = isSystemWide;
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
