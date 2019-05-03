/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "projection")
@NamedQueries({
        @NamedQuery(name = ProjectionEntity2.FIND_BY_SRS_CODE, query = "FROM ProjectionEntity2 p WHERE p.srsCode = :srsCode"),
        @NamedQuery(name = ProjectionEntity2.FIND_PROJECTION_BY_ID,
				query = "SELECT projection.srsCode AS epsgCode, projection.units AS units, projection.world AS global, projection.extent as extent, projection.axis as axis, projection.projDef as projDef, projection.worldExtent as worldExtent " +
						"FROM ProjectionEntity2 projection WHERE projection.id = :id")
})

public class ProjectionEntity2 {

    public static final String FIND_PROJECTION_BY_ID = "ReportLayerConfig2.findProjectionById";
    public static final String FIND_BY_SRS_CODE = "Projection2.findBySrsCode";
	
	@Id
	@Column(name = "id")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="projection_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
	private Long id;
	
	@Column(unique = true, nullable = false, length = 255)
	private String name;
	
	@Column(name = "srs_code", unique = true, nullable = false)
	private int srsCode;

	@Column(name = "proj_def")
	private String projDef;
	
	@Column(nullable = false, length = 255)
	private String formats;
	
	@Column(nullable = false, length = 255)
	private String units;

	@Column(name = "world", nullable = false)
	private Boolean world;

	@Column(nullable = false, length = 255)
	private String extent;

	@Column(nullable = false, length = 3)
	private String axis;

	@Column(name = "world_extent", nullable = false, length = 255)
	private String worldExtent;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectionByMapProjId", cascade = CascadeType.ALL)
	private Set<ReportConnectSpatialEntity2> reportConnectSpatialsForMapProjId;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectionByDisplayProjId", cascade = CascadeType.ALL)
	private Set<ReportConnectSpatialEntity2> reportConnectSpatialsForDisplayProjId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSrsCode() {
		return srsCode;
	}

	public void setSrsCode(int srsCode) {
		this.srsCode = srsCode;
	}

	public String getProjDef() {
		return projDef;
	}

	public void setProjDef(String projDef) {
		this.projDef = projDef;
	}

	public String getFormats() {
		return formats;
	}

	public void setFormats(String formats) {
		this.formats = formats;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public Boolean getWorld() {
		return world;
	}

	public void setWorld(Boolean world) {
		this.world = world;
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

	public String getWorldExtent() {
		return worldExtent;
	}

	public void setWorldExtent(String worldExtent) {
		this.worldExtent = worldExtent;
	}

	public Set<ReportConnectSpatialEntity2> getReportConnectSpatialsForMapProjId() {
		return reportConnectSpatialsForMapProjId;
	}

	public void setReportConnectSpatialsForMapProjId(Set<ReportConnectSpatialEntity2> reportConnectSpatialsForMapProjId) {
		this.reportConnectSpatialsForMapProjId = reportConnectSpatialsForMapProjId;
	}

	public Set<ReportConnectSpatialEntity2> getReportConnectSpatialsForDisplayProjId() {
		return reportConnectSpatialsForDisplayProjId;
	}

	public void setReportConnectSpatialsForDisplayProjId(Set<ReportConnectSpatialEntity2> reportConnectSpatialsForDisplayProjId) {
		this.reportConnectSpatialsForDisplayProjId = reportConnectSpatialsForDisplayProjId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProjectionEntity2 that = (ProjectionEntity2) o;
		return srsCode == that.srsCode &&
				Objects.equals(id, that.id) &&
				Objects.equals(name, that.name) &&
				Objects.equals(projDef, that.projDef) &&
				Objects.equals(formats, that.formats) &&
				Objects.equals(units, that.units) &&
				Objects.equals(world, that.world) &&
				Objects.equals(extent, that.extent) &&
				Objects.equals(axis, that.axis) &&
				Objects.equals(worldExtent, that.worldExtent) &&
				Objects.equals(reportConnectSpatialsForMapProjId, that.reportConnectSpatialsForMapProjId) &&
				Objects.equals(reportConnectSpatialsForDisplayProjId, that.reportConnectSpatialsForDisplayProjId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, srsCode, projDef, formats, units, world, extent, axis, worldExtent, reportConnectSpatialsForMapProjId, reportConnectSpatialsForDisplayProjId);
	}
}