/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "gfcm")
@NamedQueries({
        @NamedQuery(name = GfcmEntity2.DISABLE_GFMC_AREAS, query = "UPDATE GfcmEntity SET enabled = false"),
        @NamedQuery(name = GfcmEntity2.GFMC_BY_INTERSECT,
                query = "FROM GfcmEntity WHERE intersects(geom, :shape) = true AND enabled = true"),
        @NamedQuery(name = GfcmEntity2.SEARCH_GFCM, query = "FROM GfcmEntity where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid"),
        @NamedQuery(name = GfcmEntity2.SEARCH_GFCM_NAMES_BY_CODE, query = "From GfcmEntity where code in (SELECT distinct(code) from GfcmEntity where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid)"),
        @NamedQuery(name = GfcmEntity2.GFCM_COLUMNS, query = "SELECT gfcm.id as gid, gfcm.name AS name, gfcm.code AS code FROM GfcmEntity AS gfcm WHERE gfcm.id in (:ids)")})

public class GfcmEntity2 extends BaseAreaEntity2 {

    public static final String DISABLE_GFMC_AREAS = "gfmcEntity.disable";
    public static final String GFMC_BY_INTERSECT = "gfmcEntity.gfmcByIntersect";
    public static final String SEARCH_GFCM = "gfcmEntity.SearchgfcmByNameOrCode";
    public static final String SEARCH_GFCM_NAMES_BY_CODE = "gfcmEntity.searchNamesByCode";
    public static final String GFCM_COLUMNS = "gfcmEntity.gfcmColumns";

	@Id
	@Column(name = "gid")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="gfcm_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
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
        GfcmEntity2 that = (GfcmEntity2) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}