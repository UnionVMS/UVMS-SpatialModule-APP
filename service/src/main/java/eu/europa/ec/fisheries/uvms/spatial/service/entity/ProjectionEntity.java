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
import eu.europa.ec.fisheries.uvms.domain.CharBooleanConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "projection")
@NamedQueries({
        @NamedQuery(name = ProjectionEntity.FIND_BY_SRS_CODE, query = "FROM ProjectionEntity p WHERE p.srsCode = :srsCode"),
        @NamedQuery(name = ProjectionEntity.FIND_PROJECTION_BY_ID,
				query = "SELECT projection.srsCode AS epsgCode, projection.units AS units, projection.isWorld AS global, projection.extent as extent, projection.axis as axis, projection.projDef as projDef, projection.worldExtent as worldExtent " +
						"FROM ProjectionEntity projection WHERE projection.id = :id")
})
@EqualsAndHashCode(callSuper = true, exclude = { "reportConnectSpatialsForMapProjId", "reportConnectSpatialsForDisplayProjId"})
@Data
@ToString(exclude = { "reportConnectSpatialsForMapProjId", "reportConnectSpatialsForDisplayProjId"})
public class ProjectionEntity extends BaseEntity {

    public static final String FIND_PROJECTION_BY_ID = "ReportLayerConfig.findProjectionById";
    public static final String FIND_BY_SRS_CODE = "Projection.findBySrsCode";
	
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

	@Convert(converter = CharBooleanConverter.class)
	@Column(name = "world", nullable = false, length = 1)
	private Boolean isWorld;

	@Column(nullable = false, length = 255)
	private String extent;

	@Column(nullable = false, length = 3)
	private String axis;

	@Column(name = "world_extent", nullable = false, length = 255)
	private String worldExtent;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectionByMapProjId", cascade = CascadeType.ALL)
	private Set<ReportConnectSpatialEntity> reportConnectSpatialsForMapProjId;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectionByDisplayProjId", cascade = CascadeType.ALL)
	private Set<ReportConnectSpatialEntity> reportConnectSpatialsForDisplayProjId;

	public ProjectionEntity() {
        // why JPA why
    }
}