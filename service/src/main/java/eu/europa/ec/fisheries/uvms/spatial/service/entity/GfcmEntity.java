/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "gfcm")
@NamedQueries({
        @NamedQuery(name = GfcmEntity.DISABLE_GFMC_AREAS, query = "UPDATE GfcmEntity2 SET enabled = false"),
        @NamedQuery(name = GfcmEntity.GFMC_BY_INTERSECT,
                query = "FROM GfcmEntity2 WHERE intersects(geom, :shape) = true AND enabled = true"),
        @NamedQuery(name = GfcmEntity.SEARCH_GFCM, query = "FROM GfcmEntity2 where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid"),
        @NamedQuery(name = GfcmEntity.SEARCH_GFCM_NAMES_BY_CODE, query = "From GfcmEntity2 where code in (SELECT distinct(code) from GfcmEntity2 where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid)"),
        @NamedQuery(name = GfcmEntity.AREA_BY_AREA_CODES, query = "From GfcmEntity2 where code in :code AND enabled=true "),
        @NamedQuery(name = GfcmEntity.GFCM_COLUMNS, query = "SELECT gfcm.id as gid, gfcm.name AS name, gfcm.code AS code FROM GfcmEntity2 AS gfcm WHERE gfcm.id in (:ids)")})

public class GfcmEntity extends BaseAreaEntity {

    public static final String DISABLE_GFMC_AREAS = "gfmcEntity2.disable";
    public static final String GFMC_BY_INTERSECT = "gfmcEntity2.gfmcByIntersect";
    public static final String SEARCH_GFCM = "gfcmEntity2.SearchgfcmByNameOrCode";
    public static final String SEARCH_GFCM_NAMES_BY_CODE = "gfcmEntity2.searchNamesByCode";
    public static final String GFCM_COLUMNS = "gfcmEntity2.gfcmColumns";
    public static final String AREA_BY_AREA_CODES = "gfcmEntity2.areaByAreaCodes";

	@Id
	@Column(name = "gid")
	@SequenceGenerator(name="SEQ_GFCM_GEN", sequenceName="gfcm_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GFCM_GEN")
    @JsonProperty("gid")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GfcmEntity that = (GfcmEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}